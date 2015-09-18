package com.palomamobile.android.sdk.core.qos;

import com.path.android.jobqueue.RetryConstraint;

/**
 * Provides centralized control over job retry policy.
 * <br/>
 *
 */
public interface IRetryPolicyProvider {

    /**
     * Provides a centralized control over job retry policy. This can be overridden in each job's
     * {@link BaseRetryPolicyAwareJob#shouldReRunOnThrowable(Throwable, int, int)} method.
     * @param job
     * @param throwable
     * @param runCount
     * @param maxRunCount
     * @return
     */
    RetryConstraint shouldReRunOnThrowable(BaseRetryPolicyAwareJob job, Throwable throwable, int runCount, int maxRunCount);


}
