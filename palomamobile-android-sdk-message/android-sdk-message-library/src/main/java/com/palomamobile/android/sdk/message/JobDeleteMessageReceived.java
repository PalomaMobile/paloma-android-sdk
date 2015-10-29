package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IMessageService#deleteMessageReceived(String, long, long)}
 * Delete a received message identified by {@code messageId}.
 * Once this job is completed (with success or failure) it posts {@link EventMessageReceivedDeleted} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobDeleteMessageReceived extends BaseRetryPolicyAwareJob<Void> {

    private long messageId;
    private long userId;

    /**
     * Create a new job to delete a received message.
     * @param messageId of the message to be deleted
     */
    public JobDeleteMessageReceived(Long messageId) {
        this(new Params(0).requireNetwork(), messageId);
    }

    /**
     * Create a new job to delete a received message.
     * @param params custom job parameters
     * @param messageId of the message to be deleted
     */
    public JobDeleteMessageReceived(Params params, Long messageId) {
        super(params);
        this.userId = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser().getId();
        this.messageId = messageId;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public Void syncRun(boolean postEvent) throws Throwable {
        IMessageManager messageManager = ServiceSupport.Instance.getServiceManager(IMessageManager.class);
        IMessageService messageService = messageManager.getService();
        messageService.deleteMessageReceived(getRetryId(), userId, messageId);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageReceivedDeleted(this));
        }
        return null;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageReceivedDeleted(this, throwable));
    }

}
