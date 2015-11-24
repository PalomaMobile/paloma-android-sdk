package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.path.android.jobqueue.Params;

import java.util.Map;

/**
 * Convenience wrapper around {@link IMediaService#listUserMedia(String, long, Map, String, String...)}
 * Refresh the list of users private media.
 * Once this job is completed (with success or failure) it posts {@link EventUserMediaListReceived} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobListUserMedia extends BaseRetryPolicyAwareJob<PaginatedResponse<MediaInfo>> {

    private long userId;

    /**
     * Create a new job to get a list of users private media.
     */
    public JobListUserMedia() {
        this(new Params(0).requireNetwork());
    }

    /**
     * Create a new job to get a list of users private media.
     * @param params custom job params
     */
    public JobListUserMedia(Params params) {
        super(params);
        this.userId = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser().getId();
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventUserMediaListReceived(this, throwable));
    }

    @Override
    public PaginatedResponse<MediaInfo> syncRun(boolean postEvent) throws Throwable {
        IMediaManager mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);
        PaginatedResponse<MediaInfo> result = mediaManager.getService().listUserMedia(getRetryId(), userId, getOptions(), getFilterQuery(), getSortParams());
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventUserMediaListReceived(this, result));
        }
        return result;
    }
}
