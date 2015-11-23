package com.palomamobile.android.sdk.user;

import com.palomamobile.android.sdk.auth.IUserCredential;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience wrapper around {@link IUserService#validateCredentials(String, IUserCredential)}
 * used to clear local caches and then attempt to login an existing user user. If the credentials do not match an existing user a new user will not be created,
 * instead the job will fail. Once this job is completed (with success or failure) it posts {@link EventLocalUserUpdated} on the
 * {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobLoginUser extends BaseRetryPolicyAwareJob<User> {

    public static final Logger logger = LoggerFactory.getLogger(JobLoginUser.class);

    private IUserCredential userCredential;

    /**
     * Create a new job to to login an existing user user. If the credentials do not match an existing user a new user will not be created,
     * instead the job will fail.
     * @param userCredential login with these credentials
     */
    public JobLoginUser(IUserCredential userCredential) {
        //do NOT set .requireNetwork() - this will make the job fail quickly rather than wait for network to become available
        this(new Params(0), userCredential);
    }

    /**
     * Create a new job to to login an existing user user. If the credentials do not match an existing user a new user will not be created,
     * instead the job will fail.
     * @param params custom job params
     * @param userCredential login with these credentials
     */
    public JobLoginUser(Params params, IUserCredential userCredential) {
        super(params);
        setMaxAttempts(2);
        this.userCredential = userCredential;
        ((UserManager) ServiceSupport.Instance.getServiceManager(IUserManager.class)).setUserCredential(userCredential);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JobLoginUser{");
        sb.append("userCredential=").append(userCredential);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public User syncRun(boolean postEvent) throws Throwable {
        ServiceSupport.Instance.getCache().clear();

        logger.debug("about to login as: " + userCredential);
        final UserManager userManager = (UserManager) ServiceSupport.Instance.getServiceManager(IUserManager.class);
        //IUserService.validateCredentials() will never create a user it will return either 200 (with user in body) if user found or 404 fail
        User result = userManager.getService().validateCredentials(getRetryId(), userCredential);
        userManager.updateLocalUser(result);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventLocalUserUpdated(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventLocalUserUpdated(this, throwable));
    }

}
