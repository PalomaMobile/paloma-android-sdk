package com.palomamobile.android.sdk.notification;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.palomamobile.android.sdk.core.EventServiceManagerRegistered;
import com.palomamobile.android.sdk.core.IServiceSupport;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.EventLocalUserUpdated;
import com.palomamobile.android.sdk.user.IUserManager;

/**
 *
 */
class NotificationManager implements INotificationManager {
    private static final String CONFIG_NAME_GCM_SENDER_ID = "com.palomamobile.android.sdk.GcmSenderId";

    static final String CACHE_KEY_GCM_REGISTRATION_ID = "cache_key_gcm_registration_id";
    private static final String TAG = NotificationManager.class.getSimpleName();

    private INotificationService notificationService;

    public NotificationManager(IServiceSupport serviceSupport) {
        this.notificationService = serviceSupport.getRestAdapter().create(INotificationService.class);
        serviceSupport.getEventBus().register(this);
        serviceSupport.registerServiceManager(INotificationManager.class, this);


        IUserManager userManager = serviceSupport.getServiceManager(IUserManager.class);
        if (userManager != null) {
            serviceSupport.getJobManager().addJobInBackground(new JobUserUpdateGcmRegistrationId());
        }
    }

    @SuppressWarnings("unused")
    public void onEventBackgroundThread(EventServiceManagerRegistered event) {
        Log.d(TAG, "onEventBackgroundThread(" + event + ")");
        if (IUserManager.class == event.getIntrface()) {
            Log.d(TAG, "IUserManager instance available -> addJobInBackground(new JobUpdateGcmRegistrationId())");
            ServiceSupport.Instance.getJobManager().addJobInBackground(new JobUserUpdateGcmRegistrationId());
        }
    }

    public String getGcmSenderIdFromAppMetadata() {
        try {
            Context context = ServiceSupport.Instance.getContext();
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String gcmSenderId = bundle.getString(CONFIG_NAME_GCM_SENDER_ID);
            Log.d(TAG, "getGcmSenderIdFromAppMetadata() returns " + gcmSenderId);
            return gcmSenderId;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Failed to load meta-data, NameNotFound", e);
        }
    }

    public INotificationService getNotificationService() {
        return notificationService;
    }

    @SuppressWarnings("unused")
    public void onEventBackgroundThread(EventLocalUserUpdated eventLocalUserUpdated) {
        Log.d(TAG, "onEventBackgroundThread(): " + eventLocalUserUpdated);
        if (eventLocalUserUpdated.getSuccess() != null) {
            ServiceSupport.Instance.getJobManager().addJobInBackground(new JobUserUpdateGcmRegistrationId());
        }
    }

    @Override
    public JobPostEchoNotification createJobPostEchoNotification(Notification echo) {
        return new JobPostEchoNotification(echo);
    }

    @Override
    @NonNull
    public INotificationService getService() {
        return notificationService;
    }
}
