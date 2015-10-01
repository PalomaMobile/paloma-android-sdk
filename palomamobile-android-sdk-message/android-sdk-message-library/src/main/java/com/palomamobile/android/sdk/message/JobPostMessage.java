package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IMessageService#postMessageSent(String, long, MessageSent)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageSentPosted} on the
 * {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobPostMessage extends BaseRetryPolicyAwareJob<MessageSent> {

    public static final String TAG = JobPostMessage.class.getSimpleName();

    private long userId;
    private MessageSent message;

    /**
     * Create a new job to post a message to friends. At least one of {@code payload} or {@code url} must be non-null.
     */
    public JobPostMessage(long userId, MessageSent message) {
        this(new Params(0).requireNetwork().setPersistent(true), userId, message);
    }

    /**
     * Create a new job to post a message to friends. At least one of {@code payload} or {@code url} must be non-null.
     * @param params custom job params
     */
    public JobPostMessage(Params params, long userId, MessageSent message) {
        super(params);
        this.userId = userId;
        this.message = message;
    }

    @Override
    public MessageSent syncRun(boolean postEvent) throws Throwable {
        MessageManager messageManager = (MessageManager) ServiceSupport.Instance.getServiceManager(IMessageManager.class);
        MessageSent result = messageManager.getService().postMessageSent(getRetryId(), userId, message);
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
