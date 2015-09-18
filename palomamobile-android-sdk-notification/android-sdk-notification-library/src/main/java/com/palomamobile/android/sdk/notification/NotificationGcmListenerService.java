package com.palomamobile.android.sdk.notification;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;
import com.palomamobile.android.sdk.core.ServiceSupport;

/**
 * Handles incoming Gcm notifications by publishing {@link EventNotificationReceived} with notification contents.
 */
public class NotificationGcmListenerService extends GcmListenerService {
    private static final String GCM_KEY_CONTENT = "content";
    private static final String GCM_KEY_DEBUG = "debug";
    private static final String GCM_KEY_SENDER_CLIENT = "senderClient";
    private static final String GCM_KEY_TYPE = "type";
    private static final String GCM_KEY_URL = "url";
    private static final String GCM_KEY_RECIPIENT_ID = "recipientId";
    private static final String GCM_KEY_SENDER_ID = "senderId";
    private static final String GCM_KEY_TIMESTAMP = "timestamp";

    private static final String TAG = NotificationGcmListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.i(TAG, "onMessageReceived from: " + from + " data: " + data);
        Notification notification = new Notification();
        notification.setContent(data.getString(GCM_KEY_CONTENT));
        notification.setDebug(data.getString(GCM_KEY_DEBUG));
        notification.setSenderClient(data.getString(GCM_KEY_SENDER_CLIENT));
        notification.setType(data.getString(GCM_KEY_TYPE));
        notification.setUrl(data.getString(GCM_KEY_URL));

        String recipientId = data.getString(GCM_KEY_RECIPIENT_ID);
        notification.setRecipientId(recipientId == null ? null : Long.parseLong(recipientId));

        String senderId = data.getString(GCM_KEY_SENDER_ID);
        notification.setSenderId(senderId == null ? null : Long.parseLong(senderId));

        String timeStamp = data.getString(GCM_KEY_TIMESTAMP);
        notification.setTimestamp(timeStamp == null ? null : Long.parseLong(timeStamp));

        ServiceSupport.Instance.getEventBus().post(new EventNotificationReceived(notification));
    }
}
