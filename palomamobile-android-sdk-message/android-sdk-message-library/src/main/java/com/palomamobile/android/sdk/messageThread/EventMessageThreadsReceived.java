package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messageThreads is received on the client.
 * {@link #getSuccess()}  will return a {@link PaginatedResponse<MessageThread>>} on success, or {@code null} on failure<br/>
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.<br/>
 * To refresh a list of messageThreads use {@link IMessageThreadManager#createJobGetMessageThreads()}
 * <br/>
 *
 */
public class EventMessageThreadsReceived extends BaseJobEvent<JobGetMessageThreads, PaginatedResponse<MessageThread>> {
    protected EventMessageThreadsReceived(JobGetMessageThreads job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadsReceived(JobGetMessageThreads job, PaginatedResponse<MessageThread> messageThreadReceivedPaginatedResponse) {
        super(job, messageThreadReceivedPaginatedResponse);
    }
}
