package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.message.MessageSent;
import com.path.android.jobqueue.Params;

import java.util.Map;

/**
 * Convenience wrapper around {@link IMessageThreadService#getMessages(String, long, Map, String, String...)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageThreadMessagesReceived} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobGetMessageThreadMessages extends BaseRetryPolicyAwareJob<PaginatedResponse<MessageSent>> {

    public static final String TAG = JobGetMessageThreadMessages.class.getSimpleName();

    private long threadId;

    /**
     * Create a new job to get members of a messageThread.
     * @param threadId
     */
    public JobGetMessageThreadMessages(long threadId) {
        this(new Params(0).requireNetwork(), threadId);
    }

    /**
     * Create a new job to get members of a messageThread.
     * @param params custom job params
     * @param threadId
     */
    public JobGetMessageThreadMessages(Params params, long threadId) {
        super(params);
        this.threadId = threadId;
    }

    @Override
    public PaginatedResponse<MessageSent> syncRun(boolean postEvent) throws Throwable {
        IMessageThreadManager messageThreadManager = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class);
        PaginatedResponse<MessageSent> result = messageThreadManager.getService().getMessages(getRetryId(), threadId, getOptions(), getFilterQuery(), getSortParams());
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageThreadMessagesReceived(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageThreadMessagesReceived(this, throwable));
    }

}
