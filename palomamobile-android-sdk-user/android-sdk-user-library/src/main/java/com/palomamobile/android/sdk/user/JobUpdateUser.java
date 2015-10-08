package com.palomamobile.android.sdk.user;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

public class JobUpdateUser extends BaseRetryPolicyAwareJob<User> {

    private final long userId;
    private final UserUpdate userUpdate;

    public JobUpdateUser(long userId, UserUpdate userUpdate) {
        this(new Params(0).requireNetwork().setPersistent(true), userId, userUpdate);
    }
    public JobUpdateUser(Params params, long userId, UserUpdate userUpdate) {
        super(params);
        this.userId = userId;
        this.userUpdate = userUpdate;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventLocalUserUpdated(this, throwable));
    }

    @Override
    public User syncRun(boolean postEvent) throws Throwable {
        User result = ServiceSupport.Instance.getServiceManager(IUserManager.class).getService().updateUser(getRetryId(), userId, userUpdate);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventLocalUserUpdated(this, result));
        }
        return result;
    }
}
