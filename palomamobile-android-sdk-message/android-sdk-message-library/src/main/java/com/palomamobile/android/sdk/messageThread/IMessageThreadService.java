package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.CustomHeader;
import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.message.BuildConfig;
import com.palomamobile.android.sdk.message.MessageSent;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

import java.util.Map;

/**
 * This interface is consumed by the Retrofit library to provide access to the message threading functionality of the Paloma Mobile Platform
 * Sharing and Messaging Service RESTful API.
 * All calls are synchronous. To get a concrete implementation of this interface call {@link IMessageThreadManager#getService()}
 * This class provides somewhat low level access to the Server API it may be more convenient to use methods in {@link IMessageThreadManager}.
 * <br/>
 * @see <a href="http://54.251.112.144/docs/message-service/index.html">Paloma Mobile Platform Sharing and Messaging Service RESTful API</a>
 */
public interface IMessageThreadService {

    /**
     * Create a new named message thread, with an initial set of members.
     *
     * @param requestId
     * @param newMessageThread
     * @return newly created message thread
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/threads")
    MessageThread postMessageThread(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Body NewMessageThread newMessageThread);

    /**
     * Read thread attributes for the specified thread id.
     *
     * @param requestId
     * @param threadId
     * @return message thread
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @GET("/threads/{threadId}")
    MessageThread getMessageThread(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("threadId") long threadId);

    /**
     * Update the details of an existing thread.
     *
     * @param requestId
     * @param threadId
     * @param messageThreadUpdate
     * @return updated message thread
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/threads/{threadId}")
    MessageThread updateMessageThread(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("threadId") long threadId, @Body MessageThreadUpdate messageThreadUpdate);

    /**
     * Delete the thread associated with the specified thread id.
     * @param requestId
     * @param threadId
     * @return nothing
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @DELETE("/threads/{threadId}")
    Void deleteMessageThread(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("threadId") long threadId);

    /**
     * List all members of the specified thread, including the owner.
     *
     * @param requestId
     * @param threadId
     * @param options
     * @param filterQuery
     * @param sortOrder
     * @return paginated list of message thread members
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @GET("/threads/{threadId}/members")
    PaginatedResponse<MessageThreadMember> getMessageThreadMembers(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("threadId") long threadId, @QueryMap Map<String, String> options, @Query("where") String filterQuery, @Query("sort") String... sortOrder);

    /**
     * Add the specified user to the thread as a member.
     *
     * @param requestId
     * @param threadId
     * @param userId
     * @param emptyBody
     * @return added member
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/threads/{threadId}/members/{userId}")
    MessageThreadMember addMessageThreadMember(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("threadId") long threadId, @Path("userId") long userId, @Body String emptyBody);

    /**
     * Removes the specified user from the thread. All their existing received and sent thread messages remain, they just no longer can send or receive any new thread messages.
     *
     * @param requestId
     * @param threadId
     * @param userId
     * @return nothing
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @DELETE("/threads/{threadId}/members/{userId}")
    Void deleteMessageThreadMember(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("threadId") long threadId, @Path("userId") long userId);

    /**
     * List all messages of the specified thread.
     *
     * @param requestId
     * @param threadId
     * @param options
     * @param filterQuery
     * @param sortOrder
     * @return list of thread messages
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @GET("/threads/{threadId}/messages")
    PaginatedResponse<MessageSent> getMessages(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("threadId") long threadId, @QueryMap Map<String, String> options, @Query("where") String filterQuery, @Query("sort") String... sortOrder);

    /**
     * Send message to thread and hence to all members of the thread.
     *
     * @param requestId
     * @param threadId
     * @param message
     * @return
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/threads/{threadId}/messages")
    MessageSent postMessage(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("threadId") long threadId, @Body MessageSent message);

    /**
     * List all the threads the user is a member of.
     *
     * @param requestId
     * @param userId
     * @param options
     * @param filterQuery
     * @param sortOrder
     * @return
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @GET("/users/{userId}/threads")
    PaginatedResponse<MessageThread> getMessageThreads(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @QueryMap Map<String, String> options, @Query("where") String filterQuery, @Query("sort") String... sortOrder);

    /**
     * Delete all the threads the user is a owner of.
     *
     * @param requestId
     * @param userId
     * @return
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @DELETE("/users/{userId}/threads")
    Void deleteMessageThreads(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId);
}
