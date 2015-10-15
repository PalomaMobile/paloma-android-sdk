package com.palomamobile.android.sdk.verification.email;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IEmailVerificationService#updateEmailVerification(String, String, VerificationEmailUpdate)}
 * used to finalize an existing verification of an email address with a verification code (received on the email address being verified).
 * Once this job is completed (with success or failure) it posts {@link EventEmailVerificationCreated} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 */
public class JobUpdateEmailVerification extends BaseRetryPolicyAwareJob<Void> {

    private String emailAddress;
    private String code;
    private String applicationName;

    public JobUpdateEmailVerification(String emailAddress, String code, String applicationName) {
        this(new Params(0).requireNetwork().persist(), emailAddress, code, applicationName);
    }

    public JobUpdateEmailVerification(Params params, String emailAddress, String code, String applicationName) {
        super(params);
        this.applicationName = applicationName;
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
        verificationManager.getService().updateEmailVerification(getRetryId(), emailAddress, new VerificationEmailUpdate(applicationName, code));
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventEmailVerificationUpdated(this, (Void) null));
        }
        return null;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "JobUpdateEmailVerification{" +
                "applicationName='" + applicationName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", code='" + code + '\'' +
                "} " + super.toString();
    }
}
