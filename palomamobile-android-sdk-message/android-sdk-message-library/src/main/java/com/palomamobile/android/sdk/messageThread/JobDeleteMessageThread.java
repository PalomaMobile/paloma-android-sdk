package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

/**
 * Created by Karel Herink
 */
public class JobDeleteMessageThread extends BaseRetryPolicyAwareJob<Void> {

    private long messageThreadId;

    public JobDeleteMessageThread(long messageThreadId) {
        this(new Params(0).requireNetwork().setPersistent(true), messageThreadId);
    }

    public JobDeleteMessageThread(Params params, long messageThreadId) {
        super(params);
        this.messageThreadId = messageThreadId;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageThreadDeleted(this));
    }

    @Override
    public Void syncRun(boolean postEvent) throws Throwable {
        return null;
    }
}
