package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;
import com.palomamobile.android.sdk.message.MessageSent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messageThread messages is received on the client.
 * {@link #getSuccess()}  will return a {@link PaginatedResponse<MessageSent>>} on success, or {@code null} on failure<br/>
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.<br/>
 * To refresh a list of messageThread messages use {@link IMessageThreadManager#createJobGetMessageThreadMessages(long)}
 * <br/>
 *
 */
public class EventMessageThreadMessagesReceived extends BaseJobEvent<JobGetMessageThreadMessages, PaginatedResponse<MessageSent>> {
    protected EventMessageThreadMessagesReceived(JobGetMessageThreadMessages job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadMessagesReceived(JobGetMessageThreadMessages job, PaginatedResponse<MessageSent> messageReceivedPaginatedResponse) {
        super(job, messageReceivedPaginatedResponse);
    }
}
