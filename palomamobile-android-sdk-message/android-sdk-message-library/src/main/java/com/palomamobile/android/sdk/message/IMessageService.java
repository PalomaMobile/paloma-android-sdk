package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.CustomHeader;
import com.palomamobile.android.sdk.core.PaginatedResponse;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

import java.util.Map;

/**
 * This interface is consumed by the Retrofit library to provide access to the messaging functionality of the Paloma Mobile Platform
 * Sharing and Messaging Service RESTful API.
 * All calls are synchronous. To get a concrete implementation of this interface call {@link IMessageManager#getService()}
 * This class provides somewhat low level access to the Server API it may be more convenient to use methods in {@link IMessageManager}.
 * <br/>
 * @see <a href="http://54.251.112.144/docs/message-service/index.html">Paloma Mobile Platform Sharing and Messaging Service RESTful API</a>
 */
public interface IMessageService {

    /**
     * Send a message to another user.
     * <br/>{@link JobPostMessage} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @param messageSent
     * @return
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/users/{userId}/messages/sent")
    MessageSent postMessageSent(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @Body MessageSent messageSent);

    /**
     * Get a list of previously sent messages.
     * <br/>{@link JobGetMessagesSent} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @param options
     * @param filterQuery
     * @param sortOrder
     * @return
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @GET("/users/{userId}/messages/sent")
    PaginatedResponse<MessageSent> getMessagesSent(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @QueryMap Map<String, String> options, @Query("where") String filterQuery, @Query("sort") String... sortOrder);

    /**
     * Get a list of received messages.
     * <br/>{@link JobGetMessagesReceived} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @param options
     * @param filterQuery
     * @param sortOrder
     * @return
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @GET("/users/{userId}/messages/received")
    PaginatedResponse<MessageReceived> getMessagesReceived(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @QueryMap Map<String, String> options, @Query("where") String filterQuery, @Query("sort") String... sortOrder);

    /**
     * Delete a received message.
     * <br/>{@link JobDeleteMessageReceived} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @param messageId
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @DELETE("/users/{userId}/messages/received/{messageId}")
    Void deleteMessageReceived(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @Path("messageId") long messageId);

    /**
     * Delete all received messages belonging to a user.
     * <br/>{@link JobDeleteMessageReceived} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @DELETE("/users/{userId}/messages/received")
    Void deleteMessagesReceived(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @Query("where") String filterQuery);

}
