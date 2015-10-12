package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once a previously requested list of friends is received. The event contains either a list of {@link Friend}s on success or {@code throwable} on
 * failure.
 * To request a list of friends for the current user use {@link IFriendManager#createJobGetFriends()} ()}
 * <br/>
 *
 */
public class EventFriendsListReceived extends BaseJobEvent<JobGetFriends, PaginatedResponse<Friend>> {
    public EventFriendsListReceived(JobGetFriends job, Throwable failure) {
        super(job, failure);
    }

    public EventFriendsListReceived(JobGetFriends job, PaginatedResponse<Friend> friendPaginatedResponse) {
        super(job, friendPaginatedResponse);
    }
}
