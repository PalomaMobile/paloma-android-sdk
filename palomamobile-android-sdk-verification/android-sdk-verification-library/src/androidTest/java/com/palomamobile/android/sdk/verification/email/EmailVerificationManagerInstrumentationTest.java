package com.palomamobile.android.sdk.verification.email;

import android.test.InstrumentationTestCase;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.util.LatchedBusListener;
import com.palomamobile.android.sdk.user.EventLocalUserUpdated;
import com.palomamobile.android.sdk.user.EventPasswordResetCompleted;
import com.palomamobile.android.sdk.user.JobGetUser;
import com.palomamobile.android.sdk.user.JobRegisterUser;
import com.palomamobile.android.sdk.user.JobResetPassword;
import com.palomamobile.android.sdk.user.PasswordUserCredential;
import com.palomamobile.android.sdk.user.TestUtilities;
import com.palomamobile.android.sdk.user.User;
import com.palomamobile.android.sdk.user.VerificationMethod;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EmailVerificationManagerInstrumentationTest extends InstrumentationTestCase {

    private IEmailVerificationTestHelperService verificationTestHelperService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        verificationTestHelperService = ServiceSupport.Instance.getRestAdapter().create(IEmailVerificationTestHelperService.class);
    }

    public void testCreateEmailVerification() throws Throwable {
        String tmp = "tmp_" + Long.toString(System.currentTimeMillis());
        String emailAddress = tmp + "@example.com";

        createEmailVerification(emailAddress);
        VerificationEmailUpdate verificationEmailUpdate = verificationTestHelperService.getEmailVerification(UUID.randomUUID().toString(), emailAddress);
        String code = verificationEmailUpdate.getCode();
        updateEmailVerification(emailAddress, code);
    }

    public void testUpdateUserEmailAddress() throws Throwable {
        String tmp = "tmp_" + Long.toString(System.currentTimeMillis());
        String emailAddress = tmp + "@example.com";

        createEmailVerification(emailAddress);
        VerificationEmailUpdate verificationEmailUpdate = verificationTestHelperService.getEmailVerification(UUID.randomUUID().toString(), emailAddress);
        String code = verificationEmailUpdate.getCode();
        updateUserEmailAddress(emailAddress, code);
    }

    private void createEmailVerification(String emailAddress) throws Throwable {
        final LatchedBusListener<EventEmailVerificationCreated> busListener = new LatchedBusListener<>(EventEmailVerificationCreated.class);
        ServiceSupport.Instance.getEventBus().register(busListener);
        ServiceSupport.Instance.getJobManager().addJob(new JobCreateEmailVerification(emailAddress));
        busListener.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(busListener);
        EventEmailVerificationCreated event = busListener.getEvent();
        assertNotNull(event);
        assertNull(event.getFailure());
    }

    private void updateEmailVerification(String emailAddress, String code) throws Throwable {
        final LatchedBusListener<EventEmailVerificationUpdated> busListener = new LatchedBusListener<>(EventEmailVerificationUpdated.class);
        ServiceSupport.Instance.getEventBus().register(busListener);
        ServiceSupport.Instance.getJobManager().addJob(new JobPostEmailVerificationUpdate(emailAddress, code));
        busListener.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(busListener);
        EventEmailVerificationUpdated event = busListener.getEvent();
        assertNotNull(event);
        assertNull(event.getFailure());
    }

    private void updateUserEmailAddress(String emailAddress, String code) throws Throwable {

        final String tmpSelf = String.valueOf(System.currentTimeMillis());
        User self = TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(tmpSelf, tmpSelf));
        assertNotNull(self);

        final LatchedBusListener<EventLocalUserUpdated> busListener = new LatchedBusListener<>(EventLocalUserUpdated.class);
        ServiceSupport.Instance.getEventBus().register(busListener);
        ServiceSupport.Instance.getJobManager().addJob(new JobPostUserVerifiedEmail(emailAddress, code));
        busListener.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(busListener);
        EventLocalUserUpdated event = busListener.getEvent();
        assertNotNull(event);
        assertNull(event.getFailure());
        assertEquals(emailAddress, event.getSuccess().getEmailAddress());

    }

    public void testPasswordReset() throws Throwable {
        final String tmpSelf = String.valueOf(System.currentTimeMillis());
        new JobRegisterUser(new PasswordUserCredential(tmpSelf, tmpSelf)).syncRun(false);
        String emailAddress = tmpSelf + "@example.com";

        createEmailVerification(emailAddress);
        VerificationEmailUpdate verificationEmailUpdate = verificationTestHelperService.getEmailVerification(UUID.randomUUID().toString(), emailAddress);
        String code = verificationEmailUpdate.getCode();
        updateUserEmailAddress(emailAddress, code);

        User registeredUser = new JobGetUser().syncRun(false);
        assertEquals(emailAddress, registeredUser.getEmailAddress());

        //LOGOUT
        ServiceSupport.Instance.getCache().clear();


        //oh no - user forgot their password :)

        createEmailVerification(emailAddress);
        VerificationEmailUpdate verificationEmailUpdateForReset = verificationTestHelperService.getEmailVerification(UUID.randomUUID().toString(), emailAddress);

        String codeForReset = verificationEmailUpdateForReset.getCode();
        String newPassword = "new_password_987";
        LatchedBusListener<EventPasswordResetCompleted> resetCompletedLatchedBusListener = new LatchedBusListener<>(EventPasswordResetCompleted.class);
        ServiceSupport.Instance.getEventBus().register(resetCompletedLatchedBusListener);
        //job reset password will first fire an EventPasswordResetCompleted and then EventLocalUserUpdated since we passed TRUE to constructor
        ServiceSupport.Instance.getJobManager().addJob(new JobResetPassword(codeForReset, newPassword, VerificationMethod.Email, emailAddress, true));
        resetCompletedLatchedBusListener.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(resetCompletedLatchedBusListener);

        //make sure EventPasswordResetCompleted gets here
        assertNotNull(resetCompletedLatchedBusListener.getEvent());
        assertNull(resetCompletedLatchedBusListener.getEvent().getFailure());
        assertEquals(registeredUser, resetCompletedLatchedBusListener.getEvent().getSuccess());

        //next we should get EventLocalUserUpdated, these 2 lines could be moved up a bit but keeping them here for readability
        final LatchedBusListener<EventLocalUserUpdated> userLoginBusListener = new LatchedBusListener<>(EventLocalUserUpdated.class);
        ServiceSupport.Instance.getEventBus().register(userLoginBusListener);

        userLoginBusListener.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(userLoginBusListener);
        EventLocalUserUpdated event = userLoginBusListener.getEvent();
        assertNotNull(event);
        assertNull(event.getFailure());
        assertEquals(registeredUser, event.getSuccess());
    }

}
