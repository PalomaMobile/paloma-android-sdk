package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.IJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once a previously requested list of friends is received. The event contains either a list of {@link Friend}s on success or {@code throwable} on
 * failure.
 * To request a list of friends for the current user use {@link IFriendManager#createJobGetFriends()} ()}
 * <br/>
 *
 */
public class EventFriendsListReceived implements IJobEvent<JobGetFriends, PaginatedResponse<Friend>> {
    protected PaginatedResponse<Friend> friends;
    private JobGetFriends job;
    private Throwable throwable;

    EventFriendsListReceived(JobGetFriends job, Throwable throwable) {
        this.job = job;
        this.throwable = throwable;
    }

    EventFriendsListReceived(JobGetFriends job, PaginatedResponse<Friend> friends) {
        this.job = job;
        this.friends = friends;
    }

    @Override
    public JobGetFriends getJob() {
        return job;
    }

    public PaginatedResponse<Friend> getSuccess() {
        return friends;
    }

    @Override
    public String toString() {
        return "EventFriendsListReceived{" +
                "friends=" + friends +
                ", job=" + job +
                ", throwable=" + throwable +
                '}';
    }

    public Throwable getFailure() {
        return throwable;
    }
}
