package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messageThreads changes as a result of messageThread deletion.
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.<br/>
 * To delete a message use {@link JobDeleteMessageThread}
 * <br/>
 */
public class EventMessageThreadDeleted extends BaseJobEvent<JobDeleteMessageThread, Void> {

    public EventMessageThreadDeleted(JobDeleteMessageThread job, Throwable failure) {
        super(job, failure);
    }

    public EventMessageThreadDeleted(JobDeleteMessageThread job) {
        super(job, (Void) null);
    }
}
