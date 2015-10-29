package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.path.android.jobqueue.Params;

import java.util.Map;

/**
 * Convenience wrapper around {@link IMessageThreadService#getMessageThreads(String, long, Map, String, String...)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageThreadsReceived} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobGetMessageThreads extends BaseRetryPolicyAwareJob<PaginatedResponse<MessageThread>> {

    private long userId;

    /**
     * Create a new job to get list of current users messageThreads.
     */
    public JobGetMessageThreads() {
        this(new Params(0).requireNetwork());
    }

    /**
     * Create a new job to get list of users messageThreads.
     * @param params custom job params
     */
    public JobGetMessageThreads(Params params) {
        super(params);
        this.userId = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser().getId();
    }

    @Override
    public PaginatedResponse<MessageThread> syncRun(boolean postEvent) throws Throwable {
        IMessageThreadManager messageManager = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class);
        PaginatedResponse<MessageThread> result = messageManager.getService().getMessageThreads(getRetryId(), userId, getOptions(), getFilterQuery(), getSortParams());
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageThreadsReceived(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageThreadsReceived(this, throwable));
    }

}
