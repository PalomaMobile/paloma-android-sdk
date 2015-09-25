package com.palomamobile.android.sdk.core.qos;

import android.util.Log;
import com.path.android.jobqueue.RetryConstraint;
import retrofit.RetrofitError;

/**
 * Default simple implementation of the {@link IRetryPolicyProvider}. To provide a different system wide provider
 * pass your custom implementation to {@link com.palomamobile.android.sdk.core.ServiceSupport#setRetryPolicyProvider(IRetryPolicyProvider)}.
 * Each job can also specify it's own retry policy by overriding it's {@link BaseRetryPolicyAwareJob#shouldReRunOnThrowable(Throwable, int, int)} method.
 *
 *
 */
public class DefaultRetryPolicyProvider implements IRetryPolicyProvider {

    private static final String TAG = DefaultRetryPolicyProvider.class.getSimpleName();

    /**
     * Default implementation returns:
     * <li/>exponential backoff for network exceptions with response code >= 500 (server error)
     * or with missing response code (network not reached).
     * <li/>{@link RetryConstraint#CANCEL} all other exceptions
     * @param job
     * @param throwable
     * @param runCount
     * @param maxRunCount
     * @return
     */
    @Override
    public RetryConstraint shouldReRunOnThrowable(BaseRetryPolicyAwareJob job, Throwable throwable, int runCount, int maxRunCount) {
        Log.d(TAG, "shouldReRunOnThrowable() ");
        RetryConstraint retryConstraint = RetryConstraint.CANCEL;
        if (throwable instanceof RetrofitError) {
            RetrofitError error = (RetrofitError) throwable;
            if (isTemporary(error)) {
                retryConstraint = RetryConstraint.createExponentialBackoff(runCount, job.getInitialBackOffInMs());
            }
            else {
                Log.d(TAG, "error not temporary");
            }
        }
        Log.w(TAG, job + " failed with: " + throwable + " -> " + asString(retryConstraint));
        return retryConstraint;
    }

    private boolean isTemporary(RetrofitError error) {
        if (error.getKind() == RetrofitError.Kind.NETWORK || error.getKind() == RetrofitError.Kind.HTTP) {
            if (error.getResponse() == null) {
                return true;
            }
            int status = error.getResponse().getStatus();
            if (status >= 500) //server errors should be temporary :)
                return true;
        }
        return false; //amongst others also all 4xx errors
    }

    private static String asString(RetryConstraint retryConstraint) {
        return "RetryConstraint retry: "  + retryConstraint.shouldRetry() + ", delay: "  +  retryConstraint.getNewDelayInMs() + ", priority: "  + retryConstraint.getNewPriority();
    }

}
