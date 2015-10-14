package com.palomamobile.android.sdk.verification;

import android.test.InstrumentationTestCase;
import com.palomamobile.android.sdk.core.ServiceSupport;

/**
 *
 */
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
        verificationManager.createJobCreateEmailVerification("karel.herink@gmail.com").syncRun();
    }
}
