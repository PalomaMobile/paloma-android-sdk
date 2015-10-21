package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messageThread members is received.
 * {@link #getSuccess()}  will return a {@link PaginatedResponse<MessageThreadMember>>} on success, or {@code null} on failure<br/>
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.<br/>
 * To request refresh of messageThread members use {@link IMessageThreadManager#createJobGetMessageThreadMembers(long)}
 * <br/>
 */
public class EventMessageThreadMembersReceived extends BaseJobEvent<JobGetMessageThreadMembers, PaginatedResponse<MessageThreadMember>> {
    public EventMessageThreadMembersReceived(JobGetMessageThreadMembers job, Throwable failure) {
        super(job, failure);
    }

    public EventMessageThreadMembersReceived(JobGetMessageThreadMembers job, PaginatedResponse<MessageThreadMember> membersReceivedPaginatedResponse) {
        super(job, membersReceivedPaginatedResponse);
    }
}
