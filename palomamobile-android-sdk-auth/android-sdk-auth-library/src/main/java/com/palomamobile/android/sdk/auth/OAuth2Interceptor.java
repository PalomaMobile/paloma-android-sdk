package com.palomamobile.android.sdk.auth;

import android.util.Log;
import android.util.Pair;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 *
 */
class OAuth2Interceptor implements Interceptor {

    public static final String TAG = OAuth2Interceptor.class.getSimpleName();

    private IAuthManager authManager;

    public OAuth2Interceptor(IAuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.d(TAG, "request => " + request);

        AuthType authType = AuthType.User; //set AuthType.User as the default as most api call use this
        List<String> authTypeHeaders = request.headers(IAuthManager.AUTH_REQUIREMENT_HEADER_NAME);
        if (authTypeHeaders != null && authTypeHeaders.size() > 0) {
            if (authTypeHeaders.size() > 1) {
                Log.e(TAG, "More than one X-RequiredAuth header present: " + authTypeHeaders);
            }
            try {
                authType = AuthType.valueOf(authTypeHeaders.get(0));
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Illegal X-RequiredAuth header value: " + authTypeHeaders.get(0), e);
            }
        }


        Pair<String, String> authorizationHeader = null;

        switch (authType) {
            case Client: {
                Log.d(TAG, "Client Auth required.");
                AccessToken clientAccessToken = authManager.getClientAccessToken(IAuthManager.TokenRetrievalMode.CACHE_THEN_NETWORK);
                authorizationHeader = new Pair<>("Authorization", AuthUtils.getBearerAuthHeaderValue(clientAccessToken.getAccessToken()));
                break;
            }
            case User: {
                Log.d(TAG, "User Auth required.");
                AccessToken userAccessToken = authManager.getUserAccessToken(IAuthManager.TokenRetrievalMode.CACHE_THEN_NETWORK);
                authorizationHeader = new Pair<>("Authorization", AuthUtils.getBearerAuthHeaderValue(userAccessToken.getAccessToken()));
                break;
            }
            case None: {
                Log.d(TAG, "No Auth required.");
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown AuthType: " + authType);
            }
        }

        Request.Builder newRequestBuilder = request.newBuilder();
        newRequestBuilder.removeHeader(IAuthManager.AUTH_REQUIREMENT_HEADER_NAME);

        if (authorizationHeader != null) {
            Log.d(TAG, "adding authorizationHeader: " + authorizationHeader.first + ": " + authorizationHeader.second);
            newRequestBuilder.header(authorizationHeader.first, authorizationHeader.second);
        }
        Request newRequest = newRequestBuilder.build();
        Log.d(TAG, "newRequest => " + newRequest);

        Response response = chain.proceed(newRequest);
        Log.d(TAG, "response <= " + response);

        int respCode = response.code();

        ///This code deals with token expiry
        if (respCode == java.net.HttpURLConnection.HTTP_UNAUTHORIZED) {
            Log.d(TAG, "Response was HTTP_UNAUTHORIZED (401) for AuthType." + authType.name());
            switch (authType) {
                // Expired user token is reasonable as per spec
                case User: {
                    response = handleUserTokenExpired(chain, newRequestBuilder);
                    break;
                }
                // Expired client token is something I can imagine
                case Client: {
                    response = handleClientTokenExpired(chain, newRequestBuilder);
                    break;
                }
                //otherwise something is wrong most likely the client id and/or client signature are misconfigured
                case None : {
                    IllegalStateException e = new IllegalStateException("HTTP_UNAUTHORIZED - most likely your client id and/or client signature are misconfigured");
                    Log.e(TAG, "Configuration error.", e);
                    throw e;
                }
            }
        }
        return response;
    }

    private Response handleUserTokenExpired(Chain chain, Request.Builder newRequestBuilder) throws IOException {
        Log.d(TAG, "response was HTTP_UNAUTHORIZED for AuthType.User - OK need to refresh the User token");
        AccessToken cachedUserAccessToken = authManager.getUserAccessToken(IAuthManager.TokenRetrievalMode.CACHE_ONLY);
        if (cachedUserAccessToken != null) {
            Log.d(TAG, "we have a cached User token: " + cachedUserAccessToken);
            String refreshToken = cachedUserAccessToken.getRefreshToken();
            if (refreshToken != null) {
                Log.d(TAG, "we have a refresh User token: " + refreshToken + ", doing a user token refresh");
                AccessToken refreshedUserAccessToken = authManager.refreshUserAccessToken(refreshToken);
                Pair<String, String> authorizationHeader = new Pair<>("Authorization", AuthUtils.getBearerAuthHeaderValue(refreshedUserAccessToken.getAccessToken()));
                newRequestBuilder.header(authorizationHeader.first, authorizationHeader.second);

                Request newRequest = newRequestBuilder.build();
                Log.d(TAG, "newRequest => " + newRequest);

                Response newResponse = chain.proceed(newRequest);
                Log.d(TAG, "newResponse <= " + newResponse);
                return newResponse;
            }
            else {
                Log.e(TAG, "we DO NOT have a refresh User token, this should never happen!!!");
            }
        }
        else {
            Log.e(TAG, "we DO NOT have a cached User token, how did this happen?");
            Log.e(TAG, "TODO: need to get a token by kicking off Auth flow in the UI and coming back here with user credentials.");
        }
        return null;
    }

    private Response handleClientTokenExpired(Chain chain, Request.Builder newRequestBuilder) throws IOException {
        AccessToken clientAccessToken = authManager.getClientAccessToken(IAuthManager.TokenRetrievalMode.NETWORK_ONLY);
        Pair<String, String> authorizationHeader = new Pair<>("Authorization", AuthUtils.getBearerAuthHeaderValue(clientAccessToken.getAccessToken()));
        newRequestBuilder.header(authorizationHeader.first, authorizationHeader.second);

        Request newRequest = newRequestBuilder.build();
        Log.d(TAG, "newRequest => " + newRequest);

        Response response = chain.proceed(newRequest);
        Log.d(TAG, "newResponse <= " + response);

        return response;
    }

}
