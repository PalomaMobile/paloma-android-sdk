package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 *
 */
public class JobDeleteMessageThread extends BaseRetryPolicyAwareJob<Void> {

    private long messageThreadId;

    public JobDeleteMessageThread(long messageThreadId) {
        this(new Params(0).requireNetwork().setPersistent(true), messageThreadId);
    }

    public JobDeleteMessageThread(Params params, long messageThreadId) {
        super(params);
        this.messageThreadId = messageThreadId;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageThreadDeleted(this, throwable));
    }

    @Override
    public Void syncRun(boolean postEvent) throws Throwable {
        IMessageThreadManager messageThreadManager = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class);
        IMessageThreadService messageThreadService = messageThreadManager.getService();
        messageThreadService.deleteMessageThread(getRetryId(), messageThreadId);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageThreadDeleted(this));
        }
        return null;
    }
}
