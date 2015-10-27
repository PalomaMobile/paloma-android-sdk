package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.palomamobile.android.sdk.user.User;
import com.path.android.jobqueue.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Convenience wrapper around {@link IFriendService#getFriends(String, long, Map)}, request to refresh current users list of friends.
 * Once this job is completed (with success or failure) it posts {@link EventFriendsListReceived} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobGetFriends extends BaseRetryPolicyAwareJob<PaginatedResponse<Friend>> {

    public static final Logger logger = LoggerFactory.getLogger(JobGetFriends.class);

    /**
     * Create a new job
     * @param params custom job parameters
     */
    public JobGetFriends(Params params) {
        super(params);
    }

    /**
     * Create a new job
     */
    public JobGetFriends() {
        this(new Params(0).requireNetwork());
    }

    @Override
    public PaginatedResponse<Friend> syncRun(boolean postEvent) throws Throwable {
        User user = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser();

        FriendManager friendManager = (FriendManager) ServiceSupport.Instance.getServiceManager(IFriendManager.class);
        PaginatedResponse<Friend> result = friendManager.getService().getFriends(getRetryId(), user.getId(), null);
        logger.debug("Received list of friends from server: " + result);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventFriendsListReceived(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventFriendsListReceived(this, throwable));
    }

}
