package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * when a messageThread is updated by the client.
 * {@link #getSuccess()}  will return a {@link MessageThread} on success, or {@code null} on failure<br/>
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.<br/>
 * To post a messageThread update use {@link IMessageThreadManager#createJobUpdateMessageThread(long, MessageThreadUpdate)}
 * <br/>
 */
public class EventMessageThreadUpdated extends BaseJobEvent<JobPostMessageThreadUpdate, MessageThread> {
    public EventMessageThreadUpdated(JobPostMessageThreadUpdate job, Throwable failure) {
        super(job, failure);
    }

    public EventMessageThreadUpdated(JobPostMessageThreadUpdate job, MessageThread messageThread) {
        super(job, messageThread);
    }
}
