package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.palomamobile.android.sdk.user.User;
import com.path.android.jobqueue.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience wrapper around {@link IFriendService#addRelationship(String, long, long, RelationAttributes)}
 * used for friend requests, confirmations, user blocking etc. This enables local user to set relationship attributes between
 * themselves and another user to request or confirm friendship via {@link com.palomamobile.android.sdk.friend.RelationAttributes.Type#friend}
 * or block another user via  {@link com.palomamobile.android.sdk.friend.RelationAttributes.Type#blocked}<br/>
 * Once this job is completed (with success or failure) it posts {@link EventRelationshipUpdated} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobPostRelationship extends BaseRetryPolicyAwareJob<Relationship> {

    public static final Logger logger = LoggerFactory.getLogger(JobPostRelationship.class);
    private final Long reciprocalUserId;
    private final String reciprocalUsername;
    private final RelationAttributes relationAttributes;

    /**
     * Constructs a new job to create a relationship between the local user and a reciprocal user (eg: a friend)
     * @param params custom job parameters
     * @param reciprocalUserId id of the user on the other end of this 1:1 relationship
     * @param relationAttributes description of the relationship
     */
    public JobPostRelationship(Params params, long reciprocalUserId, RelationAttributes relationAttributes) {
        super(params);
        this.reciprocalUserId = reciprocalUserId;
        this.reciprocalUsername = null;
        this.relationAttributes = relationAttributes;
    }

    /**
     * Constructs a new job to create a relationship between the local user and a reciprocal user (eg: a friend)
     * @param params custom job parameters
     * @param reciprocalUsername username of the user on the other end of this 1:1 relationship
     * @param relationAttributes description of the relationship
     */
    public JobPostRelationship(Params params, String reciprocalUsername, RelationAttributes relationAttributes) {
        super(params);
        this.reciprocalUserId = null;
        this.reciprocalUsername = reciprocalUsername;
        this.relationAttributes = relationAttributes;
    }

    /**
     * Constructs a new job to create a relationship between the local user and a reciprocal user (eg: a friend)
     * @param reciprocalUserId id of the user on the other end of this 1:1 relationship
     * @param relationAttributes description of the relationship
     */
    public JobPostRelationship(long reciprocalUserId, RelationAttributes relationAttributes) {
        this(new Params(0).requireNetwork(), reciprocalUserId, relationAttributes);
    }

    /**
     * Constructs a new job to create a relationship between the local user and a reciprocal user (eg: a friend)
     * @param reciprocalUsername username of the user on the other end of this 1:1 relationship
     * @param relationAttributes description of the relationship
     */
    public JobPostRelationship(String reciprocalUsername, RelationAttributes relationAttributes) {
        this(new Params(0).requireNetwork(), reciprocalUsername, relationAttributes);
    }

    @Override
    public Relationship syncRun(boolean postEvent) throws Throwable {
        User user = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser();
        FriendManager friendManager = (FriendManager) ServiceSupport.Instance.getServiceManager(IFriendManager.class);
        Relationship result = null;
        if (reciprocalUsername != null) {
            result = friendManager.getService().addRelationship(getRetryId(), user.getId(), reciprocalUsername, relationAttributes);
        }
        else {
            result = friendManager.getService().addRelationship(getRetryId(), user.getId(), reciprocalUserId, relationAttributes);
        }


        logger.debug("Received relationship from server: " + result);
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
