package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Created by Karel Herink
 */
public class EventMessageThreadUpdated extends BaseJobEvent<JobUpdateMessageThread, MessageThread> {
    protected EventMessageThreadUpdated(JobUpdateMessageThread job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadUpdated(JobUpdateMessageThread job, MessageThread messageThread) {
        super(job, messageThread);
    }
}
