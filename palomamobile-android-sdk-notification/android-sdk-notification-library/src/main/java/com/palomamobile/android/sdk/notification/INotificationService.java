package com.palomamobile.android.sdk.notification;

import com.google.gson.JsonObject;
import com.palomamobile.android.sdk.auth.IAuthManager;
import com.palomamobile.android.sdk.core.CustomHeader;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * This interface is consumed by the Retrofit library to provide access to the Paloma Mobile Platform Notification Service RESTful API.
 * All calls are synchronous. To get a concrete implementation of this interface call {@link INotificationManager#getService()}
 * This class provides somewhat low level access to the Server API it may be more convenient to use methods
 * in {@link INotificationManager}.
 * <br/>
 * @see <a href="http://46.137.242.200/docs/notification-service/index.html">Paloma Mobile Platform Notification Service RESTful API</a>
 */
public interface INotificationService {

    /**
     * Updates users GcmRegistrationId. The default provided implementation of the {@link INotificationManager} makes this call
     * whenever required to ensure the GcmRegistrationId is up to date.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @param deviceId
     * @param gcmRegistrationId
     * @return
     */
    @Headers({
            IAuthManager.INTERNAL_AUTH_REQUIREMENT_HEADER_NAME + ": " + "User",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION,
            CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/users/{userId}/devices/{deviceId}/gcm")
    GcmRegistrationIdResponse addGcmRegistrationId(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") Long userId, @Path("deviceId") String deviceId, @Body JsonObject gcmRegistrationId);

    /**
     * For testing only - each user is only allowed to post a Notification to her self.
     * <br/>{@link JobPostEchoNotification} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @param notification
     */
    @Headers({
            IAuthManager.INTERNAL_AUTH_REQUIREMENT_HEADER_NAME + ": " + "User",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION,
            CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/users/{userId}/notifications")
    Response postEchoNotification(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") Long userId, @Body Notification notification);

}
