package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.IJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * when the list of messages sent is refreshed.
 * The event contains either a current list of messages sent as returned by {@link #getSuccess()} on success or
 * {@code throwable} on failure.
 * To refresh a list of sent messages use {@link IMessageManager#createJobGetMessagesSent()}
 * <br/>
 */
public class EventMessagesSent implements IJobEvent<JobGetMessagesSent, PaginatedResponse<MessageSent>> {

    private Throwable throwable;
    private JobGetMessagesSent job;
    private PaginatedResponse<MessageSent> messagesSent;

    EventMessagesSent(JobGetMessagesSent job, Throwable throwable) {
        this.job = job;
        this.throwable = throwable;
    }

    EventMessagesSent(JobGetMessagesSent job, PaginatedResponse<MessageSent> messagesSent) {
        this.job = job;
        this.messagesSent = messagesSent;
    }

    public PaginatedResponse<MessageSent> getSuccess() {
        return messagesSent;
    }

    public Throwable getFailure() {
        return throwable;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventMessagesSent{");
        sb.append("messagesSent=").append(messagesSent);
        sb.append('}');
        return sb.toString();
    }

    public JobGetMessagesSent getJob() {
        return job;
    }
}
