package com.palomamobile.android.sdk.friend;

import android.test.InstrumentationTestCase;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.TestUserManager;
import com.palomamobile.android.sdk.user.FbUserCredential;
import com.palomamobile.android.sdk.user.PasswordUserCredential;
import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.util.LatchedBusListener;
import com.palomamobile.android.sdk.core.util.Utilities;
import com.palomamobile.android.sdk.user.TestUtilities;
import com.palomamobile.android.sdk.user.User;

import java.util.concurrent.TimeUnit;

public class FriendManagerInstrumentationTest extends InstrumentationTestCase {

    public static final String TAG = FriendManagerInstrumentationTest.class.getSimpleName();

    //TODO: add values for these facebookAppId & facebookAppSecret in strings.xml (see comments)
    private static String facebookAppId = null;
    private static String facebookAppSecret = null;


    private IFriendManager friendManager;


    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        friendManager = ServiceSupport.Instance.getServiceManager(IFriendManager.class);

        try {
            facebookAppId = Utilities.getValueFromAppMetadata(getInstrumentation().getContext(), "facebook.app.id");
            facebookAppSecret = Utilities.getValueFromAppMetadata(getInstrumentation().getContext(), "facebook.app.secret");
        } catch (Exception e) {
            Log.w(TAG, "FacebookApp NOT setup, will not be not running instrumentation tests that involve creating FB users, provide your own values for a Facebook test app id and secret .");
        }
    }

    private boolean isFacebookAppSetup() {
        return facebookAppId != null && facebookAppId.length() > 0 && facebookAppSecret != null && facebookAppSecret.length() > 0;
    }

    public void testRequestRefreshFriends() throws Throwable {
        TestUtilities.registerUserSynchronous(this);

        final LatchedBusListener<EventFriendsListReceived> latchedBusListener = new LatchedBusListener<>(EventFriendsListReceived.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListener);
        JobGetFriends jobGetFriends = friendManager.createJobGetFriends();
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetFriends);
        latchedBusListener.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListener);

        assertNotNull(latchedBusListener.getEvent());
        assertNull(latchedBusListener.getEvent().getFailure());
        PaginatedResponse<Friend> friends = latchedBusListener.getEvent().getSuccess();
        assertNull(friends.getEmbedded());
    }

    public void testRequestPostSocialUserCredentialFb() throws Throwable {
        if (!isFacebookAppSetup()) {
            Log.w(TAG, "FacebookApp NOT setup, not running Test: testRequestPostSocialUserCredentialFb()");
            return;
        }

        //----------------------------------------------

        //1. Create USERS A & B with Fb

        FacebookSdk.sdkInitialize(getInstrumentation().getContext());
        TestUserManager testUserManager = new TestUserManager(facebookAppSecret, facebookAppId);

        //set-up userA
        AccessToken accessTokenA = testUserManager.getAccessTokenForPrivateUser(null);
        final String fbUserIdA = accessTokenA.getUserId();
        final String fbAuthTokenA = accessTokenA.getToken();

        //set-up userB
        AccessToken accessTokenB = testUserManager.getAccessTokenForPrivateUser(null);
        final String fbUserIdB = accessTokenB.getUserId();
        final String fbAuthTokenB = accessTokenB.getToken();

        try {
            assertNotSame(fbUserIdA, fbUserIdB);

            //----------------------------------------------

            //2. Set-up FRIENDSHIP on facebook
            //request friend A -> B
            new GraphRequest(
                    accessTokenA,
                    "/" + fbUserIdA + "/friends/" + fbUserIdB + "",
                    null,
                    HttpMethod.POST,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            Log.d(TAG, "request friend A -> B : " + response.getRawResponse());
                            assertNull(response.getError());
                        }
                    }
            ).executeAndWait();

            //confirm friend B -> A
            new GraphRequest(
                    accessTokenB,
                    "/" + fbUserIdB + "/friends/" + fbUserIdA + "",
                    null,
                    HttpMethod.POST,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            Log.d(TAG, "confirm friend B -> A : " + response.getRawResponse());
                            assertNull(response.getError());
                        }
                    }
            ).executeAndWait();


            //----------------------------------------------

            //3. Let Paloma Friend service match the facebook friends

            //Create paloma user A (login / pwd)
            String tmp = String.valueOf(System.currentTimeMillis());
            PasswordUserCredential passwordUserCredentialA = new PasswordUserCredential(tmp, tmp);
            User userA = TestUtilities.registerUserSynchronous(this, passwordUserCredentialA);
            assertNotNull(userA);

            //Add fb credential to userA
            final LatchedBusListener<EventSocialUserCredentialsPosted> latchedBusListenerFriendsA = new LatchedBusListener<>(EventSocialUserCredentialsPosted.class);
            ServiceSupport.Instance.getEventBus().register(latchedBusListenerFriendsA);
            JobPostSocialUserCredential jobPostSocialUserCredential = friendManager.createJobPostSocialUserCredential(new SocialUserCredential(fbUserIdA, fbAuthTokenA, FbUserCredential.FB_CREDENTIAL_TYPE));
            ServiceSupport.Instance.getJobManager().addJobInBackground(jobPostSocialUserCredential);
            latchedBusListenerFriendsA.await(200, TimeUnit.SECONDS);
            ServiceSupport.Instance.getEventBus().unregister(latchedBusListenerFriendsA);
            assertNotNull(latchedBusListenerFriendsA.getEvent());
            Throwable failure = latchedBusListenerFriendsA.getEvent().getFailure();
            //no errors expected yet
            assertNull(failure);
            PaginatedResponse<Friend> friendPaginatedResponse = friendManager.createJobGetFriends().syncRun();
            assertNull(friendPaginatedResponse.getEmbedded());

            //Create paloma user B (login / pwd)
            User userB = TestUtilities.registerUserSynchronous(this);
            assertNotNull(userB);

            //If A & B are friends on FB then Paloma will mirror the relationship
            final LatchedBusListener<EventSocialUserCredentialsPosted> latchedBusListenerFriendsB = new LatchedBusListener<>(EventSocialUserCredentialsPosted.class);
            ServiceSupport.Instance.getEventBus().register(latchedBusListenerFriendsB);
            jobPostSocialUserCredential = friendManager.createJobPostSocialUserCredential(new SocialUserCredential(fbUserIdB, fbAuthTokenB, FbUserCredential.FB_CREDENTIAL_TYPE));
            ServiceSupport.Instance.getJobManager().addJobInBackground(jobPostSocialUserCredential);
            latchedBusListenerFriendsB.await(20, TimeUnit.SECONDS);
            ServiceSupport.Instance.getEventBus().unregister(latchedBusListenerFriendsB);
            assertNotNull(latchedBusListenerFriendsB.getEvent());
            assertNull(latchedBusListenerFriendsB.getEvent().getFailure());

            PaginatedResponse<Friend> friendsB = friendManager.createJobGetFriends().syncRun();
            assertNotNull(friendsB);
            //expect to find userA as a friend
            assertEquals(1, friendsB.getEmbedded().getItems().size());
            assertEquals(userA.getId(), friendsB.getEmbedded().getItems().get(0).getUserId());

            //switch to be userA
            TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(passwordUserCredentialA.getUsername(), passwordUserCredentialA.getUserPassword()));
            final LatchedBusListener<EventFriendsListReceived> latchedBusListener = new LatchedBusListener<>(EventFriendsListReceived.class);
            ServiceSupport.Instance.getEventBus().register(latchedBusListener);
            JobGetFriends jobGetFriends = friendManager.createJobGetFriends();
            ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetFriends);
            latchedBusListener.await(10, TimeUnit.SECONDS);
            ServiceSupport.Instance.getEventBus().unregister(latchedBusListener);

            assertNotNull(latchedBusListener.getEvent());
            assertNull(latchedBusListener.getEvent().getFailure());
            PaginatedResponse<Friend> friendsA = latchedBusListener.getEvent().getSuccess();
            //expect to find userB as a friend
            assertEquals(1, friendsA.getEmbedded().getItems().size());
            assertEquals(userB.getId(), friendsA.getEmbedded().getItems().get(0).getUserId());
        } finally {
            deleteFbTestUser(accessTokenA);
            deleteFbTestUser(accessTokenB);
        }
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
                        Log.d(TAG, graphResponse.getRawResponse());
                        assertNull(graphResponse.getError());
                    }
                }
        ).executeAndWait();
    }

    public void testRequestPutRelationshipUserId() throws Throwable {
        //Create paloma user A (login / pwd)
        String tmp = String.valueOf(System.currentTimeMillis());
        PasswordUserCredential passwordUserCredentialA = new PasswordUserCredential(tmp, tmp);
        final User userA = TestUtilities.registerUserSynchronous(this, passwordUserCredentialA);
        Log.d(TAG, "registerUserSynchronous userA: " + userA);
        assertNotNull(userA);

        //Create paloma user B (login / pwd)
        tmp = String.valueOf(System.currentTimeMillis());
        PasswordUserCredential passwordUserCredentialB = new PasswordUserCredential(tmp, tmp);
        final User userB = TestUtilities.registerUserSynchronous(this, passwordUserCredentialB);
        Log.d(TAG, "registerUserSynchronous userB: " + userB);
        assertNotNull(userB);


        //request a friendship as user B with user A
        final LatchedBusListener<EventRelationshipUpdated> latchedBusListenerRelationshipB = new LatchedBusListener<>(EventRelationshipUpdated.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListenerRelationshipB);
        JobPutRelationship jobPutRelationship = friendManager.createJobPutRelationship(userA.getId(), new RelationAttributes(RelationAttributes.Type.friend));
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobPutRelationship);
        latchedBusListenerRelationshipB.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListenerRelationshipB);

        assertNotNull(latchedBusListenerRelationshipB.getEvent());
        assertNull(latchedBusListenerRelationshipB.getEvent().getFailure());
        Relationship relationshipB = latchedBusListenerRelationshipB.getEvent().getSuccess();
        assertNotNull(relationshipB);

        //build the relationshipExpectedB
        RelationAttributes mineB = new RelationAttributes(RelationAttributes.Type.friend);
        mineB.setTrigger(RelationAttributes.Trigger.request);
        RelationAttributes reciA = new RelationAttributes(RelationAttributes.Type.unknown);
        reciA.setTrigger(RelationAttributes.Trigger.reciprocal);
        Relationship relationshipExpectedB = new Relationship(userB.getId(), mineB, userA.getId(), userA.getUsername(), reciA);
        assertEquals(relationshipExpectedB, relationshipB);


        //switch to be userA
        TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(passwordUserCredentialA.getUsername(), passwordUserCredentialA.getUserPassword()));

        //request a friendship as user A with user B (reciprocate)
        final LatchedBusListener<EventRelationshipUpdated> latchedBusListenerRelationshipA = new LatchedBusListener<>(EventRelationshipUpdated.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListenerRelationshipA);
        jobPutRelationship = friendManager.createJobPutRelationship(userB.getId(), new RelationAttributes(RelationAttributes.Type.friend));
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobPutRelationship);
        latchedBusListenerRelationshipA.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListenerRelationshipA);

        assertNotNull(latchedBusListenerRelationshipA.getEvent());
        assertNull(latchedBusListenerRelationshipA.getEvent().getFailure());
        Relationship relationshipA = latchedBusListenerRelationshipA.getEvent().getSuccess();
        assertNotNull(relationshipA);

        //build the relationshipExpectedB
        RelationAttributes mineA = new RelationAttributes(RelationAttributes.Type.friend);
        mineA.setTrigger(RelationAttributes.Trigger.request);
        RelationAttributes reciB = new RelationAttributes(RelationAttributes.Type.friend);
        reciB.setTrigger(RelationAttributes.Trigger.request);
        Relationship relationshipExpectedA = new Relationship(userA.getId(), mineA, userB.getId(), userB.getUsername(), reciB);
        assertEquals(relationshipExpectedA, relationshipA);

        // if the relationships are all good then both A and B should have a friend in their list

        //check friends for A
        final LatchedBusListener<EventFriendsListReceived> latchedBusFriendsListenerA = new LatchedBusListener<>(EventFriendsListReceived.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusFriendsListenerA);
        JobGetFriends jobGetFriends = friendManager.createJobGetFriends();
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetFriends);
        latchedBusFriendsListenerA.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusFriendsListenerA);

        assertNotNull(latchedBusFriendsListenerA.getEvent());
        assertNull(latchedBusFriendsListenerA.getEvent().getFailure());
        PaginatedResponse<Friend> friendsA = latchedBusFriendsListenerA.getEvent().getSuccess();
        assertEquals(1, friendsA.getEmbedded().getItems().size());
        assertEquals(userB.getId(), friendsA.getEmbedded().getItems().get(0).getUserId());
        assertEquals(userB.getUsername(), friendsA.getEmbedded().getItems().get(0).getUsername());

        //check friends for B
        //switch to be userB
        TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(passwordUserCredentialB.getUsername(), passwordUserCredentialB.getUserPassword()));
        final LatchedBusListener<EventFriendsListReceived> latchedBusFriendsListenerB = new LatchedBusListener<>(EventFriendsListReceived.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusFriendsListenerB);
        jobGetFriends = friendManager.createJobGetFriends();
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetFriends);
        latchedBusFriendsListenerB.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusFriendsListenerB);

        assertNotNull(latchedBusFriendsListenerB.getEvent());
        assertNull(latchedBusFriendsListenerB.getEvent().getFailure());
        PaginatedResponse<Friend> friendsB = latchedBusFriendsListenerB.getEvent().getSuccess();
        assertEquals(1, friendsB.getEmbedded().getItems().size());
        assertEquals(userA.getId(), friendsB.getEmbedded().getItems().get(0).getUserId());
        assertEquals(userA.getUsername(), friendsB.getEmbedded().getItems().get(0).getUsername());

    }

    public void testRequestPutRelationshipUsername() throws Throwable {
        //Create paloma user A (login / pwd)
        String tmp = "userA_" + String.valueOf(System.currentTimeMillis());
        PasswordUserCredential passwordUserCredentialA = new PasswordUserCredential(tmp, tmp);
        final User userA = TestUtilities.registerUserSynchronous(this, passwordUserCredentialA);
        Log.d(TAG, "registerUserSynchronous userA: " + userA);
        assertNotNull(userA);

        //Create paloma user B (login / pwd)
        tmp = "userB_" + String.valueOf(System.currentTimeMillis());
        PasswordUserCredential passwordUserCredentialB = new PasswordUserCredential(tmp, tmp);
        final User userB = TestUtilities.registerUserSynchronous(this, passwordUserCredentialB);
        Log.d(TAG, "registerUserSynchronous userB: " + userB);
        assertNotNull(userB);


        //request a friendship as user B with user A
        final LatchedBusListener<EventRelationshipUpdated> latchedBusListenerRelationshipB = new LatchedBusListener<>(EventRelationshipUpdated.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListenerRelationshipB);
        JobPutRelationship jobPutRelationship = friendManager.createJobPutRelationship(userA.getUsername(), new RelationAttributes(RelationAttributes.Type.friend));
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobPutRelationship);
        latchedBusListenerRelationshipB.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListenerRelationshipB);

        assertNotNull(latchedBusListenerRelationshipB.getEvent());
        assertNull(latchedBusListenerRelationshipB.getEvent().getFailure());
        Relationship relationshipB = latchedBusListenerRelationshipB.getEvent().getSuccess();
        assertNotNull(relationshipB);

        //build the relationshipExpectedB
        RelationAttributes mineB = new RelationAttributes(RelationAttributes.Type.friend);
        mineB.setTrigger(RelationAttributes.Trigger.request);
        RelationAttributes reciA = new RelationAttributes(RelationAttributes.Type.unknown);
        reciA.setTrigger(RelationAttributes.Trigger.reciprocal);
        Relationship relationshipExpectedB = new Relationship(userB.getId(), mineB, userA.getId(), userA.getUsername(), reciA);
        assertEquals(relationshipExpectedB, relationshipB);


        //switch to be userA
        TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(passwordUserCredentialA.getUsername(), passwordUserCredentialA.getUserPassword()));

        //request a friendship as user A with user B (reciprocate)
        final LatchedBusListener<EventRelationshipUpdated> latchedBusListenerRelationshipA = new LatchedBusListener<>(EventRelationshipUpdated.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListenerRelationshipA);
        jobPutRelationship = friendManager.createJobPutRelationship(userB.getUsername(), new RelationAttributes(RelationAttributes.Type.friend));
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobPutRelationship);
        latchedBusListenerRelationshipA.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListenerRelationshipA);

        assertNotNull(latchedBusListenerRelationshipA.getEvent());
        assertNull(latchedBusListenerRelationshipA.getEvent().getFailure());
        Relationship relationshipA = latchedBusListenerRelationshipA.getEvent().getSuccess();
        assertNotNull(relationshipA);

        //build the relationshipExpectedB
        RelationAttributes mineA = new RelationAttributes(RelationAttributes.Type.friend);
        mineA.setTrigger(RelationAttributes.Trigger.request);
        RelationAttributes reciB = new RelationAttributes(RelationAttributes.Type.friend);
        reciB.setTrigger(RelationAttributes.Trigger.request);
        Relationship relationshipExpectedA = new Relationship(userA.getId(), mineA, userB.getId(), userB.getUsername(), reciB);
        assertEquals(relationshipExpectedA, relationshipA);

        // if the relationships are all good then both A and B should have a friend in their list

        //check friends for A
        final LatchedBusListener<EventFriendsListReceived> latchedBusFriendsListenerA = new LatchedBusListener<>(EventFriendsListReceived.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusFriendsListenerA);
        JobGetFriends jobGetFriends = friendManager.createJobGetFriends();
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetFriends);
        latchedBusFriendsListenerA.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusFriendsListenerA);

        assertNotNull(latchedBusFriendsListenerA.getEvent());
        assertNull(latchedBusFriendsListenerA.getEvent().getFailure());
        PaginatedResponse<Friend> friendsA = latchedBusFriendsListenerA.getEvent().getSuccess();
        assertEquals(1, friendsA.getEmbedded().getItems().size());
        assertEquals(userB.getId(), friendsA.getEmbedded().getItems().get(0).getUserId());
        assertEquals(userB.getUsername(), friendsA.getEmbedded().getItems().get(0).getUsername());

        //check friends for B
        //switch to be userB
        TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(passwordUserCredentialB.getUsername(), passwordUserCredentialB.getUserPassword()));
        final LatchedBusListener<EventFriendsListReceived> latchedBusFriendsListenerB = new LatchedBusListener<>(EventFriendsListReceived.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusFriendsListenerB);
        jobGetFriends = friendManager.createJobGetFriends();
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetFriends);
        latchedBusFriendsListenerB.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusFriendsListenerB);

        assertNotNull(latchedBusFriendsListenerB.getEvent());
        assertNull(latchedBusFriendsListenerB.getEvent().getFailure());
        PaginatedResponse<Friend> friendsB = latchedBusFriendsListenerB.getEvent().getSuccess();
        assertEquals(1, friendsB.getEmbedded().getItems().size());
        assertEquals(userA.getId(), friendsB.getEmbedded().getItems().get(0).getUserId());
        assertEquals(userA.getUsername(), friendsB.getEmbedded().getItems().get(0).getUsername());

    }

}
