package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.CustomHeader;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.mime.TypedInput;

/**
 * This interface is consumed by the Retrofit library to provide access to the Paloma Mobile Platform Media Service RESTful API.
 * All calls are synchronous. To get a concrete implementation of this interface call {@link IMediaManager#getService()}
 * This class provides somewhat low level access to the Server API it may be more convenient to use methods in {@link IMediaManager}.
 * <br/>
 * @see <a href="http://54.251.112.144/docs/media-service/index.html">Paloma Mobile Platform Media Service RESTful API</a>
 */
public interface IMediaService {

    //---- media namespace

    /**
     * Upload file content, the posted content is accessible to anyone who can access the media url.
     * @param requestId for the purposes of identifying retries
     * @param file file to upload
     * @return raw {@link Response} rather than {@link MediaInfo} because the media url is returned in a header.
     * {@link JobUploadMedia} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/media")
    Response postMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Body TypedInput file);

    /**
     * Upload file content, update existing media only (create not supported in the media name space).
     * @param requestId for the purposes of identifying retries
     * @param trailingMediaUri media identifier previously received as a service response to a
     * @param file file to upload
     * @return raw {@link Response} rather than {@link MediaInfo} because the media url is returned in a header.
     * {@link JobUploadMedia} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/media/{trailingMediaUri}")
    Response postMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("trailingMediaUri") String trailingMediaUri, @Body TypedInput file);


    //---- user namespace

    /**
     * Upload file content, the posted content is accessible only to the authenticated user that originally posted the content.
     * @param requestId for the purposes of identifying retries
     * @param userId local user id
     * @param file file to upload
     * @return raw {@link Response} rather than {@link MediaInfo} because the media url is returned in a header.
     * {@link JobUploadUserMedia} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/users/{userId}/media")
    Response postUserMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @Body TypedInput file);

    /**
     * Upload file content, the posted content is accessible only to the authenticated user that originally posted the content.
     * @param requestId for the purposes of identifying retries
     * @param userId local user id
     * @param trailingMediaUri media identifier such as file name
     * @param file file to upload
     * @return raw {@link Response} rather than {@link MediaInfo} because the media url is returned in a header.
     * {@link JobUploadUserMedia} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/users/{userId}/media/{trailingMediaUri}")
    Response updateNamedUserMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @Path("trailingMediaUri") String trailingMediaUri, @Body TypedInput file);


    //---- application namespace

    /**
     * Upload file content, content is publicly available.
     * @param requestId for the purposes of identifying retries
     * @param applicationName application name
     * @param file file to upload
     * @return raw {@link Response} rather than {@link MediaInfo} because the media url is returned in a header.
     * {@link JobUploadUserMedia} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/applications/{applicationName}/media")
    Response postApplicationMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("applicationName") String applicationName, @Body TypedInput file);

    /**
     * Upload file content, content is publicly available.
     * @param requestId for the purposes of identifying retries
     * @param applicationName application name
     * @param trailingMediaUri media identifier such as file name
     * @param file file to upload
     * @return raw {@link Response} rather than {@link MediaInfo} because the media url is returned in a header.
     * {@link JobUploadUserMedia} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/applications/{applicationName}/media/{trailingMediaUri}")
    Response updateNamedApplicationMedia(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("applicationName") String applicationName, @Path("trailingMediaUri") String trailingMediaUri, @Body TypedInput file);

}
