package com.palomamobile.android.sdk.verification.email;

import com.palomamobile.android.sdk.auth.IAuthManager;
import com.palomamobile.android.sdk.core.CustomHeader;
import com.palomamobile.android.sdk.user.User;
import com.palomamobile.android.sdk.verification.BuildConfig;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * This interface is consumed by the Retrofit library to provide access to the Paloma Mobile Platform Verification Service RESTful API.
 * All calls are synchronous. To get a concrete implementation of this interface call {@link IEmailVerificationManager#getService()}
 * This class provides somewhat low level access to the Server API it may be more convenient to use methods
 * in {@link IEmailVerificationManager}.
 * <br/>
 * @see <a href="http://54.251.112.144/docs/verification-service/index.html">Paloma Mobile Platform Verification Service RESTful API</a>
 */
public interface IEmailVerificationService {

    /**
     * Post an email address to be verified, this will trigger a verification email with a code to be sent to that email address.
     *
     * @param requestId for the purposes of identifying retries
     * @param emailAddress email address to verify
     * @return void
     * {@link JobCreateEmailVerification} provides a convenient wrapper, consider using it instead.
     */
    @Headers({
            IAuthManager.INTERNAL_AUTH_REQUIREMENT_HEADER_NAME + ": " + "Client",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME
    })
    @POST("/verification/emails/{address}")
    Void postEmailVerification(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("address") String emailAddress, @Body String ignored);


    /**
     * Complete verification cycle for a given email address by providing the secret code from the verification email.
     *
     * @param requestId for the purposes of identifying retries
     * @param emailAddress email address to verify
     * @param verificationEmailUpdate parameters for update of a verification email
     * @return void
     * {@link JobPostEmailVerificationUpdate} provides a convenient wrapper, consider using it instead.
     */
    @Headers({
            IAuthManager.INTERNAL_AUTH_REQUIREMENT_HEADER_NAME + ": " + "Client",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME
    })
    @PUT("/verification/emails/{address}")
    Void updateEmailVerification(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("address") String emailAddress, @Body VerificationEmailUpdate verificationEmailUpdate);


    /**
     * Update user verified email address and return the updated {@link User} that matches the provided userId on success. Throws {@link retrofit.RetrofitError} on failure.
     * <br/>{@link JobPostUserVerifiedEmail} provides a convenient wrapper, consider using it instead.
     *
     * @param requestId for the purposes of identifying retries
     * @param userId
     * @param userVerifiedEmailUpdate
     * @return existing user
     */
    @Headers({
            IAuthManager.INTERNAL_AUTH_REQUIREMENT_HEADER_NAME + ": " + "User",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + com.palomamobile.android.sdk.user.BuildConfig.TARGET_SERVICE_VERSION,
            CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + com.palomamobile.android.sdk.user.BuildConfig.VERSION_NAME})
    @PUT("/users/{userId}/email")
    User updateUserEmail(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("userId") long userId, @Body UserVerifiedEmailUpdate userVerifiedEmailUpdate);

}
