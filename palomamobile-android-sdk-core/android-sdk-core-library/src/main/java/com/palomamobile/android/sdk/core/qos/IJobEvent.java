package com.palomamobile.android.sdk.core.qos;

import com.palomamobile.android.sdk.core.IEvent;
import com.palomamobile.android.sdk.core.IServiceSupport;

/**
 * JobEvent is posted on the {@link de.greenrobot.event.EventBus} returned by {@link IServiceSupport#getEventBus()}
 * in order to notify interested listeners
 * of changes in SDK state or data availability as a direct result of executing a {@link BaseRetryPolicyAwareJob}
 *
 */
public interface IJobEvent<Job extends BaseRetryPolicyAwareJob, Result> extends IEvent<Result> {

    /**
     * @return Job that caused this event to be generated or {@code nulle} if this Event isn't a result of a job.
     */
    Job getJob();

}
