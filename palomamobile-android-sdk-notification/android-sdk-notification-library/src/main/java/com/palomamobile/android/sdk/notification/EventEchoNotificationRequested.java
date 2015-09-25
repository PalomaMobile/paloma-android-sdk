package com.palomamobile.android.sdk.notification;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * whenever a {@link Notification} for the client application is requested.
 * The event contains either the {@code notification} on request success or {@code throwable} on
 * request failure.<br/>
 */
public class EventEchoNotificationRequested extends BaseJobEvent<JobPostEchoNotification, Notification> {
    protected EventEchoNotificationRequested(JobPostEchoNotification job, Throwable failure) {
        super(job, failure);
    }

    protected EventEchoNotificationRequested(JobPostEchoNotification job, Notification notification) {
        super(job, notification);
    }
}
