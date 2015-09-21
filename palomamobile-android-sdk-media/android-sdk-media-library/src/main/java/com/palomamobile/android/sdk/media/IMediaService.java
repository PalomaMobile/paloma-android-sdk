package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.CustomHeader;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.mime.TypedInput;

/**
 * This interface is consumed by the Retrofit library to provide access to the Paloma Mobile Platform Media Service RESTful API.
 * All calls are synchronous. To get a concrete implementation of this interface call {@link IMediaManager#getService()}
 * This class provides somewhat low level access to the Server API it may be more convenient to use methods in {@link IMediaManager}.
 * <br/>
 * @see <a href="http://46.137.242.200/docs/media-service/index.html">Paloma Mobile Platform Media Service RESTful API</a>
 */
public interface IMediaService {

    /**
     * Upload file content, the posted content is accessible to anyone who can access the media url.
     * @param requestId for the purposes of identifying retries
     * @param file file to upload
     * @return raw {@link Response} rather than {@link MediaInfo} because the media url is returned in a header.
     * {@link JobUploadMediaPublic} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/media")
    Response postMediaPublic(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Body TypedInput file);


    /**
     * Upload file content, the posted content is accessible only to the authenticated user that originally posted the content.
     * @param requestId for the purposes of identifying retries
     * @param userId local user id
     * @param file file to upload
     * @return raw {@link Response} rather than {@link MediaInfo} because the media url is returned in a header.
     * {@link JobUploadMediaPrivate} provides a convenient wrapper, consider using it instead.
     */
    @Headers({CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/users/{userId}/media")
    Response postMediaPrivate(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @Body TypedInput file);


}
