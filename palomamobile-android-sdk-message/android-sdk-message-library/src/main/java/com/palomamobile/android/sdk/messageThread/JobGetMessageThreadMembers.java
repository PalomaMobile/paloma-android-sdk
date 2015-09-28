package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

import java.util.Map;

/**
 * Convenience wrapper around {@link IMessageThreadService#getMessageThreadMembers(String, long, Map, String, String...)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageThreadMembersReceived} on the
 * {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobGetMessageThreadMembers extends BaseRetryPolicyAwareJob<PaginatedResponse<MessageThreadMember>> {

    public static final String TAG = JobGetMessageThreadMembers.class.getSimpleName();

    private long threadId;

    /**
     * Create a new job to get members of a messageThread.
     * @param threadId
     */
    public JobGetMessageThreadMembers(long threadId) {
        this(new Params(0).requireNetwork(), threadId);
    }

    /**
     * Create a new job to get members of a messageThread.
     * @param params custom job params
     * @param threadId
     */
    public JobGetMessageThreadMembers(Params params, long threadId) {
        super(params);
        this.threadId = threadId;
    }

    @Override
    public PaginatedResponse<MessageThreadMember> syncRun(boolean postEvent) throws Throwable {
        IMessageThreadManager messageThreadManager = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class);
        PaginatedResponse<MessageThreadMember> result = messageThreadManager.getService().getMessageThreadMembers(getRetryId(), threadId, getOptions(), getFilterQuery(), getSortParams());
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageThreadMembersReceived(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageThreadMembersReceived(this, throwable));
    }

}
