package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once a previously requested list of relationships is received. The event contains either a list of {@link Relationship}s on success or {@code throwable} on
 * failure.
 * To request a list of relationships for the current user use {@link JobGetRelationships}
 * <br/>
 *
 */
public class EventRelationshipsListReceived extends BaseJobEvent<JobGetRelationships, PaginatedResponse<Relationship>> {
    public EventRelationshipsListReceived(JobGetRelationships job, Throwable failure) {
        super(job, failure);
    }

    public EventRelationshipsListReceived(JobGetRelationships job, PaginatedResponse<Relationship> relationshipPaginatedResponse) {
        super(job, relationshipPaginatedResponse);
    }
}
