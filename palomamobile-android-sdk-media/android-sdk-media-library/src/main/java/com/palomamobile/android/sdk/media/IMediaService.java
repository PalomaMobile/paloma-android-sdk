package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.CustomHeader;
import com.palomamobile.android.sdk.core.PaginatedResponse;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit.mime.TypedInput;

import java.util.Map;

/**
 * This interface is consumed by the Retrofit library to provide access to the Paloma Mobile Platform Media Service RESTful API.
 * All calls are synchronous. To get a concrete implementation of this interface call {@link IMediaManager#getService()}
 * This class provides somewhat low level access to the Server API it may be more convenient to use methods in {@link IMediaManager}.
 * <br/>
 * @see <a href="http://46.137.242.200/docs/media-service/index.html">Paloma Mobile Platform Media Service RESTful API</a>
 */
public interface IMediaService {

    /**
     * Header with this name identifies each individual chunk as part of a single transfer.
     */
    String HEADER_NAME_PALOMA_TRANSFER_ID = "X-Paloma-Transfer";
    String HEADER_NAME_CONTENT_MD5 = "Content-MD5";

    //---- media namespace

    /**
     * Upload file content, the posted content is accessible to anyone who can access the media url.
     * @param requestId for the purposes of identifying retries
     * @param file file to upload
     * @return description of media
     * {@link JobUploadMedia} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/media")
    MediaInfo postMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Body TypedInput file);

    /**
     * Upload file content, update existing media only (create not supported in the media name space).
     * @param requestId for the purposes of identifying retries
     * @param trailingMediaUri media identifier previously received as a service response to a
     * @param file file to upload
     * @return description of media
     * {@link JobUploadMedia} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/media/{trailingMediaUri}")
    MediaInfo postMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("trailingMediaUri") String trailingMediaUri, @Body TypedInput file);


    //Upload a chunk of data, the posted content is accessible to anyone who can access the media url.
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/media")
    MediaInfo postMediaChunk(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId,
                             @Header(HEADER_NAME_PALOMA_TRANSFER_ID) String transferId,
                             @Header("Content-Range") String contentRangeHeaderValue,
                             @Body TypedInput chunk,
                             @Header(HEADER_NAME_CONTENT_MD5) String contentMd5);

    //Upload a chunk of data, the posted content is accessible to anyone who can access the media url.
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/media/{trailingMediaUri}")
    MediaInfo putMediaChunk(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId,
                            @Header(HEADER_NAME_PALOMA_TRANSFER_ID) String transferId,
                            @Header("Content-Range") String contentRangeHeaderValue,
                            @Path("trailingMediaUri") String trailingMediaUri,
                            @Body TypedInput chunk,
                            @Header(HEADER_NAME_CONTENT_MD5) String contentMd5);

    //---- user namespace

    /**
     * Upload file content, the posted content is accessible only to the authenticated user that originally posted the content.
     * @param requestId for the purposes of identifying retries
     * @param userId local user id
     * @param file file to upload
     * @return description of media
     * {@link JobUploadUserMedia} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/users/{userId}/media")
    MediaInfo postUserMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @Body TypedInput file);

    /**
     * Upload file content, the posted content is accessible only to the authenticated user that originally posted the content.
     * @param requestId for the purposes of identifying retries
     * @param userId local user id
     * @param trailingMediaUri media identifier such as file name
     * @param file file to upload
     * @return description of media
     * {@link JobUploadUserMedia} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/users/{userId}/media/{trailingMediaUri}")
    MediaInfo updateNamedUserMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @Path("trailingMediaUri") String trailingMediaUri, @Body TypedInput file);


    //Upload a chunk of data, the posted content is accessible only to the authenticated user that originally posted the content.
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/users/{userId}/media")
    MediaInfo postUserMediaChunk(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId,
                                 @Header(HEADER_NAME_PALOMA_TRANSFER_ID) String transferId,
                                 @Header("Content-Range") String contentRangeHeaderValue,
                                 @Path("userId") long userId,
                                 @Body TypedInput chunk,
                                 @Header(HEADER_NAME_CONTENT_MD5) String contentMd5);

    //Upload a chunk of data, the posted content is accessible only to the authenticated user that originally posted the content.
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/users/{userId}/media/{trailingMediaUri}")
    MediaInfo putUserMediaChunk(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId,
                                @Header(HEADER_NAME_PALOMA_TRANSFER_ID) String transferId,
                                @Header("Content-Range") String contentRangeHeaderValue,
                                @Path("userId") long userId,
                                @Path("trailingMediaUri") String trailingMediaUri,
                                @Body TypedInput chunk,
                                @Header(HEADER_NAME_CONTENT_MD5) String contentMd5);

    //---- application namespace

    /**
     * Upload file content, content is publicly available.
     * @param requestId for the purposes of identifying retries
     * @param applicationName application name
     * @param file file to upload
     * @return description of media
     * {@link JobUploadUserMedia} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/applications/{applicationName}/media")
    MediaInfo postApplicationMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("applicationName") String applicationName, @Body TypedInput file);

    /**
     * Upload file content, content is publicly available.
     * @param requestId for the purposes of identifying retries
     * @param applicationName application name
     * @param trailingMediaUri media identifier such as file name
     * @param file file to upload
     * @return description of media
     * {@link JobUploadUserMedia} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/applications/{applicationName}/media/{trailingMediaUri}")
    MediaInfo updateNamedApplicationMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("applicationName") String applicationName, @Path("trailingMediaUri") String trailingMediaUri, @Body TypedInput file);

    //Upload a chunk of data, content is publicly available.
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/applications/{applicationName}/media")
    MediaInfo postApplicationMediaChunk(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId,
                                        @Header(HEADER_NAME_PALOMA_TRANSFER_ID) String transferId,
                                        @Header("Content-Range") String contentRangeHeaderValue,
                                        @Path("applicationName") String applicationName,
                                        @Body TypedInput chunk,
                                        @Header(HEADER_NAME_CONTENT_MD5) String contentMd5);

    //Upload a chunk of data, content is publicly available.
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/applications/{applicationName}/media/{trailingMediaUri}")
    MediaInfo putApplicationMediaChunk(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId,
                                       @Header(HEADER_NAME_PALOMA_TRANSFER_ID) String transferId,
                                       @Header("Content-Range") String contentRangeHeaderValue,
                                       @Path("applicationName") String applicationName,
                                       @Path("trailingMediaUri") String trailingMediaUri,
                                       @Body TypedInput chunk,
                                       @Header(HEADER_NAME_CONTENT_MD5) String contentMd5);


    //--------------------------------

    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @GET("/users/{userId}/media")
    PaginatedResponse<MediaInfo> listUserMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @QueryMap Map<String, String> options, @Query("where") String filterQuery, @Query("sort") String... sortOrder);
}
