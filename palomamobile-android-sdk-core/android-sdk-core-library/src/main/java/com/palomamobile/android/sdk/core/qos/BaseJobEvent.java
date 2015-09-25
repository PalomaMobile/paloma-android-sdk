package com.palomamobile.android.sdk.core.qos;

/**
 * Created by Karel Herink
 */
public abstract class BaseJobEvent<Job extends BaseRetryPolicyAwareJob, Result> implements IJobEvent<Job, Result> {
    private Job job;
    private Result result;
    private Throwable failure;

    protected BaseJobEvent(Job job,  Result result) {
        this.job = job;
        this.result = result;
    }

    protected BaseJobEvent(Job job, Throwable failure) {
        this.job = job;
        this.failure = failure;
    }

    @Override
    public Job getJob() {
        return job;
    }

    @Override
    public Result getSuccess() {
        return result;
    }

    @Override
    public Throwable getFailure() {
        return failure;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "failure=" + failure +
                ", job=" + job +
                ", result=" + result +
                '}';
    }
}
