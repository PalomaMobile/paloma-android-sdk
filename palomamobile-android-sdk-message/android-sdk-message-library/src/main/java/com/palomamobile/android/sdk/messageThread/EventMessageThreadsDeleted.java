package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once all users messageThreads are deleted.
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.<br/>
 * To delete all users messageThreads use {@link IMessageThreadManager#createJobDeleteMessageThreads()}
 * <br/>
 */
public class EventMessageThreadsDeleted extends BaseJobEvent<JobDeleteMessageThreads, Void> {

    protected EventMessageThreadsDeleted(JobDeleteMessageThreads job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadsDeleted(JobDeleteMessageThreads job) {
        super(job, (Void) null);
    }
}
