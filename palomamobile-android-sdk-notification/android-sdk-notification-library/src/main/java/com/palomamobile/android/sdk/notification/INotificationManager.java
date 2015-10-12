package com.palomamobile.android.sdk.notification;

import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Methods in this interface provide convenient job creation methods that provide easy access
 * to the underlying {@link INotificationService} functionality. App developers can either use {@link BaseRetryPolicyAwareJob}
 * job instances returned by the {@code createJob...()} methods, or create custom jobs that invoke
 * methods of the {@link INotificationService} returned by {@link IServiceManager#getService()}
 *
 * <br/>
 * To get a concrete implementation of this interface call
 * {@code ServiceSupport.Instance.getServiceManager(INotificationManager.class)}
 * <br/>
 *
 * <br/>
 *
 */
public interface INotificationManager extends IServiceManager<INotificationService> {

    /**
     * Normally notifications are only received on the client as a result of some action taking
     * place on the server (eg. message received, friend found etc.).
     * This method exists solely to provide the ability to test Notifications on their own without the need for
     * additional triggers. This asynchronous method will ask the server to return the provided {@code echo} notification via
     * the configured channel (GCM, SMS, etc.).</br>
     * When the request to send notification is posted to server an {@link EventEchoNotificationRequested} is published on the {@link com.palomamobile.android.sdk.core.IEventBus}
     * as returned by {@link ServiceSupport#getEventBus()}.</br>
     * When a notification is received {@link EventNotificationReceived} is published on the {@link com.palomamobile.android.sdk.core.IEventBus}
     * as returned by {@link ServiceSupport#getEventBus()}.</br>
     *
     * @param echo notification sent to the server to be received back via a notification channel
     */
    JobPostEchoNotification createJobPostEchoNotification(Notification echo);
}
