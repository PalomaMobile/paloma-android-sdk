package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messages received changes as a result of deletion of (potentially) multiple messages.
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.<br/>
 * To delete a message use {@link JobDeleteMessageReceived}
 * <br/>
 *
 */
public class EventMessagesReceivedDeleted extends BaseJobEvent<JobDeleteMessagesReceived, Void> {
    public EventMessagesReceivedDeleted(JobDeleteMessagesReceived job, Throwable failure) {
        super(job, failure);
    }

    public EventMessagesReceivedDeleted(JobDeleteMessagesReceived job) {
        super(job, (Void) null);
    }
}
