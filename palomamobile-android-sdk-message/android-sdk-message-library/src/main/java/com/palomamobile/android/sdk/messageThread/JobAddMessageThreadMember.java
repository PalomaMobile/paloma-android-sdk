package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IMessageThreadService#addMessageThreadMember(String, long, long)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageThreadUpdated} on the
 * {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobAddMessageThreadMember extends BaseRetryPolicyAwareJob<MessageThreadMember> {

    public static final String TAG = JobAddMessageThreadMember.class.getSimpleName();

    private long messageThreadId;
    private long userId;


    public JobAddMessageThreadMember(long messageThreadId, long userId) {
        this(new Params(0).requireNetwork().setPersistent(true), messageThreadId, userId);
    }

    public JobAddMessageThreadMember(Params params, long messageThreadId, long userId) {
        super(params);
        this.messageThreadId = messageThreadId;
        this.userId = userId;
    }

    @Override
    public MessageThreadMember syncRun(boolean postEvent) throws Throwable {
        IMessageThreadManager messageThreadManager = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class);
        MessageThreadMember result = messageThreadManager.getService().addMessageThreadMember(getRetryId(), messageThreadId, userId);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageThreadMemberAdded(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageThreadMemberAdded(this, throwable));
    }

}
