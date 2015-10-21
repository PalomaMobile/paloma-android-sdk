package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messageThread members changes as a result of messageThread member deletion.
 * {@link #getSuccess()}  will return always return {@code null}<br/>
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.<br/>
 * To delete a messageThread member use {@link IMessageThreadManager#createJobDeleteMessageThreadMember(long, long)}
 * <br/>
 */
public class EventMessageThreadMemberDeleted extends BaseJobEvent<JobDeleteMessageThreadMember, Void> {

    public EventMessageThreadMemberDeleted(JobDeleteMessageThreadMember job, Throwable failure) {
        super(job, failure);
    }

    public EventMessageThreadMemberDeleted(JobDeleteMessageThreadMember job) {
        super(job, (Void) null);
    }
}
