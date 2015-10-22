package com.palomamobile.android.sdk.auth;

import com.palomamobile.android.sdk.core.CustomHeader;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * {@code IAuthService} provides API calls to retrieve Client and User tokens.
 * This class provides somewhat low level access to the Server API it may be more convenient to use methods in {@link IAuthManager}.
 * <br/>
 *
 */
public interface IAuthService {

    /**
     * Retrieve Client access token.<br/>
     *
     * @param authorizationValue base64 encoded basic auth header with client id and client password
     * @param requestId of the request that triggered this method call
     * @param grantType requires {@code client_credentials}
     * @return client access token
     */
    @Headers({
            IAuthManager.INTERNAL_AUTH_REQUIREMENT_HEADER_NAME + ": " + "None",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION
            , CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/oauth/token")
    @FormUrlEncoded
    AccessToken getClientAccessToken(@Header("Authorization") String authorizationValue,
                                     @Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId,
                                     @Field("grant_type") String grantType
    );

    /**
     * Retrieve User access token based on user credentials.<br/>
     *
     * @param authorizationValue base64 encoded basic auth header with client id and client password
     * @param requestId of the request that triggered this method call
     * @param userName
     * @param passsword
     * @param credentialType supported values are {@code password} or {@code facebook}
     * @param grantType requires {@code password}
     * @return {@link AccessToken} which also includes a {@link AccessToken#refreshToken}
     */
    @Headers({
            IAuthManager.INTERNAL_AUTH_REQUIREMENT_HEADER_NAME + ": " + "None",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION,
            CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/oauth/token")
    @FormUrlEncoded
    AccessToken getUserAccessToken(@Header("Authorization") String authorizationValue,
                                   @Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId,
                                   @Field("username") String userName,
                                   @Field("password") String passsword,
                                   @Field("credential_type") String credentialType,
                                   @Field("grant_type") String grantType
    );



    /**
     * Retrieve a new User access token based on a refresh token.<br/>
     *
     * @param authorizationValue base64 encoded basic auth header with client id and client password
     * @param requestId of the request that triggered this method call
     * @param grantType requires {@code refresh_token}
     * @param refreshToken refresh token value as previously returned in {@link AccessToken#refreshToken} from {@link #getUserAccessToken(String, String, String, String, String, String)}
     * @return
     */
    @Headers({
            IAuthManager.INTERNAL_AUTH_REQUIREMENT_HEADER_NAME + ": " + "None",
            CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION + ": " + BuildConfig.TARGET_SERVICE_VERSION,
            CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION + ": " + BuildConfig.VERSION_NAME})
    @POST("/oauth/token")
    @FormUrlEncoded
    AccessToken refreshUserAccessToken(@Header("Authorization") String authorizationValue,
                                       @Header(CustomHeader.HEADER_NAME_PALOMA_REQUEST) String requestId,
                                       @Field("grant_type") String grantType,
                                       @Field("refresh_token") String refreshToken
    );
}
