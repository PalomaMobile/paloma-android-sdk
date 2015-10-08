package com.palomamobile.android.sdk.user;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Created by Karel Herink
 */
public class JobGetUser extends BaseRetryPolicyAwareJob<User> {
    private final long userId;

    public JobGetUser(long userId) {
        this(new Params(0).requireNetwork().setPersistent(true), userId);
    }
    public JobGetUser(Params params, long userId) {
        super(params);
        this.userId = userId;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventLocalUserUpdated(this, throwable));
    }

    @Override
    public User syncRun(boolean postEvent) throws Throwable {
        User result = ServiceSupport.Instance.getServiceManager(IUserManager.class).getService().getUser(getRetryId(), userId);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventLocalUserUpdated(this, result));
        }
        return result;
    }

}
