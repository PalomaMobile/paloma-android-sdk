package com.palomamobile.android.sdk.notification;

import android.content.Context;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.JsonObject;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.core.util.Utilities;
import com.palomamobile.android.sdk.user.IUserManager;
import com.palomamobile.android.sdk.user.User;
import com.path.android.jobqueue.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default provided implementation of the {@link INotificationManager} runs this job
 * whenever required to ensure the GcmRegistrationId is up to date on the back-end.
 *
 * Convenience wrapper around {@link INotificationService#addGcmRegistrationId(String, Long, String, JsonObject)}
 * Once this job is completed (with success or failure) it posts {@link EventGcmRegistrationIdUpdated} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobPostUserGcmRegistrationIdUpdate extends BaseRetryPolicyAwareJob<GcmRegistrationIdResponse> {

    public static final Logger logger = LoggerFactory.getLogger(JobPostUserGcmRegistrationIdUpdate.class);


    /**
     * Create a new job to request that the client updates the service back-end with the current GcmRegistrationId.
     */
    public JobPostUserGcmRegistrationIdUpdate() {
        this(new Params(0).requireNetwork());
    }

    /**
     * Create a new job to request that the client updates the service back-end with the current GcmRegistrationId.
     * @param params custom job params
     */
    protected JobPostUserGcmRegistrationIdUpdate(Params params) {
        super(params);
    }

    public GcmRegistrationIdResponse syncRun(boolean postEvent) throws Throwable {
        logger.info("onRun()");
        Context context = ServiceSupport.Instance.getContext();

        NotificationManager notificationManager = (NotificationManager) ServiceSupport.Instance.getServiceManager(INotificationManager.class);
        String gcmSenderId = notificationManager.getGcmSenderIdFromAppMetadata();

        String gcmRegistrationId = GcmUtils.getRegistrationId(context);

        //do we need to update gcmRegistrationId && are we able to
        if (gcmRegistrationId == null && GcmUtils.checkPlayServices(context)) {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
            if (gcm != null) {
                if (gcmSenderId != null && gcmSenderId.length() > 0) {
                    logger.debug("Asking GoogleCloudMessaging for a new gcmRegistrationId with gcmSenderId: " + gcmSenderId);

                    InstanceID instanceID = InstanceID.getInstance(context);
                    gcmRegistrationId = instanceID.getToken(gcmSenderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                    GcmUtils.storeRegistrationId(context, gcmRegistrationId);
                }
                else {
                    logger.debug("NOT asking GoogleCloudMessaging for a new gcmRegistrationId, gcmSenderId is not configured.");
                }
            }
            else {
                logger.info("Gcm NOT available");
            }
        }

        String lastServerGcmRegistrationId = ServiceSupport.Instance.getCache().get(NotificationManager.CACHE_KEY_GCM_REGISTRATION_ID, String.class);

        //see if we need to update local user with a new gcmRegistrationId
        User localUser = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser();
        logger.debug("gcmRegistrationId: " + gcmRegistrationId + ", lastServerGcmRegistrationId: " + lastServerGcmRegistrationId + ", localUser: " + localUser);
        if (gcmRegistrationId != null && localUser != null) {
            logger.debug("asking server to update user's gcmRegistrationId");
            return postGcmRegistrationIdUpdate(localUser.getId(), gcmRegistrationId, postEvent);
        }
        else {
            String reason = null;
            if (gcmRegistrationId == null) {
                reason = "current gcmRegistrationId NOT available";
            }
            if (localUser == null) {
                reason = "user not yet logged in";
            }
            logger.info("NOT asking server to update user's gcmRegistrationId because: " + reason);
            throw new IllegalStateException("Unable to update current user's gcm registration id: " + reason);
        }
    }

    private GcmRegistrationIdResponse postGcmRegistrationIdUpdate(final long userId, final String gcmRegistrationId, boolean postEvent) {
        JsonObject gcmRegistrationIdJson = new JsonObject();
        gcmRegistrationIdJson.addProperty("gcmRegistrationId", gcmRegistrationId);
        NotificationManager notificationManager = (NotificationManager) ServiceSupport.Instance.getServiceManager(INotificationManager.class);
        String deviceId = Utilities.getDeviceId(getApplicationContext());

        GcmRegistrationIdResponse gcmRegistrationIdResponse = notificationManager.getService().addGcmRegistrationId(getRetryId(), userId, deviceId, gcmRegistrationIdJson);
        logger.debug("SUCCESS (update cache) addGcmRegistrationId() for userId: " + userId + ", gcmRegistrationId: " + gcmRegistrationId);
        ServiceSupport.Instance.getCache().put(NotificationManager.CACHE_KEY_GCM_REGISTRATION_ID, gcmRegistrationIdResponse.getGcmRegistrationId());
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventGcmRegistrationIdUpdated(this, gcmRegistrationIdResponse));
        }
        return gcmRegistrationIdResponse;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventGcmRegistrationIdUpdated(this, throwable));
    }
}
