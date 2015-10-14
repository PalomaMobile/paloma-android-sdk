package com.palomamobile.android.sdk.auth;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import com.palomamobile.android.sdk.core.IServiceSupport;
import com.palomamobile.android.sdk.core.util.Utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
class AuthManager implements IAuthManager {

    public static final String TAG = AuthManager.class.getSimpleName();

    public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    public static final String GRANT_TYPE_PASSWORD = "password";
    public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";


    static final String CACHE_KEY_CLIENT_ACCESS_TOKEN = "cache_key_client_access_token";
    static final String CACHE_KEY_USER_ACCESS_TOKEN = "cache_key_user_access_token";

    private IServiceSupport serviceSupport;
    private IAuthService authService;
    private IUserCredentialsProvider userCredentialsProvider;
    private Context context;
    private String clientId;
    private String appSignature;


    public AuthManager(IServiceSupport serviceSupport) {
        this.context = serviceSupport.getContext();
        initClientCredentials();
        setServiceSupport(serviceSupport);
        serviceSupport.registerServiceManager(IAuthManager.class, this);
    }

    private void setServiceSupport(IServiceSupport serviceSupport) {
        this.serviceSupport = serviceSupport;
        this.authService = this.serviceSupport.getRestAdapter().create(IAuthService.class);
        this.serviceSupport.getOkHttpClient().interceptors().add(new OAuth2Interceptor(this));
    }

    private void initClientCredentials() {
        clientId = Utilities.getValueFromAppMetadata(context, Utilities.CONFIG_NAME_CLIENT_ID);
        try {
            appSignature = getApplicationSignature(context);
            Log.v(TAG, "Application signature: " + appSignature);
        } catch (Exception e) {
            throw new RuntimeException("Unable to determine ApplicationSignature.", e);
        }
    }

    public static String getApplicationSignature(Context context) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        String packageName = context.getPackageName();
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(pInfo.signatures[0].toByteArray());
        return Base64.encodeToString(md.digest(), Base64.NO_WRAP | Base64.URL_SAFE);
    }


    @Override
    public AccessToken getClientAccessToken(TokenRetrievalMode mode, String requestId) {
        AccessToken clientAccessToken = null;
        switch (mode) {
            case CACHE_ONLY:
                return this.serviceSupport.getCache().get(CACHE_KEY_CLIENT_ACCESS_TOKEN, AccessToken.class);
            case NETWORK_ONLY:
                clientAccessToken = authService.getClientAccessToken(AuthUtils.getBasicAuthHeaderValueBase64(clientId, appSignature), requestId, GRANT_TYPE_CLIENT_CREDENTIALS);
                serviceSupport.getCache().put(CACHE_KEY_CLIENT_ACCESS_TOKEN, clientAccessToken);
                break;
            case CACHE_THEN_NETWORK:
                clientAccessToken = getClientAccessToken(TokenRetrievalMode.CACHE_ONLY, requestId);
                if (clientAccessToken == null) {
                    clientAccessToken = getClientAccessToken(TokenRetrievalMode.NETWORK_ONLY, requestId);
                }
                break;
        }
        return clientAccessToken;
    }

    @Override
    public AccessToken getUserAccessToken(TokenRetrievalMode mode, String requestId) {
        AccessToken userAccessToken = null;
        switch (mode) {
            case CACHE_ONLY:
                return this.serviceSupport.getCache().get(CACHE_KEY_USER_ACCESS_TOKEN, AccessToken.class);
            case NETWORK_ONLY:
                if (userCredentialsProvider == null) {
                    throw new CredentialsProviderMissingException("Unable to retrieve UserAccessToken, first provide user credentials by calling authManager.setUserCredentialsProvider(...)");
                }
                IUserCredential userCredential = userCredentialsProvider.getUserCredential();
                userAccessToken = authService.getUserAccessToken(
                        AuthUtils.getBasicAuthHeaderValueBase64(clientId, appSignature),
                        requestId,
                        userCredential.getUsername(),
                        userCredential.getUserPassword(),
                        userCredential.getCredentialType(),
                        GRANT_TYPE_PASSWORD);
                if (userAccessToken != null) {
                    serviceSupport.getCache().put(CACHE_KEY_USER_ACCESS_TOKEN, userAccessToken);
                }
                break;
            case CACHE_THEN_NETWORK:
                userAccessToken = getUserAccessToken(TokenRetrievalMode.CACHE_ONLY, requestId);
                if (userAccessToken == null) {
                    userAccessToken = getUserAccessToken(TokenRetrievalMode.NETWORK_ONLY, requestId);
                }
                break;
        }
        return userAccessToken;
    }

    @Override
    public AccessToken refreshUserAccessToken(String refreshToken, String requestId) {
        AccessToken userAccessToken = authService.refreshUserAccessToken(
                AuthUtils.getBasicAuthHeaderValueBase64(clientId, appSignature),
                requestId,
                GRANT_TYPE_REFRESH_TOKEN,
                refreshToken);
        if (userAccessToken != null) {
            serviceSupport.getCache().put(CACHE_KEY_USER_ACCESS_TOKEN, userAccessToken);
        }
        return userAccessToken;
    }

    @Override
    public void setUserCredentialsProvider(IUserCredentialsProvider credentialsProvider) {
        this.userCredentialsProvider = credentialsProvider;
    }

    @Override
    public void clearCachedTokens() {
        this.serviceSupport.getCache().remove(CACHE_KEY_CLIENT_ACCESS_TOKEN);
        this.serviceSupport.getCache().remove(CACHE_KEY_USER_ACCESS_TOKEN);
    }

    @NonNull
    @Override
    public IAuthService getService() {
        return authService;
    }
}
