package com.palomamobile.android.sdk.notification;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.IEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * whenever a {@link Notification} for the client application is received on any channel (GCM, SMS etc.)
 * The event contains either the {@code notification} on success or {@code throwable} on
 * failure.<br/>
 *
 */
public class EventNotificationReceived implements IEvent<Notification> {

    private Notification notification;

    EventNotificationReceived(Notification notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventNotificationReceived{");
        sb.append("notification=").append(notification);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Notification getSuccess() {
        return notification;
    }

    @Override
    public Throwable getFailure() {
        return null;
    }
}
