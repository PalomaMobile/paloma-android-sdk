package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.message.EventMessageSentPosted;
import com.palomamobile.android.sdk.message.MessageSent;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IMessageThreadService#postMessage(String, long, MessageSent)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageSentPosted} on the
 * {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobPostMessageThreadMessage extends BaseRetryPolicyAwareJob<MessageSent> {

    public static final String TAG = JobPostMessageThreadMessage.class.getSimpleName();

    private long threadId;
    private MessageSent newMessage;


    public JobPostMessageThreadMessage(long threadId, MessageSent newMessage) {
        this(new Params(0).requireNetwork().setPersistent(true), threadId, newMessage);
    }

    public JobPostMessageThreadMessage(Params params, long threadId, MessageSent newMessage) {
        super(params);
        this.threadId = threadId;
        this.newMessage = newMessage;
    }

    @Override
    public MessageSent syncRun(boolean postEvent) throws Throwable {
        IMessageThreadManager messageThreadManager = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class);
        MessageSent result = messageThreadManager.getService().postMessage(getRetryId(), threadId, newMessage);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageSentPosted(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageSentPosted(this, throwable));
    }

}
