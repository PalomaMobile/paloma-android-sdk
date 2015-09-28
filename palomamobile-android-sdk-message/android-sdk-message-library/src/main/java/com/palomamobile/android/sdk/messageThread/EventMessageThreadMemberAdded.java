package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Created by Karel Herink
 */
public class EventMessageThreadMemberAdded extends BaseJobEvent<JobAddMessageThreadMember, MessageThreadMember> {
    protected EventMessageThreadMemberAdded(JobAddMessageThreadMember job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadMemberAdded(JobAddMessageThreadMember job, MessageThreadMember messageThreadMember) {
        super(job, messageThreadMember);
    }
}
