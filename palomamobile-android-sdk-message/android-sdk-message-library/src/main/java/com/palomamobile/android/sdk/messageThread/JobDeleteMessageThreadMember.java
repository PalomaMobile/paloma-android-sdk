package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IMessageThreadService#deleteMessageThreadMember(String, long, long)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageThreadMemberDeleted} on the
 * {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobDeleteMessageThreadMember extends BaseRetryPolicyAwareJob<Void> {

    private long messageThreadId;
    private long userId;

    public JobDeleteMessageThreadMember(long messageThreadId, long userId) {
        this(new Params(0).requireNetwork().setPersistent(true), messageThreadId, userId);
    }

    public JobDeleteMessageThreadMember(Params params, long messageThreadId, long userId) {
        super(params);
        this.messageThreadId = messageThreadId;
        this.userId = userId;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageThreadMemberDeleted(this, throwable));
    }

    @Override
    public Void syncRun(boolean postEvent) throws Throwable {
        IMessageThreadManager messageThreadManager = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class);
        IMessageThreadService messageThreadService = messageThreadManager.getService();
        messageThreadService.deleteMessageThreadMember(getRetryId(), messageThreadId, userId);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageThreadMemberDeleted(this));
        }
        return null;
    }
}
