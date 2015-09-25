package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.CustomHeader;
import com.palomamobile.android.sdk.core.PaginatedResponse;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;

import java.util.Map;

/**
 * This interface is consumed by the Retrofit library to provide access to the Paloma Mobile Platform Friend Service RESTful API.
 * All calls are synchronous. To get a concrete implementation of this interface call {@link IFriendManager#getService()}
 * This class provides somewhat low level access to the Server API it may be more convenient to use methods in {@link IFriendManager}.
 * <br/>
 * @see <a href="http://54.251.112.144/docs/friend-service/index.html">Paloma Mobile Platform Friend Service RESTful API</a>
 */
public interface IFriendService {

    /**
     * Get list of friends for the user.
     * <br/>{@link JobGetFriends} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @param options
     * @return
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @GET("/users/{userId}/friends")
    PaginatedResponse<Friend> getFriends(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @QueryMap Map<String, String> options);

    /**
     * Post user's credentials for the purposes of friend matching.
     * <br/>{@link JobPostSocialUserCredential} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @param credential
     * @param options
     * @return
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/users/{userId}/socialusercredentials")
    Void postSocialUserCredentials(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @Body SocialUserCredential credential, @QueryMap Map<String, String> options);

    /**
     * Update the attributes of user's relationship with another user.
     * <br/>{@link JobPutRelationship} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @param reciprocalUserId
     * @param relationAttributes
     * @return
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/users/{userId}/relationships/{reciprocalUserId}")
    Relationship addRelationship(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @Path("reciprocalUserId") long reciprocalUserId, @Body RelationAttributes relationAttributes);

    /**
     * Get the list of relationships this user has with other users.
     * <br/>{@link JobGetRelationships} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @param options
     * @return
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @GET("/users/{userId}/relationships")
    PaginatedResponse<Relationship> getRelationships(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @QueryMap Map<String, String> options);

}
