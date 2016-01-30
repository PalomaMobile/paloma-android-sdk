package com.palomamobile.android.sdk.core.qos;

import com.palomamobile.android.sdk.core.ServiceRequestParams;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Base class extended by jobs in the SDK, provides retry facilities.
 */
public abstract class BaseRetryPolicyAwareJob<Result> extends Job {

    public static final long DEFAULT_INITIAL_BACK_OFF_MS = 10 * 1000; //10 secs

    private static transient String sessionId = UUID.randomUUID().toString();
    private static transient AtomicLong sessionJobCounter = new AtomicLong(-1);

    private String retryId;

    public static final Logger logger = LoggerFactory.getLogger(BaseRetryPolicyAwareJob.class);

    /**
     * Create a new job with the supplied params.
     * @param params
     */
    public BaseRetryPolicyAwareJob(Params params) {
        super(params);
        retryId = sessionId + ":" + sessionJobCounter.incrementAndGet();
        logger.trace("Create new job with retryId: " + retryId);
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
        logger.debug("onAdded(" + this + ")");
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
        logger.info("retryConstraint: " + toString(retryConstraint));
        if (!retryConstraint.shouldRetry()) {
            postFailure(throwable);
        }
        return retryConstraint;
    }

    /**
     * Implement this method to post a failure {@link IJobEvent} on the {@link com.palomamobile.android.sdk.core.IEventBus}
     * as returned by {@link ServiceSupport#getEventBus()}
     * @param throwable cause of job failure
     */
    protected abstract void postFailure(Throwable throwable);

    /**
     * Calls {@link #syncRun(boolean true)}, determines if this job needs to call {@link #postFailure(Throwable)} if exception is thrown.
     * @throws Throwable
     */
    @Override
    public void onRun() throws Throwable {
        try {
            result = syncRun(true);
        } catch (Throwable throwable) {
            if (getCurrentRunCount() >= getMaxAttempts()) {
                logger.warn("getCurrentRunCount() == getRetryLimit() is true: postFailure");
                postFailure(throwable);
            }
            throw throwable;
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
     * Same as calling {@link #syncRun(boolean)} with parameter {@code postEvent} set to {@code false}
     * @return job result
     * @throws Throwable
     */
    public Result syncRun() throws Throwable {
        return syncRun(false);
    }

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

    /**
     * Retry id identifies each individual job to the backend service, this id remains the same even if the client makes
     * multiple network calls for the purpose of re-tries.
     *
     * @return unique persistent job id that remains constant for each individual job
     */
    public String getRetryId() {
        return retryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseRetryPolicyAwareJob<?> that = (BaseRetryPolicyAwareJob<?>) o;

        if (maxAttempts != that.maxAttempts) return false;
        if (retryId != null ? !retryId.equals(that.retryId) : that.retryId != null) return false;
        if (result != null ? !result.equals(that.result) : that.result != null) return false;
        return !(serviceRequestParams != null ? !serviceRequestParams.equals(that.serviceRequestParams) : that.serviceRequestParams != null);

    }

    @Override
    public int hashCode() {
        int result1 = retryId != null ? retryId.hashCode() : 0;
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (serviceRequestParams != null ? serviceRequestParams.hashCode() : 0);
        result1 = 31 * result1 + maxAttempts;
        return result1;
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
                ", retryId=" + retryId +
                "} " + super.toString();
    }

    public static boolean isResponseCodeSuccess(int code) {
        return code >= 200 && code < 300;
    }

    /**
     * This implementation only deals with @link retrofit.RetrofitError
     *
     * @param throwable
     * @return
     */
    public boolean isExceptionTemporary(Throwable throwable) {
        if (throwable instanceof RetrofitError) {
            RetrofitError error = (RetrofitError) throwable;
            if (error.getKind() == RetrofitError.Kind.NETWORK || error.getKind() == RetrofitError.Kind.HTTP) {
                if (error.getResponse() == null) {
                    return true;
                }
                int status = error.getResponse().getStatus();
                if (status >= 500) //server errors should be temporary :)
                    return true;
            }
        }
        return false; //amongst others also all 4xx errors
    }

}
