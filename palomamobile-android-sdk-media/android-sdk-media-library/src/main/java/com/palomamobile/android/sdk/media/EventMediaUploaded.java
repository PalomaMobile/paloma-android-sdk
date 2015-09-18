package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.IJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once a previously requested media upload has finished.
 * The event contains either {@code mediaInfo} on success or {@code throwable} on
 * failure.
 * To request a media upload use {@link JobUploadMediaPrivate} or {@link JobUploadMediaPublic}
 * <br/>
 *
 */
public class EventMediaUploaded implements IJobEvent<BaseJobUploadMedia, MediaInfo> {

    private BaseJobUploadMedia job;
    private MediaInfo mediaInfo;
    private Throwable throwable;

    EventMediaUploaded(BaseJobUploadMedia job, MediaInfo mediaInfo) {
        this.job = job;
        this.mediaInfo = mediaInfo;
    }

    EventMediaUploaded(BaseJobUploadMedia job, Throwable throwable) {
        this.job = job;
        this.throwable = throwable;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventMediaUploaded{");
        sb.append("mediaInfo=").append(mediaInfo);
        sb.append(", throwable=").append(throwable);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public BaseJobUploadMedia getJob() {
        return job;
    }

    @Override
    public MediaInfo getSuccess() {
        return mediaInfo;
    }

    @Override
    public Throwable getFailure() {
        return throwable;
    }
}
