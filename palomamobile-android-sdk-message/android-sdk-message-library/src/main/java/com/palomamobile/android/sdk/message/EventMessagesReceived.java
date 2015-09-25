package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messages received on the client is refreshed.
 * The event contains either a current list of messages received as returned by {@link #getSuccess()}  on success or {@code throwable} on
 * failure.
 * To refresh a list of received messages use {@link IMessageManager#createJobGetMessagesReceived()}
 * <br/>
 *
 */
public class EventMessagesReceived extends BaseJobEvent<JobGetMessagesReceived, PaginatedResponse<MessageReceived>> {
    protected EventMessagesReceived(JobGetMessagesReceived job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessagesReceived(JobGetMessagesReceived job, PaginatedResponse<MessageReceived> messageReceivedPaginatedResponse) {
        super(job, messageReceivedPaginatedResponse);
    }
}
