package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.IJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messages received changes as a result of message deletion.
 * {@link #getFailure()} will return a {@code null} on success, on failure a non-null {@code throwable}.
 * To delete a message use {@link IMessageManager#createJobDeleteMessageReceived(long)}
 * To refresh a list of received messages use {@link IMessageManager#createJobGetMessagesReceived()}
 * <br/>
 *
 */
public class EventMessageReceivedDeleted implements IJobEvent<JobDeleteMessageReceived, Void> {

    private Throwable throwable;
    private JobDeleteMessageReceived job;

    EventMessageReceivedDeleted(JobDeleteMessageReceived job, Throwable throwable) {
        this.job = job;
        this.throwable = throwable;
    }

    EventMessageReceivedDeleted(JobDeleteMessageReceived job) {
        this.job = job;
    }


    public Throwable getFailure() {
        return throwable;
    }

    @Override
    public String toString() {
        return "EventMessageReceivedDeleted{" +
                "job=" + job +
                ", throwable=" + throwable +
                '}';
    }

    public JobDeleteMessageReceived getJob() {
        return job;
    }

    @Override
    public Void getSuccess() {
        return null;
    }

}
