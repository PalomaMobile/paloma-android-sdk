package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IMessageThreadService#deleteMessageThreads(String, long)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageThreadsDeleted} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobDeleteMessageThreads extends BaseRetryPolicyAwareJob<Void> {

    private long userId;

    public JobDeleteMessageThreads() {
        this(new Params(0).requireNetwork().setPersistent(true));
    }

    public JobDeleteMessageThreads(Params params) {
        super(params);
        this.userId = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser().getId();
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageThreadsDeleted(this, throwable));
    }

    @Override
    public Void syncRun(boolean postEvent) throws Throwable {
        IMessageThreadManager messageThreadManager = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class);
        IMessageThreadService messageThreadService = messageThreadManager.getService();
        messageThreadService.deleteMessageThreads(getRetryId(), userId);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageThreadsDeleted(this));
        }
        return null;
    }
}
