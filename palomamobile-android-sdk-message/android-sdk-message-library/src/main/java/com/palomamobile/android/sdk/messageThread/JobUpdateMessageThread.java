package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IMessageThreadService#updateMessageThread(String, long, MessageThreadUpdate)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageThreadUpdated} on the
 * {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobUpdateMessageThread extends BaseRetryPolicyAwareJob<MessageThread> {

    public static final String TAG = JobUpdateMessageThread.class.getSimpleName();

    private long messageThreadId;
    private final MessageThreadUpdate update;


    public JobUpdateMessageThread(long messageThreadId, MessageThreadUpdate update) {
        this(new Params(0).requireNetwork().setPersistent(true), messageThreadId, update);
    }

    public JobUpdateMessageThread(Params params, long messageThreadId, MessageThreadUpdate update) {
        super(params);
        this.messageThreadId = messageThreadId;
        this.update = update;
    }

    @Override
    public MessageThread syncRun(boolean postEvent) throws Throwable {
        IMessageThreadManager messageThreadManager = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class);
        MessageThread result = messageThreadManager.getService().updateMessageThread(getRetryId(), messageThreadId, update);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageThreadUpdated(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageThreadUpdated(this, throwable));
    }

}
