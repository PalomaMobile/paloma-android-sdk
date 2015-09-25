package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

import java.util.List;
import java.util.Map;

/**
 * Convenience wrapper around {@link IMessageThreadService#postMessageThread(String, NewMessageThread)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageThreadPosted} on the
 * {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobPostMessageThread extends BaseRetryPolicyAwareJob<MessageThread> {

    public static final String TAG = JobPostMessageThread.class.getSimpleName();

    protected String name;
    protected Long relatedTo;
    protected String type;
    protected Map<String, String> custom;
    private List<Long> members;


    public JobPostMessageThread(String name, Long relatedTo, String type, Map<String, String> custom, List<Long> members) {
        this(new Params(0).requireNetwork().setPersistent(true), name, relatedTo, type, custom, members);
    }

    public JobPostMessageThread(Params params, String name, Long relatedTo, String type, Map<String, String> custom, List<Long> members) {
        super(params);
        this.name = name;
        this.relatedTo = relatedTo;
        this.type = type;
        this.custom = custom;
        this.members = members;
    }

    @Override
    public MessageThread syncRun(boolean postEvent) throws Throwable {
        NewMessageThread newMessageThread = new NewMessageThread();
        newMessageThread.setName(name);
        newMessageThread.setRelatedTo(relatedTo);
        newMessageThread.setType(type);
        newMessageThread.setCustom(custom);
        newMessageThread.setMembers(members);
        IMessageThreadManager messageThreadManager = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class);
        MessageThread result = messageThreadManager.getService().postMessageThread(getRetryId(), newMessageThread);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageThreadPosted(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageThreadPosted(this, throwable));
    }

}
