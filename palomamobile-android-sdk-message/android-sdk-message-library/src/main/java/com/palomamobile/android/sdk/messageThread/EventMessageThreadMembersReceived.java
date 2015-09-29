package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messageThread members is received on the client.
 * The event contains either a current list of messageThread members returned by {@link #getSuccess()} on success or {@code throwable} on
 * failure.
 * To refresh a list of messageThread members use {@link IMessageThreadManager#createJobGetMessageThreadMembers(long)}
 * <br/>
 *
 */
public class EventMessageThreadMembersReceived extends BaseJobEvent<JobGetMessageThreadMembers, PaginatedResponse<MessageThreadMember>> {
    protected EventMessageThreadMembersReceived(JobGetMessageThreadMembers job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadMembersReceived(JobGetMessageThreadMembers job, PaginatedResponse<MessageThreadMember> membersReceivedPaginatedResponse) {
        super(job, membersReceivedPaginatedResponse);
    }
}
