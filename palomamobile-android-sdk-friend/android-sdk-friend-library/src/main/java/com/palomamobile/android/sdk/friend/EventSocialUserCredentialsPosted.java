package com.palomamobile.android.sdk.friend;

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
public class EventSocialUserCredentialsPosted implements IJobEvent<JobPostSocialUserCredential, Void> {
    private JobPostSocialUserCredential job;
    private Throwable throwable;

    EventSocialUserCredentialsPosted(JobPostSocialUserCredential job, Throwable throwable) {
        this.job = job;
        this.throwable = throwable;
    }

    EventSocialUserCredentialsPosted(JobPostSocialUserCredential job) {
        this.job = job;
    }

    @Override
    public JobPostSocialUserCredential getJob() {
        return job;
    }

    public Void getSuccess() {
        return null;
    }

    @Override
    public String toString() {
        return "EventSocialUserCredentialsPosted{" +
                "job=" + job +
                ", throwable=" + throwable +
                '}';
    }

    public Throwable getFailure() {
        return throwable;
    }
}
