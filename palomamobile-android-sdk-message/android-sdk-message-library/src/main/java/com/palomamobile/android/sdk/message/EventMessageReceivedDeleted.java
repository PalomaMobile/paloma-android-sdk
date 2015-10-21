package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messages received changes as a result of message deletion.
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.<br/>
 * To delete a message use {@link IMessageManager#createJobDeleteMessageReceived(long)}
 * <br/>
 *
 */
public class EventMessageReceivedDeleted extends BaseJobEvent<JobDeleteMessageReceived, Void> {
    public EventMessageReceivedDeleted(JobDeleteMessageReceived job, Throwable failure) {
        super(job, failure);
    }

    public EventMessageReceivedDeleted(JobDeleteMessageReceived job) {
        super(job, (Void) null);
    }
}
