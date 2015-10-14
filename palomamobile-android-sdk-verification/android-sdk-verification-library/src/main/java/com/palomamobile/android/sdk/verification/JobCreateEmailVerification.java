package com.palomamobile.android.sdk.verification;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 *
 */
public class JobCreateEmailVerification extends BaseRetryPolicyAwareJob<Void> {
    private String emailAddress;

    public JobCreateEmailVerification(String emailAddress) {
        this(new Params(0).requireNetwork().persist(), emailAddress);
    }

    public JobCreateEmailVerification(Params params, String emailAddress) {
        super(params);
        this.emailAddress = emailAddress;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventEmailVerificationCreated(this, throwable));
    }

    @Override
    public Void syncRun(boolean postEvent) throws Throwable {
        IVerificationManager verificationManager = ServiceSupport.Instance.getServiceManager(IVerificationManager.class);
        verificationManager.getService().postEmailVerification(getRetryId(), emailAddress, "");
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventEmailVerificationCreated(this, (Void) null));
        }
        return null;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String toString() {
        return "JobCreateEmailVerification{" +
                "emailAddress='" + emailAddress + '\'' +
                "} " + super.toString();
    }
}
