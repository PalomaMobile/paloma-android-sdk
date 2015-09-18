package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.IJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once a update of a relationships is received. The event contains a {@link Relationship} on success or {@code throwable} on
 * failure.
 * To request a relationship creation or update for the current user use {@link IFriendManager#createJobPutRelationship(long, RelationAttributes)}
 * <br/>
 *
 */public class EventRelationshipUpdated implements IJobEvent<JobPutRelationship, Relationship> {

    private JobPutRelationship jobPutRelationship;
    private Throwable throwable;
    private Relationship relationship;

    EventRelationshipUpdated(JobPutRelationship jobPutRelationship, Throwable throwable) {
        this.jobPutRelationship = jobPutRelationship;
        this.throwable = throwable;
    }

    EventRelationshipUpdated(JobPutRelationship jobPutRelationship, Relationship relationship) {
        this.jobPutRelationship = jobPutRelationship;
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return "EventRelationshipUpdated{" +
                "jobPutRelationship=" + jobPutRelationship +
                ", throwable=" + throwable +
                ", relationship=" + relationship +
                '}';
    }

    @Override
    public JobPutRelationship getJob() {
        return jobPutRelationship;
    }

    @Override
    public Relationship getSuccess() {
        return relationship;
    }

    @Override
    public Throwable getFailure() {
        return throwable;
    }
}
