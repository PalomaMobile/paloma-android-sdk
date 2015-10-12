package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once a previously requested media upload has finished.
 * The event contains either {@code mediaInfo} on success or {@code throwable} on
 * failure.
 * To request a media upload use {@link JobUploadMediaPrivate} or {@link JobUploadMediaPublic}
 * <br/>
 *
 */
public class EventMediaUploaded extends BaseJobEvent<BaseJobUploadMedia, MediaInfo> {
    protected EventMediaUploaded(BaseJobUploadMedia job, Throwable failure) {
        super(job, failure);
    }

    protected EventMediaUploaded(BaseJobUploadMedia job, MediaInfo mediaInfo) {
        super(job, mediaInfo);
    }
}
