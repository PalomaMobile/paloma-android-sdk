package com.palomamobile.android.sdk.user;

import com.palomamobile.android.sdk.auth.IUserCredential;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;

/**
 * Convenience wrapper around {@link IUserService#registerUser(String, IUserCredential)}
 * used to clear local caches and then attempt to register a new user user. If the credentials match an existing user a new user will not be created,
 * instead the existing user will be returned.
 * Once this job is completed (with success or failure) it posts {@link EventLocalUserUpdated} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobRegisterUser extends BaseRetryPolicyAwareJob<User> {

    public static final Logger logger = LoggerFactory.getLogger(JobRegisterUser.class);

    private IUserCredential userCredential;

    /**
     * Create a new job to register a new user. If the credentials match an existing user a new user will not be created,
     * instead the existing user will be returned.
     * @param userCredential register with these credentials
     */
    public JobRegisterUser(IUserCredential userCredential) {
        //do NOT set .requireNetwork() - this will make the job fail quickly rather than wait if network not available
        this(new Params(0), userCredential);
    }

    /**
     * Create a new job to register a new user. If the credentials match an existing user a new user will not be created,
     * instead the existing user will be returned.
     * @param params custom job params
     * @param userCredential register with these credentials
     */
    public JobRegisterUser(Params params, IUserCredential userCredential) {
        super(params);
        setMaxAttempts(2);
        this.userCredential = userCredential;
        ((UserManager) ServiceSupport.Instance.getServiceManager(IUserManager.class)).setUserCredential(userCredential);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JobRegisterUser{");
        sb.append("userCredential=").append(userCredential);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public User syncRun(boolean postEvent) throws Throwable {
        ServiceSupport.Instance.getCache().clear();

        logger.debug("about to register as: " + userCredential);
        User result;
        final UserManager userManager = (UserManager) ServiceSupport.Instance.getServiceManager(IUserManager.class);
        try {
            result = userManager.getService().registerUser(getRetryId(), userCredential);
        } catch (RetrofitError error) {
            //HTTP STATUS 303: See Other (since HTTP/1.1)
            if (error.getResponse() != null && error.getResponse().getStatus() == 303) {
                logger.debug("user exists, but we gave the correct credentials so this is fine and we're basically doing a login");
                result = (User) error.getBodyAs(User.class);
            }
            else {
                throw error;
            }
        }
        logger.debug("Received a registered user from server: " + result);
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
