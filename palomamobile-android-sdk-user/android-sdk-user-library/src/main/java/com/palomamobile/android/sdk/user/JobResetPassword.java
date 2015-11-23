package com.palomamobile.android.sdk.user;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IUserService#resetPassword(String, String, String, PasswordUpdate)}
 * used to reset user's password (usually done to login a user who has forgotten their password).
 * Prior to executing this
 * job the user needs to obtain a verification code via an existing verification channel that is associated with
 * the user's account (e.g. request an email verification code - see the Verification module on how to do this)
 * Once this is done and the verification code is received by the user the application will be able to provide the following info:
 * <ul>
 *     <li>verification code received</li>
 *     <li>verification method used to receive verification code (eg: {@link VerificationMethod#Email})</li>
 *     <li>verification method address used to receive verification code (eg: the email address on which the code was received)</li>
 *     <li>value for the new password </li>
 * </ul>
 * Once this job is completed (with success or failure) it posts {@link EventPasswordResetCompleted} on the
 * {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * For convenience, this job can be configured to schedule a {@link JobLoginUser} if the password reset was successful.
 * This can be achieved by setting the constructor parameter {@code triggerLogin = true}.
 */
public class JobResetPassword extends BaseRetryPolicyAwareJob<User> {

    private PasswordUpdate passwordUpdate;
    private VerificationMethod verificationMethod;
    private String verificationAddress;
    private boolean triggerLogin;

    /**
     * Create a new instance.
     *
     * @param verificationCode verification code received via verificationMethod at verificationAddress
     * @param newPassword value for the new password
     * @param verificationMethod method used to receive the verification code
     * @param verificationAddress address at which the verification code was received
     * @param triggerLogin if {@code true} and execution of this password reset is successful then schedule a {@link JobLoginUser}
     */
    public JobResetPassword(String newPassword, String verificationCode, VerificationMethod verificationMethod, String verificationAddress, boolean triggerLogin) {
        //do NOT set .requireNetwork() - this will make the job fail quickly rather than wait for network to become available
        this(new Params(0), verificationCode, newPassword, verificationMethod, verificationAddress, triggerLogin);
    }

    /**
     * Create a new instance.
     *
     * @param params custom job params
     * @param verificationCode verification code received via verificationMethod at verificationAddress
     * @param newPassword value for the new password
     * @param verificationMethod method used to receive the verification code
     * @param verificationAddress address at which the verification code was received
     * @param triggerLogin if {@code true} and execution of this password reset is successful then schedule a {@link JobLoginUser}
     */
    public JobResetPassword(Params params, String newPassword, String verificationCode, VerificationMethod verificationMethod, String verificationAddress, boolean triggerLogin) {
        super(params);
        setMaxAttempts(2);
        this.passwordUpdate = new PasswordUpdate(newPassword, verificationCode);
        this.verificationMethod = verificationMethod;
        this.verificationAddress = verificationAddress;
        this.triggerLogin = triggerLogin;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventPasswordResetCompleted(this, throwable));
    }

    @Override
    public User syncRun(boolean postEvent) throws Throwable {
        IUserService userService = ServiceSupport.Instance.getServiceManager(IUserManager.class).getService();
        User result = userService.resetPassword(getRetryId(), verificationMethod.getApiChannelName(), verificationAddress, passwordUpdate);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventPasswordResetCompleted(this, result));
        }
        if (isTriggerLogin()) {
            ServiceSupport.Instance.getJobManager().addJobInBackground(new JobLoginUser(new PasswordUserCredential(result.getUsername(), passwordUpdate.getPassword())));
        }
        return result;
    }

    /**
     * @return {@code true} if job was configured at construction time to schedule a {@link JobLoginUser} job on success, false otherwise
     */
    public boolean isTriggerLogin() {
        return triggerLogin;
    }
}
