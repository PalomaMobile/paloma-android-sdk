package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.palomamobile.android.sdk.user.User;
import com.path.android.jobqueue.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Convenience wrapper around {@link IFriendService#postSocialUserCredentials(String, long, SocialUserCredential, Map)}
 * request to post 3rd party service credentials to the Friend service for the purposes of friend discovery and friend matching.
 * Once this job is completed (with success or failure) it posts {@link EventSocialUserCredentialsPosted} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobPostSocialUserCredential extends BaseRetryPolicyAwareJob<Void> {

    public static final Logger logger = LoggerFactory.getLogger(JobPostSocialUserCredential.class);
    private SocialUserCredential socialUserCredential;

    /**
     * Create a new job
     * @param params custom job parameters
     * @param socialUserCredential supported 3rd party credential to be sent to friend service for friend matching etc.
     */
    public JobPostSocialUserCredential(Params params, SocialUserCredential socialUserCredential) {
        super(params);
        this.socialUserCredential = socialUserCredential;
    }

    /**
     * Create a new job
     * @param socialUserCredential to be sent to friend service for matching
     */
    public JobPostSocialUserCredential(SocialUserCredential socialUserCredential) {
        this(new Params(0).requireNetwork(), socialUserCredential);
    }

    @Override
    public Void syncRun(boolean postEvent) throws Throwable {
        User user = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser();
        FriendManager friendManager = (FriendManager) ServiceSupport.Instance.getServiceManager(IFriendManager.class);
        logger.debug("Posting: " + socialUserCredential);
        friendManager.getService().postSocialUserCredentials(getRetryId(), user.getId(), socialUserCredential, null);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventSocialUserCredentialsPosted(this));
        }
        return null;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventSocialUserCredentialsPosted(this, throwable));
    }
}
