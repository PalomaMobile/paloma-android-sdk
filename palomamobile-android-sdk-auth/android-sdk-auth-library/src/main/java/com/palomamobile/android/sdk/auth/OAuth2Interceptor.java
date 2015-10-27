package com.palomamobile.android.sdk.auth;

import android.util.Pair;
import com.palomamobile.android.sdk.core.CustomHeader;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

class OAuth2Interceptor implements Interceptor {

    public static final Logger logger = LoggerFactory.getLogger(OAuth2Interceptor.class);

    private IAuthManager authManager;

    public OAuth2Interceptor(IAuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logger.debug("request => " + request);

        AuthType authType = getAuthType(request);

        String requestId = request.header(CustomHeader.HEADER_NAME_PALOMA_REQUEST);
        if (requestId == null) {
            throw new RuntimeException(CustomHeader.HEADER_NAME_PALOMA_REQUEST + " header must be present on all requests.");
        }

        Pair<String, String> authorizationHeader = null;

        switch (authType) {
            case Client: {
                logger.debug("Client Auth required.");
                AccessToken clientAccessToken = authManager.getClientAccessToken(IAuthManager.TokenRetrievalMode.CACHE_THEN_NETWORK, requestId);
                authorizationHeader = new Pair<>("Authorization", AuthUtils.getBearerAuthHeaderValue(clientAccessToken.getAccessToken()));
                break;
            }
            case User: {
                logger.debug("User Auth required.");
                AccessToken userAccessToken = authManager.getUserAccessToken(IAuthManager.TokenRetrievalMode.CACHE_THEN_NETWORK, requestId);
                authorizationHeader = new Pair<>("Authorization", AuthUtils.getBearerAuthHeaderValue(userAccessToken.getAccessToken()));
                break;
            }
            case None: {
                logger.debug("No Auth required.");
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown AuthType: " + authType);
            }
        }

        Request.Builder newRequestBuilder = request.newBuilder();
        newRequestBuilder.removeHeader(IAuthManager.INTERNAL_AUTH_REQUIREMENT_HEADER_NAME);

        if (authorizationHeader != null) {
            logger.debug("adding authorizationHeader: " + authorizationHeader.first + ": " + authorizationHeader.second);
            newRequestBuilder.header(authorizationHeader.first, authorizationHeader.second);
        }
        Request newRequest = newRequestBuilder.build();
        logger.debug("newRequest => " + newRequest);

        Response response = chain.proceed(newRequest);
        logger.debug("response <= " + response);

        int respCode = response.code();

        ///This code deals with token expiry
        if (respCode == java.net.HttpURLConnection.HTTP_UNAUTHORIZED) {
            logger.debug("Response was HTTP_UNAUTHORIZED (401) for AuthType." + authType.name());
            switch (authType) {
                // Expired user token is reasonable as per spec
                case User: {
                    response = handleUserTokenExpired(chain, newRequestBuilder, requestId);
                    break;
                }
                // Expired client token is something I can imagine
                case Client: {
                    response = handleClientTokenExpired(chain, newRequestBuilder, requestId);
                    break;
                }
                //otherwise something is wrong most likely the client id and/or client signature are misconfigured
                case None : {
                    IllegalStateException e = new IllegalStateException("HTTP_UNAUTHORIZED - most likely your client id and/or client signature are misconfigured");
                    logger.error("Configuration error.", e);
                    throw e;
                }
            }
        }
        return response;
    }

    private AuthType getAuthType(Request request) {
        AuthType authType = AuthType.User; //set AuthType.User as the default as most api calls use this
        List<String> authTypeHeaders = request.headers(IAuthManager.INTERNAL_AUTH_REQUIREMENT_HEADER_NAME);
        if (authTypeHeaders != null && authTypeHeaders.size() > 0) {
            if (authTypeHeaders.size() > 1) {
                logger.error("More than one X-RequiredAuth header present: " + authTypeHeaders);
            }
            try {
                authType = AuthType.valueOf(authTypeHeaders.get(0));
            } catch (IllegalArgumentException e) {
                logger.error("Illegal X-RequiredAuth header value: " + authTypeHeaders.get(0), e);
            }
        }
        return authType;
    }

    private Response handleUserTokenExpired(Chain chain, Request.Builder newRequestBuilder, String requestId) throws IOException {
        logger.debug("response was HTTP_UNAUTHORIZED for AuthType.User - OK need to refresh the User token");
        AccessToken cachedUserAccessToken = authManager.getUserAccessToken(IAuthManager.TokenRetrievalMode.CACHE_ONLY, requestId);
        if (cachedUserAccessToken != null) {
            logger.debug("we have a cached User token: " + cachedUserAccessToken);
            String refreshToken = cachedUserAccessToken.getRefreshToken();
            if (refreshToken != null) {
                logger.debug("we have a refresh User token: " + refreshToken + ", doing a user token refresh");
                AccessToken refreshedUserAccessToken = authManager.refreshUserAccessToken(refreshToken, requestId);
                Pair<String, String> authorizationHeader = new Pair<>("Authorization", AuthUtils.getBearerAuthHeaderValue(refreshedUserAccessToken.getAccessToken()));
                newRequestBuilder.header(authorizationHeader.first, authorizationHeader.second);

                Request newRequest = newRequestBuilder.build();
                logger.debug("newRequest => " + newRequest);

                Response newResponse = chain.proceed(newRequest);
                logger.debug("newResponse <= " + newResponse);
                return newResponse;
            }
            else {
                logger.error("we DO NOT have a refresh User token, this should never happen!!!");
            }
        }
        else {
            logger.error("we DO NOT have a cached User token, how did this happen?");
            logger.error("TODO: need to get a token by kicking off Auth flow in the UI and coming back here with user credentials.");
        }
        return null;
    }

    private Response handleClientTokenExpired(Chain chain, Request.Builder newRequestBuilder, String requestId) throws IOException {
        AccessToken clientAccessToken = authManager.getClientAccessToken(IAuthManager.TokenRetrievalMode.NETWORK_ONLY, requestId);
        Pair<String, String> authorizationHeader = new Pair<>("Authorization", AuthUtils.getBearerAuthHeaderValue(clientAccessToken.getAccessToken()));
        newRequestBuilder.header(authorizationHeader.first, authorizationHeader.second);

        Request newRequest = newRequestBuilder.build();
        logger.debug("newRequest => " + newRequest);

        Response response = chain.proceed(newRequest);
        logger.debug("newResponse <= " + response);

        return response;
    }

}
