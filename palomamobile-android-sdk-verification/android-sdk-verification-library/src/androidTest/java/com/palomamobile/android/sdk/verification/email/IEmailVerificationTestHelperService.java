package com.palomamobile.android.sdk.verification.email;

import com.palomamobile.android.sdk.auth.IAuthManager;
import com.palomamobile.android.sdk.core.CustomHeader;
import com.palomamobile.android.sdk.verification.BuildConfig;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Path;

/**
 * Implement a helper service used only by tests and available for test data.
 */
public interface IEmailVerificationTestHelperService {

    /**
     * Helper API for test purposes only as described in Verification Service API docs. Will return verification code only for addresses
     * ending with <b>@example.com</b>.
     *
     * @param requestId
     * @param emailAddress
     * @return update that includes the secret verification code
     */
    @Headers({
            IAuthManager.INTERNAL_AUTH_REQUIREMENT_HEADER_NAME + ": " + "Client",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION, CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME
    })
    @GET("/verification/emails/{address}")
    VerificationEmailUpdate getEmailVerification(@Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId, @Path("address") String emailAddress);

}
