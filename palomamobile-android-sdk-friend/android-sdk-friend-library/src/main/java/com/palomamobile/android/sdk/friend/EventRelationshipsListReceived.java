package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.IJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once a previously requested list of relationships is received. The event contains either a list of {@link Relationship}s on success or {@code throwable} on
 * failure.
 * To request a list of relationships for the current user use {@link IFriendManager#createJobGetRelationships()}
 * <br/>
 *
 */
public class EventRelationshipsListReceived implements IJobEvent<JobGetRelationships, PaginatedResponse<Relationship>> {
    private JobGetRelationships job;
    protected PaginatedResponse<Relationship> relationships;
    private Throwable throwable;

    EventRelationshipsListReceived(JobGetRelationships job, Throwable throwable) {
        this.job = job;
        this.throwable = throwable;
    }

    EventRelationshipsListReceived(JobGetRelationships job, PaginatedResponse<Relationship> relationships) {
        this.job = job;
        this.relationships = relationships;
    }

    @Override
    public JobGetRelationships getJob() {
        return job;
    }

    public PaginatedResponse<Relationship> getSuccess() {
        return relationships;
    }

    @Override
    public String toString() {
        return "EventRelationshipsListReceived{" +
                "job=" + job +
                ", relationships=" + relationships +
                ", throwable=" + throwable +
                '}';
    }

    public Throwable getFailure() {
        return throwable;
    }
}
