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
 * Convenience wrapper around {@link IFriendService#getRelationships(String, long, Map)} to retrieve a list of local users relationships.
 * Once this job is completed (with success or failure) it posts {@link EventRelationshipsListReceived} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobGetRelationships extends BaseRetryPolicyAwareJob<PaginatedResponse<Relationship>> {

    public static final Logger logger = LoggerFactory.getLogger(JobGetRelationships.class);

    /**
     * Create a new job.
     * @param params custom job parameters
     */
    public JobGetRelationships(Params params) {
        super(params);
    }

    /**
     * Create a new job.
     */
    public JobGetRelationships() {
        this(new Params(0).requireNetwork());
    }

    @Override
    public PaginatedResponse<Relationship> syncRun(boolean postEvent) throws Throwable {
        User user = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser();
        FriendManager friendManager = (FriendManager) ServiceSupport.Instance.getServiceManager(IFriendManager.class);
        PaginatedResponse<Relationship> result = friendManager.getService().getRelationships(getRetryId(), user.getId(), null);
        logger.debug("Received list of relationship from server: " + result);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventRelationshipsListReceived(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventRelationshipsListReceived(this, throwable));
    }
}
