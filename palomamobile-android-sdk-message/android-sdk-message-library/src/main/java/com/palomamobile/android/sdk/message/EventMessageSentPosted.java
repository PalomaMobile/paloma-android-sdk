package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.IJobEvent;

import java.util.List;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once a message is posted to a friend or friends. The event contains either {@code messageSent} on success or {@code throwable} on
 * failure.<br/>
 * To post a message use {@link IMessageManager#createJobPostMessageToFriend(String, String, String, long)} or {@link IMessageManager#createJobPostMessageToFriends(String, String, String, List)}
 * <br/>
 *
 */
public class EventMessageSentPosted implements IJobEvent<JobPostMessage, MessageSent> {

    private MessageSent messageSent;
    private JobPostMessage job;
    private Throwable throwable;

    EventMessageSentPosted(JobPostMessage job, Throwable throwable) {
        this.job = job;
        this.throwable = throwable;
    }

    EventMessageSentPosted(JobPostMessage job, MessageSent messageSent) {
        this.job = job;
        this.messageSent = messageSent;
    }

    public MessageSent getSuccess() {
        return messageSent;
    }

    public Throwable getFailure() {
        return throwable;
    }

    @Override
    public String toString() {
        return "EventMessageSentPosted{" +
                "job=" + job +
                ", messageSent=" + messageSent +
                ", throwable=" + throwable +
                '}';
    }

    public JobPostMessage getJob() {
        return job;
    }
}
