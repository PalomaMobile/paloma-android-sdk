package com.palomamobile.android.sdk.friend;

import android.util.Log;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.palomamobile.android.sdk.user.User;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IFriendService#addRelationship(String, long, long, RelationAttributes)}
 * used for friend requests, confirmations, user blocking etc.
 * Once this job is completed (with success or failure) it posts {@link EventRelationshipUpdated} on the
 * {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobPutRelationship extends BaseRetryPolicyAwareJob<Relationship> {

    public static final String TAG = JobPutRelationship.class.getSimpleName();
    private final long reciprocalUserId;
    private final RelationAttributes relationAttributes;

    /**
     * Constructs a new job to create a relationship between the local user and a reciprocal user (eg: a friend)
     * @param params custom job parameters
     * @param reciprocalUserId id of the user on the other end of this 1:1 relationship
     * @param relationAttributes description of the relationship
     */
    public JobPutRelationship(Params params, long reciprocalUserId, RelationAttributes relationAttributes) {
        super(params);
        this.reciprocalUserId = reciprocalUserId;
        this.relationAttributes = relationAttributes;
    }

    /**
     * Constructs a new job to create a relationship between the local user and a reciprocal user (eg: a friend)
     * @param reciprocalUserId id of the user on the other end of this 1:1 relationship
     * @param relationAttributes description of the relationship
     */
    public JobPutRelationship(long reciprocalUserId, RelationAttributes relationAttributes) {
        this(new Params(0).requireNetwork(), reciprocalUserId, relationAttributes);
    }

    @Override
    public Relationship syncRun(boolean postEvent) throws Throwable {
        User user = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser();
        FriendManager friendManager = (FriendManager) ServiceSupport.Instance.getServiceManager(IFriendManager.class);
        Relationship result = friendManager.getService().addRelationship(getRetryId(), user.getId(), reciprocalUserId, relationAttributes);
        Log.d(TAG, "Received relationship from server: " + result);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventRelationshipUpdated(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventRelationshipUpdated(this, throwable));
    }
}
