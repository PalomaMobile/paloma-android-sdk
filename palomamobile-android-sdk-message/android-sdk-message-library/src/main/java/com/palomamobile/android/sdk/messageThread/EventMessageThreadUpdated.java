package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * when a messageThread is updated by the client.
 * {@link #getSuccess()}  will return a {@link MessageThread} on success, or {@code null} on failure<br/>
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.<br/>
 * To post a messageThread update use {@link IMessageThreadManager#createJobUpdateMessageThread(long, MessageThreadUpdate)}
 * <br/>
 */
public class EventMessageThreadUpdated extends BaseJobEvent<JobUpdateMessageThread, MessageThread> {
    protected EventMessageThreadUpdated(JobUpdateMessageThread job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadUpdated(JobUpdateMessageThread job, MessageThread messageThread) {
        super(job, messageThread);
    }
}
