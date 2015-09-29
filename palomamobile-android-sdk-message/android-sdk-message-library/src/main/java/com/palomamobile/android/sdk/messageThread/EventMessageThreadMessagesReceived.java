package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;
import com.palomamobile.android.sdk.message.MessageSent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messageThread messages is received on the client.
 * The event contains either a current list of messageThread messages returned by {@link #getSuccess()} on success or {@code throwable} on
 * failure.
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
