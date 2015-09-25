package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messageThread members changes as a result of messageThread member addition.
 * {@link #getSuccess()}  will return a {@link MessageThreadMember} on success, or {@code null} on failure<br/>
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.<br/>
 * To add a messageThread member use {@link IMessageThreadManager#createJobAddMessageThreadMember(long, long)}
 * <br/>
 */
public class EventMessageThreadMemberAdded extends BaseJobEvent<JobAddMessageThreadMember, MessageThreadMember> {
    protected EventMessageThreadMemberAdded(JobAddMessageThreadMember job, Throwable failure) {
        super(job, failure);
    }

    protected EventMessageThreadMemberAdded(JobAddMessageThreadMember job, MessageThreadMember messageThreadMember) {
        super(job, messageThreadMember);
    }
}
