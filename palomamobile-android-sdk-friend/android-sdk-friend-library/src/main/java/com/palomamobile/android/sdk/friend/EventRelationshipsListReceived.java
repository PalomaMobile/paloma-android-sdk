package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once a previously requested list of relationships is received. The event contains either a list of {@link Relationship}s on success or {@code throwable} on
 * failure.
 * To request a list of relationships for the current user use {@link IFriendManager#createJobGetRelationships()}
 * <br/>
 *
 */
public class EventRelationshipsListReceived extends BaseJobEvent<JobGetRelationships, PaginatedResponse<Relationship>> {
    protected EventRelationshipsListReceived(JobGetRelationships job, Throwable failure) {
        super(job, failure);
    }

    protected EventRelationshipsListReceived(JobGetRelationships job, PaginatedResponse<Relationship> relationshipPaginatedResponse) {
        super(job, relationshipPaginatedResponse);
    }
}
