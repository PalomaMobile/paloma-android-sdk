package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 *
 */
public class EventMessageThreadMemberDeleted extends BaseJobEvent<JobDeleteMessageThreadMember, Void> {

    protected EventMessageThreadMemberDeleted(JobDeleteMessageThreadMember job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadMemberDeleted(JobDeleteMessageThreadMember job) {
        super(job, (Void) null);
    }
}
