package com.palomamobile.android.sdk.user;

import com.palomamobile.android.sdk.auth.IAuthManager;
import com.palomamobile.android.sdk.auth.IUserCredential;
import com.palomamobile.android.sdk.core.CustomHeader;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * This interface is consumed by the Retrofit library to provide access to the Paloma Mobile Platform User Service RESTful API.
 * All calls are synchronous. To get a concrete implementation of this interface call {@link IUserManager#getService()}
 * This class provides somewhat low level access to the Server API it may be more convenient to use methods in {@link IUserManager}.
 * <br/>
 * @see <a href="http://54.251.112.144/docs/user-service/index.html">Paloma Mobile Platform User Service RESTful API</a>
 */
public interface IUserService {

    /**
     * Register a new {@link User} with the provided {@code userCredential}. If such {@link User} already exists it is returned.
     * Throws {@link retrofit.RetrofitError} on failure.
     * <br/>{@link JobRegisterUser} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userCredential
     * @return new or existing user
     */
    @Headers({
            IAuthManager.AUTH_REQUIREMENT_HEADER_NAME + ": " + "Client",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION,
            CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/users")
    User registerUser(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Body IUserCredential userCredential);

    /**
     * Return an existing {@link User} that matches the provided credentials on success. Throws {@link retrofit.RetrofitError} on failure.
     * <br/>{@link JobLoginUser} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userCredential
     * @return existing user
     */
    @Headers({
            IAuthManager.AUTH_REQUIREMENT_HEADER_NAME + ": " + "Client",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION,
            CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/users/me")
    User validateCredentials(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Body IUserCredential userCredential);

    /**
     * Return an existing {@link User} that matches the provided userId on success. Throws {@link retrofit.RetrofitError} on failure.
     * <br/>{@link JobGetUser} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @return existing user
     */
    @Headers({
            IAuthManager.AUTH_REQUIREMENT_HEADER_NAME + ": " + "User",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION,
            CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @GET("/users/{userId}")
    User getUser(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId);

    /**
     * Update and return an existing {@link User} that matches the provided userId on success. Throws {@link retrofit.RetrofitError} on failure.
     * <br/>{@link JobPostUserUpdate} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @return existing user
     */
    @Headers({
            IAuthManager.AUTH_REQUIREMENT_HEADER_NAME + ": " + "User",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION,
            CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @PUT("/users/{userId}")
    User updateUser(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @Body UserUpdate userUpdate);

}
