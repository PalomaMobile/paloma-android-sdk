package com.palomamobile.android.sdk.verification;

import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Methods in this interface provide convenient job creation methods that provide easy access
 * to the underlying {@link IVerificationService} functionality. App developers can either use {@link BaseRetryPolicyAwareJob}
 * job instances returned by the {@code createJob...()} methods, or create custom jobs that invoke
 * methods of the {@link IVerificationService} returned by {@link IServiceManager#getService()}
 *
 * <br/>
 * To get a concrete implementation of this interface call
 * {@code ServiceSupport.Instance.getServiceManager(IVerificationManager.class)}
 */
public interface IVerificationManager extends IServiceManager<IVerificationService> {

    JobCreateEmailVerification createJobCreateEmailVerification(String emailAddress);

    JobUpdateEmailVerification createJobUpdateEmailVerification(String emailAddress, String code);

}
