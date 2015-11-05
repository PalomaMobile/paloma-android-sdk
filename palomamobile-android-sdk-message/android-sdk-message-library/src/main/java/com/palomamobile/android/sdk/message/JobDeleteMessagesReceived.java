package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.path.android.jobqueue.Params;

/**
 * Convenience wrapper around {@link IMessageService#deleteMessagesReceived(String, long, String)}
 * Delete a received message identified by {@code messageId}.
 * Once this job is completed (with success or failure) it posts {@link EventMessagesReceivedDeleted} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobDeleteMessagesReceived extends BaseRetryPolicyAwareJob<Void> {

    private long userId;

    /**
     * Create a new job to delete received messages, or their subset based on a filter.
     */
    public JobDeleteMessagesReceived() {
        this(new Params(0).requireNetwork());
    }

    /**
     * Create a new job to delete received messages, or their subset based on a filter.
     * @param params custom job parameters
     */
    public JobDeleteMessagesReceived(Params params) {
        super(params);
        this.userId = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser().getId();
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public Void syncRun(boolean postEvent) throws Throwable {
        IMessageManager messageManager = ServiceSupport.Instance.getServiceManager(IMessageManager.class);
        IMessageService messageService = messageManager.getService();
        messageService.deleteMessagesReceived(getRetryId(), userId, getFilterQuery());
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessagesReceivedDeleted(this));
        }
        return null;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessagesReceivedDeleted(this, throwable));
    }

}
