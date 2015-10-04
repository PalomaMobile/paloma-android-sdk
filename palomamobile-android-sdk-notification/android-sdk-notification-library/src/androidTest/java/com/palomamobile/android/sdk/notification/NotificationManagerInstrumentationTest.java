package com.palomamobile.android.sdk.notification;

import android.os.Build;
import android.test.InstrumentationTestCase;
import android.util.Log;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.TestUtilities;
import com.palomamobile.android.sdk.user.User;
import com.palomamobile.android.sdk.core.util.LatchedBusListener;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class NotificationManagerInstrumentationTest extends InstrumentationTestCase {

    public static final String TAG = NotificationManagerInstrumentationTest.class.getSimpleName();

    private NotificationManager notificationManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        notificationManager = (NotificationManager) ServiceSupport.Instance.getServiceManager(INotificationManager.class);
    }

    public void testGcmNotifications() throws Throwable {

        //don't run GCM notification test in emulator
        if (Build.FINGERPRINT.contains("generic")) return;

        final User user = TestUtilities.registerUserSynchronous(this);
        final Notification outNotification = new Notification();

        outNotification.setType("NS.test");
        outNotification.setRecipientId(user.getId());
        outNotification.setContent("just some content");
        outNotification.setDebug("just some debug info");
        outNotification.setSenderClient("user");
        outNotification.setUrl("http://www.google.com");
        outNotification.setSenderId(user.getId());
        outNotification.setTimestamp(System.currentTimeMillis());

        //later versions of the service will be able to persist the notification request and act on it once the client is registered
        //currently though we need to make sure the GCM registrationId has been sent to server before we ask for notifications to be delivered

        final LatchedBusListener<EventGcmRegistrationIdUpdated> gcmRegisteredLatchedBusListener = new LatchedBusListener<>(EventGcmRegistrationIdUpdated.class);
        ServiceSupport.Instance.getEventBus().register(gcmRegisteredLatchedBusListener);
        String curRegId = ServiceSupport.Instance.getCache().get(NotificationManager.CACHE_KEY_GCM_REGISTRATION_ID, String.class);
        if (curRegId == null) {
            Log.d(TAG, "We don't have a GCM_REGISTRATION_ID yet, waiting for it.");
            gcmRegisteredLatchedBusListener.await(30, TimeUnit.SECONDS);
            curRegId = ServiceSupport.Instance.getCache().get(NotificationManager.CACHE_KEY_GCM_REGISTRATION_ID, String.class);
        }
        ServiceSupport.Instance.getEventBus().unregister(gcmRegisteredLatchedBusListener);
        assertNotNull(curRegId);

        final LatchedBusListener<EventNotificationReceived> latchedBusListener = new LatchedBusListener<>(EventNotificationReceived.class);

        ServiceSupport.Instance.getEventBus().register(latchedBusListener);
        JobPostEchoNotification jobPostEchoNotification = notificationManager.createJobPostEchoNotification(outNotification);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobPostEchoNotification);

        latchedBusListener.await(30, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListener);
        EventNotificationReceived eventNotificationReceived = latchedBusListener.getEvent();
        assertNotNull(eventNotificationReceived);
        assertTrue(outNotification.equals(eventNotificationReceived.getSuccess()));

    }
}