package com.palomamobile.android.sdk.auth;

import android.support.annotation.Nullable;
import com.palomamobile.android.sdk.core.IServiceManager;

/**
 * This interface is normally <b>NOT of interest to Application developers</b> unless you are intending to extend or modify the SDK functionality.<br/>
 * Implementing classes provide access to Client and User access tokens that are required when accessing authenticated Paloma Services over network.
 * <br/>
 */
public interface IAuthManager extends IServiceManager<IAuthService> {

    /**
     * Specifies the Auth requirements of a service call. For example if a call is known to require User Authentication (eg.
     * to update user attributes) then adding this header to the service call with {@link AuthType#User} will cause
     * the SDK to ensure the presence of the User Auth token header, this may lead to additional network calls if token is not yet cached.
     * If no header value is specified then {@link AuthType#User} is assumed as default since it is required by most service calls.
     */
    String INTERNAL_AUTH_REQUIREMENT_HEADER_NAME = "Internal-RequiredAuth";

    /**
     * Removes both Client and User access token from cache so that the next call to {@link #getUserAccessToken(TokenRetrievalMode, String)}
     * or {@link #getClientAccessToken(TokenRetrievalMode, String)} with a {@link com.palomamobile.android.sdk.auth.IAuthManager.TokenRetrievalMode#CACHE_ONLY}
     * parameter will return {@code null}
     */
    void clearCachedTokens();

    /**
     * Specifies the token retrieval options.
     */
    enum TokenRetrievalMode {
        /**
         * Get the token from local cache only. Do not attempt network.
         */
        CACHE_ONLY,

        /**
         * Get the token from network only. Ignore any cached value.
         */
        NETWORK_ONLY,

        /**
         * First attempt to retrieve token from local cache, if cached token not available then try to retrieve one from network.
         */
        CACHE_THEN_NETWORK,
    }

    /**
     * Retrieve the access token for the client application. The method may return {@code null} if the Access Token cannot
     * be retrieved using the {@link com.palomamobile.android.sdk.auth.IAuthManager.TokenRetrievalMode} specified.
     * @param mode of retrieval
     * @return client application access token
     */
    @Nullable AccessToken getClientAccessToken(TokenRetrievalMode mode, String requestId);

    /**
     * Retrieve the access token for the application user. The method may return {@code null} if the Access Token cannot
     * be retrieved using the {@link com.palomamobile.android.sdk.auth.IAuthManager.TokenRetrievalMode} specified.
     *
     * @param mode of retrieval
     * @param requestId of the request that triggered this method call
     * @return user access token
     * @throws CredentialsProviderMissingException if calling this method results in an attempt to retrieve the user token from the network
     * before {@link #setUserCredentialsProvider(IUserCredentialsProvider)} has been called, this is because we must be
     * able to Auth the user with the provided credentials before we can get the token back
     */
    @Nullable AccessToken getUserAccessToken(TokenRetrievalMode mode, String requestId);

    /**
     * Retrieve a fresh access token for the application user using the provided refresh token.
     * @param refreshToken user refresh token returned with the previous user token
     * @param requestId of the request that triggered this method call
     * @return fresh user access token
     */
    AccessToken refreshUserAccessToken(String refreshToken, String requestId);

    /**
     * Registers an implementation of the {@link IUserCredentialsProvider} interface. This method must be called
     * must be registered prior to attempting to {@link #getUserAccessToken(TokenRetrievalMode, String)} over network, since
     * retrieving a user token requires a valid set of user credentials.
     * @param credentialsProvider
     */
    void setUserCredentialsProvider(IUserCredentialsProvider credentialsProvider);

}
