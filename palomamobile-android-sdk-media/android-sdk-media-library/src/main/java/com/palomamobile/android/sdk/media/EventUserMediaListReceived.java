package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messages received on the client is refreshed.
 * The event contains a list of users private media as returned by {@link #getSuccess()} on success or {@code throwable} on
 * failure.
 * To refresh a list of users private media use {@link JobListUserMedia}
 * <br/>
 */
public class EventUserMediaListReceived extends BaseJobEvent<JobListUserMedia, PaginatedResponse<MediaInfo>> {

    protected EventUserMediaListReceived(JobListUserMedia job, Throwable failure) {
        super(job, failure);
    }

    protected EventUserMediaListReceived(JobListUserMedia job, PaginatedResponse<MediaInfo> mediaInfoPaginatedResponse) {
        super(job, mediaInfoPaginatedResponse);
    }
}
