package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IMessageService#postMessageSent(String, long, MessageSent)}
 * Request to send a message to a friend identified by {@code friendId} as returned by the Friend service.
 * The actual message must contain at least one of {@code payload} or {@code url} values.
 * If both {@code payload} and {@code url} are set the content found at the {@code url} doesn't have to match the value of
 * {@code payload}, especially since the content at the {@code url} can change over time.
 * If only {@code payload} is set  then service will create a url with content type "text/plain" containing the payload.
 * Once this job is completed (with success or failure) it posts {@link EventMessageSentPosted} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobPostMessage extends BaseRetryPolicyAwareJob<MessageSent> {

    private long userId;
    private MessageSent message;

    /**
     * Create a new job to post a message to friends. At least one of {@code payload} or {@code url} must be non-null.
     */
    public JobPostMessage(MessageSent message) {
        this(new Params(0).requireNetwork().setPersistent(true), message);

    }

    /**
     * Create a new job to post a message to friends. At least one of {@code payload} or {@code url} must be non-null.
     * @param params custom job params
     */
    public JobPostMessage(Params params, MessageSent message) {
        super(params);
        this.userId = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser().getId();
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
