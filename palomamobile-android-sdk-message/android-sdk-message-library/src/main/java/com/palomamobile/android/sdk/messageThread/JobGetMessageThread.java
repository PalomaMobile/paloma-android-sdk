package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Created by Karel Herink
 */
public class JobGetMessageThread extends BaseRetryPolicyAwareJob<MessageThread> {

    private long messageThreadId;

    public JobGetMessageThread(long messageThreadId) {
        this(new Params(0).requireNetwork().persist(), messageThreadId);
    }

    public JobGetMessageThread(Params params, long messageThreadId) {
        super(params);
        this.messageThreadId = messageThreadId;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageThreadReceived(this, throwable));
    }

    @Override
    public MessageThread syncRun(boolean postEvent) throws Throwable {
        MessageThread result = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class).getService().getMessageThread(getRetryId(), messageThreadId);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageThreadReceived(this, result));
        }
        return result;
    }
}
