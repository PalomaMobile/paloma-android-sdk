package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IMessageThreadService#postMessageThread(String, NewMessageThread)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageThreadPosted} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobPostMessageThread extends BaseRetryPolicyAwareJob<MessageThread> {

    public static final String TAG = JobPostMessageThread.class.getSimpleName();

    private NewMessageThread newMessageThread;


    public JobPostMessageThread(NewMessageThread newMessageThread) {
        this(new Params(0).requireNetwork().setPersistent(true), newMessageThread);
    }

    public JobPostMessageThread(Params params, NewMessageThread newMessageThread) {
        super(params);
        this.newMessageThread = newMessageThread;
    }

    @Override
    public MessageThread syncRun(boolean postEvent) throws Throwable {
        IMessageThreadManager messageThreadManager = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class);
        MessageThread result = messageThreadManager.getService().postMessageThread(getRetryId(), newMessageThread);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageThreadPosted(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageThreadPosted(this, throwable));
    }

}
