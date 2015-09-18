package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.IJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once the list of messages received on the client is refreshed.
 * The event contains either a current list of messages received as returned by {@link #getSuccess()}  on success or {@code throwable} on
 * failure.
 * To refresh a list of received messages use {@link IMessageManager#createJobGetMessagesReceived()}
 * <br/>
 *
 */
public class EventMessagesReceived implements IJobEvent<JobGetMessagesReceived, PaginatedResponse<MessageReceived>> {

    private Throwable throwable;
    private JobGetMessagesReceived job;
    private PaginatedResponse<MessageReceived> messagesReceived;

    EventMessagesReceived(JobGetMessagesReceived job, Throwable throwable) {
        this.job = job;
        this.throwable = throwable;
    }

    EventMessagesReceived(JobGetMessagesReceived job, PaginatedResponse<MessageReceived> messagesReceived) {
        this.job = job;
        this.messagesReceived = messagesReceived;
    }

    public PaginatedResponse<MessageReceived> getSuccess() {
        return messagesReceived;
    }

    public Throwable getFailure() {
        return throwable;
    }

    @Override
    public String toString() {
        return "EventMessagesReceived{" +
                "job=" + job +
                ", throwable=" + throwable +
                ", messagesReceived=" + messagesReceived +
                '}';
    }

    public JobGetMessagesReceived getJob() {
        return job;
    }
}
