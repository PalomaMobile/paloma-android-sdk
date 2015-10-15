package com.palomamobile.android.sdk.verification;

import android.test.InstrumentationTestCase;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.util.LatchedBusListener;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class VerificationManagerInstrumentationTest extends InstrumentationTestCase {

    public static final String TAG = VerificationManagerInstrumentationTest.class.getSimpleName();

    private VerificationManager verificationManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        verificationManager = (VerificationManager) ServiceSupport.Instance.getServiceManager(IVerificationManager.class);
    }

    public void testCreateEmailVerification() throws Throwable {
        IVerificationTestHelperService verificationTestHelperService = ServiceSupport.Instance.getRestAdapter().create(IVerificationTestHelperService.class);
        String tmp = "tmp_" + Long.toString(System.currentTimeMillis());
        String emailAddress = tmp + "@example.com";

        createEmailVerification(emailAddress);
        VerificationEmailUpdate verificationEmailUpdate = verificationTestHelperService.getEmailVerification(UUID.randomUUID().toString(), emailAddress);
        String code = verificationEmailUpdate.getCode();
        updateEmailVerification(emailAddress, code);
    }

    private void createEmailVerification(String emailAddress) throws Throwable {
        final LatchedBusListener<EventEmailVerificationCreated> busListener = new LatchedBusListener<>(EventEmailVerificationCreated.class);
        ServiceSupport.Instance.getEventBus().register(busListener);
        ServiceSupport.Instance.getJobManager().addJob(verificationManager.createJobCreateEmailVerification(emailAddress));
        busListener.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(busListener);
        EventEmailVerificationCreated event = busListener.getEvent();
        assertNotNull(event);
        assertNull(event.getFailure());
    }

    private void updateEmailVerification(String emailAddress, String code) throws Throwable {
        final LatchedBusListener<EventEmailVerificationUpdated> busListener = new LatchedBusListener<>(EventEmailVerificationUpdated.class);
        ServiceSupport.Instance.getEventBus().register(busListener);
        ServiceSupport.Instance.getJobManager().addJob(verificationManager.createJobUpdateEmailVerification(emailAddress, code));
        busListener.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(busListener);
        EventEmailVerificationUpdated event = busListener.getEvent();
        assertNotNull(event);
        assertNull(event.getFailure());
    }

}
