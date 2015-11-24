package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.path.android.jobqueue.Params;

import java.util.Map;

/**
 * Convenience wrapper around {@link IMessageService#getMessagesReceived(String, long, Map, String, String...)}
 * Refresh the list of messages received by the current user.
 * Once this job is completed (with success or failure) it posts {@link EventMessagesReceived} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobGetMessagesReceived extends BaseRetryPolicyAwareJob<PaginatedResponse<MessageReceived>> {

    private long userId;

    /**
     * Create a new job to get received messages.
     */
    public JobGetMessagesReceived() {
        this(new Params(0).requireNetwork());
    }

    /**
     * Create a new job to get received messages.
     * @param params custom job params
     */
    public JobGetMessagesReceived(Params params) {
        super(params);
        this.userId = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser().getId();
    }

    @Override
    public PaginatedResponse<MessageReceived> syncRun(boolean postEvent) throws Throwable {
        MessageManager messageManager = (MessageManager) ServiceSupport.Instance.getServiceManager(IMessageManager.class);
        PaginatedResponse<MessageReceived> result = messageManager.getService().getMessagesReceived(getRetryId(), userId, getOptions(), getFilterQuery(), getSortParams());
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessagesReceived(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessagesReceived(this, throwable));
    }

}
