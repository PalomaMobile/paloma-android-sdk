package com.palomamobile.android.sdk.verification.email;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.EventLocalUserUpdated;
import com.palomamobile.android.sdk.user.IUserManager;
import com.palomamobile.android.sdk.user.User;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IEmailVerificationService#updateUserEmail(String, long, UserVerifiedEmailUpdate)}
 * used to finalize an existing verification of an email address with a verification code (received on the email address being verified)
 * and assign the verified email to a user.
 * Once this job is completed (with success or failure) it posts {@link com.palomamobile.android.sdk.user.EventLocalUserUpdated} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 */
public class JobPostUserVerifiedEmail extends BaseRetryPolicyAwareJob<User> {

    private long userId;
    private UserVerifiedEmailUpdate userVerifiedEmailUpdate;

    /**
     * Create a new job instance.
     *
     * @param emailAddress email address being verified
     * @param code received via the email address being verified
     */
    public JobPostUserVerifiedEmail(String emailAddress, String code) {
        this(new Params(0).requireNetwork().persist(), emailAddress, code);
    }

    /**
     * Create a new job instance.
     *
     * @param params job behaviour attributes
     * @param emailAddress email address being verified
     * @param code received via the email address being verified
     */
    public JobPostUserVerifiedEmail(Params params, String emailAddress, String code) {
        super(params);
        this.userId = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser().getId();
        this.userVerifiedEmailUpdate = new UserVerifiedEmailUpdate(emailAddress, code);
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventLocalUserUpdated(this, throwable));
    }

    @Override
    public User syncRun(boolean postEvent) throws Throwable {
        IEmailVerificationManager verificationManager = ServiceSupport.Instance.getServiceManager(IEmailVerificationManager.class);
        User result = verificationManager.getService().updateUserEmail(getRetryId(), userId, userVerifiedEmailUpdate);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventLocalUserUpdated(this, result));
        }
        return result;
    }

    public UserVerifiedEmailUpdate getUserVerifiedEmailUpdate() {
        return userVerifiedEmailUpdate;
    }

    @Override
    public String toString() {
        return "JobPostUserVerifiedEmail{" +
                "userId=" + userId +
                ", userVerifiedEmailUpdate=" + userVerifiedEmailUpdate +
                "} " + super.toString();
    }
}
