package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Created by Karel Herink
 */
public class EventMessageThreadReceived extends BaseJobEvent<JobGetMessageThread, MessageThread> {
    protected EventMessageThreadReceived(JobGetMessageThread job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadReceived(JobGetMessageThread job, MessageThread messageThread) {
        super(job, messageThread);
    }
}
