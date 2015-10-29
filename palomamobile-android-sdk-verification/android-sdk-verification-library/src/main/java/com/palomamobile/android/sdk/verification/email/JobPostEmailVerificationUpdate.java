package com.palomamobile.android.sdk.verification.email;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IEmailVerificationService#updateEmailVerification(String, String, VerificationEmailUpdate)}
 * used to finalize an existing verification of an email address with a verification code (received on the email address being verified).
 * Once this job is completed (with success or failure) it posts {@link EventEmailVerificationUpdated} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 */
public class JobPostEmailVerificationUpdate extends BaseRetryPolicyAwareJob<Void> {

    private String emailAddress;
    private String code;

    /**
     * Create a new job instance.
     *
     * @param emailAddress email address being verified
     * @param code received via the email address being verified
     */
    public JobPostEmailVerificationUpdate(String emailAddress, String code) {
        this(new Params(0).requireNetwork().persist(), emailAddress, code);
    }

    /**
     * Create a new job instance.
     *
     * @param params job behaviour attributes
     * @param emailAddress email address being verified
     * @param code received via the email address being verified
     */
    public JobPostEmailVerificationUpdate(Params params, String emailAddress, String code) {
        super(params);
        this.emailAddress = emailAddress;
        this.code = code;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventEmailVerificationUpdated(this, throwable));
    }

    @Override
    public Void syncRun(boolean postEvent) throws Throwable {
        IEmailVerificationManager verificationManager = ServiceSupport.Instance.getServiceManager(IEmailVerificationManager.class);
        verificationManager.getService().updateEmailVerification(getRetryId(), emailAddress, new VerificationEmailUpdate(code));
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventEmailVerificationUpdated(this, (Void) null));
        }
        return null;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "JobPostEmailVerificationUpdate{" +
                "emailAddress='" + emailAddress + '\'' +
                ", code='" + code + '\'' +
                "} " + super.toString();
    }
}
