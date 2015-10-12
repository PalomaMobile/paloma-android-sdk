package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * when an existing messageThread is received by the client.
 * {@link #getSuccess()}  will return a {@link MessageThread} on success, or {@code null} on failure<br/>
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.<br/>
 * To get an existing messageThread by it id use {@link IMessageThreadManager#createJobGetMessageThread(long)}
 * <br/>
 */
public class EventMessageThreadReceived extends BaseJobEvent<JobGetMessageThread, MessageThread> {
    protected EventMessageThreadReceived(JobGetMessageThread job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadReceived(JobGetMessageThread job, MessageThread messageThread) {
        super(job, messageThread);
    }
}
