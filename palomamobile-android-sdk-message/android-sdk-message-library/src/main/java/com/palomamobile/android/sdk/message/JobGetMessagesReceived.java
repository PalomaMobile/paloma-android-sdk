package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

import java.util.Map;

/**
 * Convenience wrapper around {@link IMessageService#getMessagesReceived(long, Map, String, String...)}
 * Once this job is completed (with success or failure) it posts {@link EventMessagesReceived} on the
 * {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobGetMessagesReceived extends BaseRetryPolicyAwareJob<PaginatedResponse<MessageReceived>> {

    public static final String TAG = JobGetMessagesReceived.class.getSimpleName();

    private long userId;

    /**
     * Create a new job to get received messages.
     * @param userId local user id
     */
    public JobGetMessagesReceived(long userId) {
        this(new Params(0).requireNetwork(), userId);
    }

    /**
     * Create a new job to get received messages.
     * @param params custom job params
     * @param userId local user id
     */
    public JobGetMessagesReceived(Params params, long userId) {
        super(params);
        this.userId = userId;
    }

    @Override
    public PaginatedResponse<MessageReceived> syncRun(boolean postEvent) throws Throwable {
        MessageManager messageManager = (MessageManager) ServiceSupport.Instance.getServiceManager(IMessageManager.class);
        PaginatedResponse<MessageReceived> messagesReceivedResponse = messageManager.getService().getMessagesReceived(userId, getOptions(), getFilterQuery(), getSortParams());
        PaginatedResponse<MessageReceived> result = messagesReceivedResponse;
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessagesReceived(this, result) );
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessagesReceived(this, throwable));
    }

}
