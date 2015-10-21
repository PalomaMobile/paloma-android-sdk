package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * when the list of messages sent is refreshed.
 * The event contains either a current list of messages sent as returned by {@link #getSuccess()} on success or
 * {@code throwable} on failure.
 * To refresh a list of sent messages use {@link IMessageManager#createJobGetMessagesSent()}
 * <br/>
 */
public class EventMessagesSent extends BaseJobEvent<JobGetMessagesSent, PaginatedResponse<MessageSent>> {
    public EventMessagesSent(JobGetMessagesSent job, Throwable failure) {
        super(job, failure);
    }

    public EventMessagesSent(JobGetMessagesSent job, PaginatedResponse<MessageSent> messageSentPaginatedResponse) {
        super(job, messageSentPaginatedResponse);
    }
}
