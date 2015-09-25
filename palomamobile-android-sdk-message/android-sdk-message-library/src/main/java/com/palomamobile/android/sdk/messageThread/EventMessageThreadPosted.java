package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 *
 */
public class EventMessageThreadPosted extends BaseJobEvent<JobPostMessageThread, MessageThread> {
    protected EventMessageThreadPosted(JobPostMessageThread job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadPosted(JobPostMessageThread job, MessageThread messageThread) {
        super(job, messageThread);
    }
}
