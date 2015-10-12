package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IMessageService#deleteMessageReceived(String, long, long)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageReceivedDeleted} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobDeleteMessageReceived extends BaseRetryPolicyAwareJob<Void> {

    public static final String TAG = JobDeleteMessageReceived.class.getSimpleName();

    private long messageId;
    private long userId;

    /**
     * Create a new job to delete a received message.
     * @param userId local user id
     * @param messageId of the message to be deleted
     */
    public JobDeleteMessageReceived(Long userId, Long messageId) {
        this(new Params(0).requireNetwork(), userId, messageId);
    }

    /**
     * Create a new job to delete a received message.
     * @param params custom job parameters
     * @param userId local user id
     * @param messageId of the message to be deleted
     */
    public JobDeleteMessageReceived(Params params, Long userId, Long messageId) {
        super(params);
        this.userId = userId;
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
