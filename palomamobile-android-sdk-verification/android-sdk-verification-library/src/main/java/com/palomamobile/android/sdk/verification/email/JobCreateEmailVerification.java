package com.palomamobile.android.sdk.verification.email;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IEmailVerificationService#postEmailVerification(String, String, String)}
 * used to create a new verification of an email address.
 * Once this job is completed (with success or failure) it posts {@link EventEmailVerificationCreated} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 */
public class JobCreateEmailVerification extends BaseRetryPolicyAwareJob<Void> {
    private final String emailAddress;

    /**
     * Create a new job instance.
     * @param emailAddress email address to be verified
     */
    public JobCreateEmailVerification(String emailAddress) {
        this(new Params(0).requireNetwork().persist(), emailAddress);
    }

    /**
     * Create a new job instance.
     * @param params job behaviour attributes
     * @param emailAddress email address to be verified
     */
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
        IEmailVerificationManager verificationManager = ServiceSupport.Instance.getServiceManager(IEmailVerificationManager.class);
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
