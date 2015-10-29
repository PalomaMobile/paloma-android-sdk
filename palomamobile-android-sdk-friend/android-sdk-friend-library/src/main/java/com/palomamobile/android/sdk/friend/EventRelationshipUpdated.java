package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once a update of a relationships is received. The event contains a {@link Relationship} on success or {@code throwable} on
 * failure.
 * To request a relationship creation or update for the current user use {@link JobPostRelationship}
 * <br/>
 *
 */
public class EventRelationshipUpdated extends BaseJobEvent<JobPostRelationship, Relationship> {

    public EventRelationshipUpdated(JobPostRelationship job, Relationship relationship) {
        super(job, relationship);
    }

    public EventRelationshipUpdated(JobPostRelationship job, Throwable failure) {
        super(job, failure);
    }
}
