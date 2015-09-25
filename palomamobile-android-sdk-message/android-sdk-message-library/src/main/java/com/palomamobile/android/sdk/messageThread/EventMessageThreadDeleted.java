package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 *
 */
public class EventMessageThreadDeleted extends BaseJobEvent<JobDeleteMessageThread, Void> {

    protected EventMessageThreadDeleted(JobDeleteMessageThread job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadDeleted(JobDeleteMessageThread job) {
        super(job, (Void) null);
    }
}
