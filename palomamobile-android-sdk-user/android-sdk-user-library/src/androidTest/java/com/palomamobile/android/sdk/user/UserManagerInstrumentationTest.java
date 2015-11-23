package com.palomamobile.android.sdk.user;

import android.test.InstrumentationTestCase;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.TestUserManager;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.util.LatchedBusListener;
import com.palomamobile.android.sdk.core.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserManagerInstrumentationTest extends InstrumentationTestCase {

    public static final Logger logger = LoggerFactory.getLogger(UserManagerInstrumentationTest.class);

    private static String facebookAppId = null;
    private static String facebookAppSecret = null;


    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());

        try {
            facebookAppId = Utilities.getValueFromAppMetadata(getInstrumentation().getContext(), "facebook.app.id");
            facebookAppSecret = Utilities.getValueFromAppMetadata(getInstrumentation().getContext(), "facebook.app.secret");
        } catch (Exception e) {
            logger.warn("FacebookApp NOT setup, will not be not running instrumentation tests that involve creating FB users, provide your own values for a Facebook test app id and secret.");
        }
    }

    private boolean isFacebookAppSetup() {
        return facebookAppId != null && facebookAppId.length() > 0 && facebookAppSecret != null && facebookAppSecret.length() > 0;
    }

    public void testRequestRegisterFacebookUserNoExplicitUserName() throws Throwable {
        if (!isFacebookAppSetup()) {
            logger.info("FacebookApp NOT setup, not running Test: testRequestRegisterFacebookUserNoExplicitUserName()");
            return;
        }

        FacebookSdk.sdkInitialize(getInstrumentation().getContext());

        TestUserManager testUserManager = new TestUserManager(facebookAppSecret, facebookAppId);
        AccessToken accessTokenForTestUser = testUserManager.getAccessTokenForPrivateUser(null);


        final String fbUserId = accessTokenForTestUser.getUserId();
        final String fbAuthToken = accessTokenForTestUser.getToken();

        final LatchedBusListener<EventLocalUserUpdated> latchedBusListener = new LatchedBusListener<>(EventLocalUserUpdated.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListener);
        JobRegisterUser jobRegisterUserViaFacebook = new JobRegisterUser(new FbUserCredential(fbUserId, fbAuthToken));
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobRegisterUserViaFacebook);
        latchedBusListener.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListener);

        deleteFbTestUser(accessTokenForTestUser);
        assertNotNull(latchedBusListener.getEvent());
        assertNotNull(latchedBusListener.getEvent().getSuccess());
        assertNull(latchedBusListener.getEvent().getFailure());
    }

    public void testRequestRegisterFacebookUserBadCreds() throws Throwable {
        if (!isFacebookAppSetup()) {
            logger.info("FacebookApp NOT setup, not running Test: testRequestRegisterFacebookUserBadCreds()");
            return;
        }

        FacebookSdk.sdkInitialize(getInstrumentation().getContext());

        TestUserManager testUserManager = new TestUserManager(facebookAppSecret, facebookAppId);
        AccessToken accessTokenForTestUser = testUserManager.getAccessTokenForPrivateUser(null);


        final String fbUserId = accessTokenForTestUser.getUserId();
        final String fbAuthToken = accessTokenForTestUser.getToken();

        final LatchedBusListener<EventLocalUserUpdated> latchedBusListener = new LatchedBusListener<>(EventLocalUserUpdated.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListener);
        JobRegisterUser jobRegisterUserViaFacebook = new JobRegisterUser(new FbUserCredential(fbUserId + "0", fbAuthToken));
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobRegisterUserViaFacebook);
        latchedBusListener.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListener);

        deleteFbTestUser(accessTokenForTestUser);
        assertNotNull(latchedBusListener.getEvent());
        assertNull(latchedBusListener.getEvent().getSuccess());
        Throwable throwable = latchedBusListener.getEvent().getFailure();
        assertNotNull(throwable);
        assertTrue(throwable instanceof RetrofitError);
        assertEquals(401, ((RetrofitError) throwable).getResponse().getStatus());
    }


    public void testRequestRegisterFacebookUserWithExplicitUserName() throws Throwable {
        if (!isFacebookAppSetup()) {
            logger.info("FacebookApp NOT setup, not running Test: testRequestRegisterFacebookUserWithExplicitUserName()");
            return;
        }

        FacebookSdk.sdkInitialize(getInstrumentation().getContext());

        TestUserManager testUserManager = new TestUserManager(facebookAppSecret, facebookAppId);
        AccessToken accessTokenForTestUser = testUserManager.getAccessTokenForPrivateUser(null);

        final String userName = "tmp_" + System.currentTimeMillis();
        final String fbUserId = accessTokenForTestUser.getUserId();
        final String fbAuthToken = accessTokenForTestUser.getToken();
        FbUserCredential credential = new FbUserCredential(fbUserId, fbAuthToken);
        credential.setUsername(userName);

        final LatchedBusListener<EventLocalUserUpdated> latchedBusListener = new LatchedBusListener<>(EventLocalUserUpdated.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListener);
        JobRegisterUser jobRegisterUserViaFacebook = new JobRegisterUser(credential);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobRegisterUserViaFacebook);
        latchedBusListener.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListener);

        assertNotNull(latchedBusListener.getEvent());
        assertNotNull(latchedBusListener.getEvent().getSuccess());
        assertNull(latchedBusListener.getEvent().getFailure());

        // register the same FB user again but with different userName - should return 303 with the user in body
        FbUserCredential wrongCredential = new FbUserCredential(fbUserId, fbAuthToken);
        credential.setUsername("wrong_username");

        final LatchedBusListener<EventLocalUserUpdated> latchedBusListener2 = new LatchedBusListener<>(EventLocalUserUpdated.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListener2);
        jobRegisterUserViaFacebook = new JobRegisterUser(wrongCredential);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobRegisterUserViaFacebook);
        latchedBusListener2.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListener2);

        assertNotNull(latchedBusListener2.getEvent());
        User localUser = latchedBusListener2.getEvent().getSuccess();
        assertNotNull(localUser);
        assertNull(latchedBusListener2.getEvent().getFailure());
        assertEquals(userName, localUser.getUsername());

        deleteFbTestUser(accessTokenForTestUser);
    }


    private void deleteFbTestUser(AccessToken accessTokenForPrivateUser) {
        new GraphRequest(
                accessTokenForPrivateUser,
                "/" + accessTokenForPrivateUser.getUserId(),
                null,
                HttpMethod.DELETE,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        logger.debug(graphResponse.getRawResponse());
                        assertNull(graphResponse.getError());
                    }
                }
        ).executeAndWait();
    }

    public void testRequestRegisterPasswordUser() throws Throwable {

        long curTime = System.nanoTime();
        final String userName = "usr_" + curTime;
        final String userPassword = "pwd_" + curTime;

        final LatchedBusListener<EventLocalUserUpdated> latchedBusListener = new LatchedBusListener<>(EventLocalUserUpdated.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListener);
        PasswordUserCredential credential = new PasswordUserCredential(userName, userPassword);

        credential.setDisplayName("toodles");
        credential.setDateOfBirth("1978-11-28");
        Map<String, String> custom = new HashMap<>();
        custom.put("hair", "red");
        custom.put("eyes", "green");
        credential.setCustom(custom);

        JobRegisterUser jobRegisterUserViaPassword = new JobRegisterUser(credential);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobRegisterUserViaPassword);
        latchedBusListener.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListener);

        assertNotNull(latchedBusListener.getEvent());
        User newUser = latchedBusListener.getEvent().getSuccess();
        assertNotNull(newUser);
        assertEquals(userName, newUser.getUsername());

        assertEquals("toodles", newUser.getDisplayName());
        assertEquals("1978-11-28", newUser.getDateOfBirth());
        assertEquals(2, newUser.getCustom().size());
        assertEquals("red", newUser.getCustom().get("hair"));
        assertEquals("green", newUser.getCustom().get("eyes"));

        assertNull(latchedBusListener.getEvent().getFailure());

        //try and register the same username again with different password
        try {
            JobRegisterUser jobRegisterUserViaPassword2 = new JobRegisterUser(new PasswordUserCredential(userName, "other_password"));
            jobRegisterUserViaPassword2.syncRun(false);
            fail("RetrofitError expected");
        } catch (RetrofitError retrofitError) {
            assertEquals(409, retrofitError.getResponse().getStatus());
        }

        //try and register the same username again with the same password
        JobRegisterUser jobRegisterUserViaPassword2 = new JobRegisterUser(new PasswordUserCredential(userName, userPassword));
        User localUser2 = jobRegisterUserViaPassword2.syncRun(false);
        assertEquals(localUser2, newUser);
    }

    public void testUpdateGetUser() throws Throwable {
        long curTime = System.nanoTime();
        final String username = "usr_" + curTime;
        final String userPassword = "pwd_" + curTime;
        User user = new JobRegisterUser(new PasswordUserCredential(username, userPassword)).syncRun();

        UserUpdate update = new UserUpdate();
        update.setDisplayName("toodles");
        update.setDateOfBirth("1978-11-28");
        Map<String, String> custom = new HashMap<>();
        custom.put("hair", "red");
        custom.put("eyes", "green");
        update.setCustom(custom);

        final LatchedBusListener<EventLocalUserUpdated> latchedBusListenerUpdate = new LatchedBusListener<>(EventLocalUserUpdated.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListenerUpdate);
        JobPostUserUpdate jobPostUserUpdate = new JobPostUserUpdate(update);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobPostUserUpdate);
        latchedBusListenerUpdate.await(20, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListenerUpdate);

        assertNull(latchedBusListenerUpdate.getEvent().getFailure());
        assertNotNull(latchedBusListenerUpdate.getEvent());
        User updatedUser = latchedBusListenerUpdate.getEvent().getSuccess();
        assertNotNull(updatedUser);
        assertEquals(username, updatedUser.getUsername());
        assertEquals("toodles", updatedUser.getDisplayName());
        assertEquals("1978-11-28", updatedUser.getDateOfBirth());
        assertEquals(2, updatedUser.getCustom().size());
        assertEquals("red", updatedUser.getCustom().get("hair"));
        assertEquals("green", updatedUser.getCustom().get("eyes"));


        final LatchedBusListener<EventLocalUserUpdated> latchedBusListenerGet = new LatchedBusListener<>(EventLocalUserUpdated.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListenerGet);
        JobGetUser jobGetUser = new JobGetUser();
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetUser);
        latchedBusListenerGet.await(20, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListenerGet);

        assertNotNull(latchedBusListenerGet.getEvent());
        assertNull(latchedBusListenerGet.getEvent().getFailure());
        User gottenUser = latchedBusListenerGet.getEvent().getSuccess();
        assertEquals(updatedUser, gottenUser);
    }

    public void testPasswordReset() throws Throwable {
        //test implemented in Verification module as verification is required
    }

    public void testPasswordUpdate() throws Throwable {
        final String tmp = "tmp_" + System.currentTimeMillis();
        User registered = new JobRegisterUser(new PasswordUserCredential(tmp, tmp)).syncRun(false);

        UserUpdate userUpdate = new UserUpdate();
        String newPassword = "newPassword123";
        userUpdate.setPassword(newPassword);
        new JobPostUserUpdate(userUpdate).syncRun(false);

        try {
            new JobLoginUser(new PasswordUserCredential(tmp, tmp)).syncRun(false);
            fail("Shouldn't be able to login with old user credentials");
        } catch (Exception e) {
            //expected
        }

        User logedin = new JobLoginUser(new PasswordUserCredential(tmp, newPassword)).syncRun(false);
        assertEquals(registered, logedin);
    }

    public void testLoginFbUser() throws Throwable {
        if (!isFacebookAppSetup()) {
            logger.info("FacebookApp NOT setup, not running Test: testLoginFbUser()");
            return;
        }

        FacebookSdk.sdkInitialize(getInstrumentation().getContext());
        TestUserManager testUserManager = new TestUserManager(facebookAppSecret, facebookAppId);
        AccessToken accessTokenForTestUser = testUserManager.getAccessTokenForPrivateUser(null);
        final String fbUserId = accessTokenForTestUser.getUserId();
        final String fbAuthToken = accessTokenForTestUser.getToken();

        try {
            JobLoginUser jobLoginUserViaFacebook = new JobLoginUser(new FbUserCredential(fbUserId, fbAuthToken));
            jobLoginUserViaFacebook.syncRun(false);
            fail("RetrofitError expected - even if FB creds valid no paloma user exists yet");
        } catch (RetrofitError retrofitError) {
            assertEquals(404, retrofitError.getResponse().getStatus());
        }

        final String userName = "tmp_" + System.currentTimeMillis();
        FbUserCredential credential = new FbUserCredential(fbUserId, fbAuthToken);
        credential.setUsername(userName);
        JobRegisterUser jobRegisterUserViaFacebook = new JobRegisterUser(credential);
        User userRegistered = jobRegisterUserViaFacebook.syncRun(false);
        assertEquals(userName, userRegistered.getUsername());

        JobLoginUser jobLoginUserViaFacebook = new JobLoginUser(new FbUserCredential(fbUserId, fbAuthToken));
        User userLogedin = jobLoginUserViaFacebook.syncRun(false);
        assertEquals(userRegistered, userLogedin);

        deleteFbTestUser(accessTokenForTestUser);
    }


    public void testLoginPwdUser() throws Throwable {
        final String tmp = "tmp_" + System.currentTimeMillis();
        JobLoginUser jobLoginUserViaPassword = new JobLoginUser(new PasswordUserCredential(tmp, tmp));
        try {
            jobLoginUserViaPassword.syncRun(false);
            fail("RetrofitError expected - can't verify creds as no paloma user exists yet");
        } catch (RetrofitError retrofitError) {
            assertNotNull(retrofitError.getResponse());
            assertEquals(404, retrofitError.getResponse().getStatus());
        }

        JobRegisterUser jobRegisterUserViaFacebook = new JobRegisterUser(new PasswordUserCredential(tmp, tmp));
        User userRegistered = jobRegisterUserViaFacebook.syncRun(false);
        assertEquals(tmp, userRegistered.getUsername());

        jobLoginUserViaPassword = new JobLoginUser(new PasswordUserCredential(tmp, tmp));
        User userLogedin = jobLoginUserViaPassword.syncRun(false);
        assertEquals(userRegistered, userLogedin);
    }


}
