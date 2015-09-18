package com.palomamobile.android.sdk.core.qos;

import android.util.Log;
import com.palomamobile.android.sdk.core.ServiceRequestParams;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import java.util.Map;

/**
 *
 */
public abstract class BaseRetryPolicyAwareJob<Result> extends Job {

    public static final long DEFAULT_INITIAL_BACK_OFF_MS = 10 * 1000; //10 secs

    /**
     * Create a new job with the supplied params.
     * @param params
     */
    public BaseRetryPolicyAwareJob(Params params) {
        super(params);
    }

    private Result result;

    transient OnJobAddedListener onJobAddedListener;

    protected ServiceRequestParams serviceRequestParams;

    private int maxAttempts = Integer.MAX_VALUE;

    /**
     * To implement custom actions in this method either subclass and override this method or call
     * {@link #setOnJobAddedListener(OnJobAddedListener)}. The default implementation will delegate
     * to {@link OnJobAddedListener#onAdded()}.
     */
    @Override
    public void onAdded() {
        Log.d(getClass().getSimpleName(), "onAdded(" + this + ")");
        if (onJobAddedListener != null) {
            onJobAddedListener.onAdded(this);
        }
    }

    /**
     * Get the current listener for the {@link Job#onAdded()} callback.
     * @return listener
     */
    public OnJobAddedListener getOnJobAddedListener() {
        return onJobAddedListener;
    }

    /**
     * Add a listener for the {@link Job#onAdded()} callback.
     * @param onJobAddedListener listener
     */
    public void setOnJobAddedListener(OnJobAddedListener onJobAddedListener) {
        this.onJobAddedListener = onJobAddedListener;
    }


    /**
     * Functionality implemented in {@link #shouldReRunOnThrowable(Throwable, int, int)}  where we have access to the Throwable}
     */
    @Override
    protected void onCancel() {
        //EMPTY
    }

    /**
     * Default implementation delegates to {@link DefaultRetryPolicyProvider#shouldReRunOnThrowable(BaseRetryPolicyAwareJob, Throwable, int, int)}.
     * Override to implement custom retry policy, if the returned value is {@link RetryConstraint#CANCEL} it is
     * developers responsibility to call {@link #postFailure(Throwable)} in the overridden implementation.
     * @param throwable
     * @param runCount
     * @param maxRunCount
     * @return
     */
    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        RetryConstraint retryConstraint = ServiceSupport.Instance.getRetryPolicyProvider().shouldReRunOnThrowable(this, throwable, runCount, maxRunCount);
        Log.i(getClass().getSimpleName(), "retryConstraint: " + toString(retryConstraint));
        if (!retryConstraint.shouldRetry()) {
            postFailure(throwable);
        }
        return retryConstraint;
    }

    /**
     * Implement this method to post a failure {@link IJobEvent} on the {@link de.greenrobot.event.EventBus}
     * as returned by {@link ServiceSupport#getEventBus()}
     * @param throwable cause of job failure
     */
    protected abstract void postFailure(Throwable throwable);

    /**
     * Same as calling {@link #syncRun(boolean true)}
     * @throws Throwable
     */
    @Override
    public void onRun() throws Throwable {
        try {
            result = syncRun(true);
        } catch (Throwable throwable) {
            if (getCurrentRunCount() >= getMaxAttempts()) {
                Log.w(getClass().getSimpleName(), "getCurrentRunCount() == getRetryLimit() is true: postFailure");
                postFailure(throwable);
            }
            throw  throwable;
        }
    }

    /**
     * Implement this method to do the actual work the job needs to perform. Eg. Call network, updated DB, etc.
     * @param postEvent if {@code true} the method is expected to post a {@link IJobEvent} with {@link #result}.
     * @return job result
     * @throws Throwable
     */
    public abstract Result syncRun(boolean postEvent) throws Throwable;

    /**
     * @return the same value as {@link #getMaxAttempts()}.
     */
    @Override
    protected int getRetryLimit() {
        return getMaxAttempts();
    }

    /**
     * @return maximum number of attempts to run this job, default value is {@link Integer#MAX_VALUE}
     */
    public int getMaxAttempts() {
        return maxAttempts;
    }

    /**
     * Set the maximum number of attempts to run this job, default value is {@link Integer#MAX_VALUE}
     * @param maxAttempts maximum number of attempts run this job
     */
    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Result getResult() {
        return result;
    }

    protected long getInitialBackOffInMs() {
        return DEFAULT_INITIAL_BACK_OFF_MS;
    }

    public ServiceRequestParams getServiceRequestParams() {
        return serviceRequestParams;
    }

    /**
     * Set parameters that determine how the job is executed and influence the result returned by
     * jobs execution.
     * @param serviceRequestParams
     * @return this
     */
    public BaseRetryPolicyAwareJob<Result> setServiceRequestParams(ServiceRequestParams serviceRequestParams) {
        this.serviceRequestParams = serviceRequestParams;
        return this;
    }


    protected String getFilterQuery(){
        return serviceRequestParams == null ? null : serviceRequestParams.getFilterQuery();
    }

    protected Map<String, String> getOptions() {
        return serviceRequestParams == null ? null : serviceRequestParams.getOptions();
    }

    protected String[] getSortParams() {
        return serviceRequestParams == null ? null : serviceRequestParams.getSortParams();
    }

    /**
     * Callback to take appropriate action when the {@link Job#onAdded()} method execution without
     * the need to subclass a this job to override it's {@link Job#onAdded()} method.
     * @see
     */
    public interface OnJobAddedListener<T extends BaseRetryPolicyAwareJob> {

        /**
         * Called inside the {@link Job#onAdded()}.
         * @param qosAwareJob
         */
        void onAdded(T qosAwareJob);

    }

    public static String toString(RetryConstraint constraint) {
        return "RetryConstraint{" +
                "retry=" + constraint.shouldRetry() +
                ", newDelayInMs=" + constraint.getNewDelayInMs() +
                ", newPriority=" + constraint.getNewPriority() +
                "} ";
    }

    @Override
    public String toString() {
        return "BaseRetryPolicyAwareJob{" +
                "onJobAddedListener=" + onJobAddedListener +
                ", result=" + result +
                ", serviceRequestParams=" + serviceRequestParams +
                ", maxAttempts=" + maxAttempts +
                "} " + super.toString();
    }
}
