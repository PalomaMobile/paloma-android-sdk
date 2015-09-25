package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once a update of a relationships is received. The event contains a {@link Relationship} on success or {@code throwable} on
 * failure.
 * To request a relationship creation or update for the current user use {@link IFriendManager#createJobPutRelationship(long, RelationAttributes)}
 * <br/>
 *
 */
public class EventRelationshipUpdated extends BaseJobEvent<JobPutRelationship, Relationship> {

    protected EventRelationshipUpdated(JobPutRelationship job, Relationship relationship) {
        super(job, relationship);
    }

    protected EventRelationshipUpdated(JobPutRelationship job, Throwable failure) {
        super(job, failure);
    }
}
