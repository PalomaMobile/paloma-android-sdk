package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.path.android.jobqueue.Params;

import java.util.Map;

/**
 * Convenience wrapper around {@link IMessageService#getMessagesSent(String, long, Map, String, String...)}
 * Refresh the list of messages sent by the current user
 * Once this job is completed (with success or failure) it posts {@link EventMessagesSent} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobGetMessagesSent extends BaseRetryPolicyAwareJob<PaginatedResponse<MessageSent>> {

    private long userId;

    /**
     * Create a new job to get sent messages.
     */
    public JobGetMessagesSent() {
        this(new Params(0).requireNetwork());
    }

    /**
     * Create a new job to get sent messages.
     * @param params custom job params
     */
    public JobGetMessagesSent(Params params) {
        super(params);
        this.userId = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser().getId();
    }

    @Override
    public PaginatedResponse<MessageSent> syncRun(boolean postEvent) throws Throwable {
        MessageManager messageManager = (MessageManager) ServiceSupport.Instance.getServiceManager(IMessageManager.class);
        PaginatedResponse<MessageSent> messagesSentResponse = messageManager.getService().getMessagesSent(getRetryId(), userId, getOptions(), getFilterQuery(), getSortParams());
        PaginatedResponse<MessageSent> result = messagesSentResponse;
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessagesSent(this, result) );
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessagesSent(this, throwable));
    }

}
