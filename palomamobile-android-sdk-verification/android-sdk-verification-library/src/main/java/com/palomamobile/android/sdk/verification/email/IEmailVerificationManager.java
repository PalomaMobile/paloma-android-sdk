package com.palomamobile.android.sdk.verification.email;

import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Methods in this interface provide convenient job creation methods that provide easy access
 * to the underlying {@link IEmailVerificationService} functionality. App developers can either use {@link BaseRetryPolicyAwareJob}
 * job instances returned by the {@code createJob...()} methods, or create custom jobs that invoke
 * methods of the {@link IEmailVerificationService} returned by {@link IServiceManager#getService()}
 *
 * <br/>
 * To get a concrete implementation of this interface call
 * {@code ServiceSupport.Instance.getServiceManager(IVerificationManager.class)}
 */
public interface IEmailVerificationManager extends IServiceManager<IEmailVerificationService> {

    /**
     * Create a new {@link JobCreateEmailVerification}.
     * @param emailAddress email address being verified
     * @return new job
     */
    JobCreateEmailVerification createJobCreateEmailVerification(String emailAddress);

    /**
     * Create a new {@link JobPostEmailVerificationUpdate}.
     * @param emailAddress email address being verified
     * @param code received via the email address being verified
     * @return new job
     */
    JobPostEmailVerificationUpdate createJobUpdateEmailVerification(String emailAddress, String code);

    /**
     * Create a new {@link JobPostUserVerifiedEmail}.
     * @param emailAddress email address being verified
     * @param code received via the email address being verified
     * @return new job
     */
    JobPostUserVerifiedEmail createJobPostUserVerifiedEmail(String emailAddress, String code);

}
