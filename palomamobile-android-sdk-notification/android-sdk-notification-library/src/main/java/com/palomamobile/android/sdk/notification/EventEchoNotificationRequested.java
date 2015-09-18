package com.palomamobile.android.sdk.notification;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.IJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * whenever a {@link Notification} for the client application is requested.
 * The event contains either the {@code notification} on request success or {@code throwable} on
 * request failure.<br/>
 *
 */
public class EventEchoNotificationRequested implements IJobEvent<JobPostEchoNotification, Notification> {

    private JobPostEchoNotification job;
    private Notification notification;
    private Throwable throwable;

    EventEchoNotificationRequested(JobPostEchoNotification job, Notification notification) {
        this.job = job;
        this.notification = notification;
    }

    EventEchoNotificationRequested(JobPostEchoNotification job, Throwable throwable) {
        this.job = job;
        this.throwable = throwable;
    }

    @Override
    public String toString() {
        return "EventEchoNotificationRequested{" +
                "job=" + job +
                ", notification=" + notification +
                ", throwable=" + throwable +
                '}';
    }

    @Override
    public JobPostEchoNotification getJob() {
        return job;
    }

    @Override
    public Notification getSuccess() {
        return notification;
    }

    @Override
    public Throwable getFailure() {
        return throwable;
    }
}
