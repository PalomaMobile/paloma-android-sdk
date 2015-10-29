package com.palomamobile.android.sdk.user;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

public class JobPostUserUpdate extends BaseRetryPolicyAwareJob<User> {

    private long userId;
    private UserUpdate userUpdate;

    public JobPostUserUpdate(UserUpdate userUpdate) {
        this(new Params(0).requireNetwork().setPersistent(true), userUpdate);
    }

    public JobPostUserUpdate(Params params, UserUpdate userUpdate) {
        super(params);
        this.userId = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser().getId();
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
