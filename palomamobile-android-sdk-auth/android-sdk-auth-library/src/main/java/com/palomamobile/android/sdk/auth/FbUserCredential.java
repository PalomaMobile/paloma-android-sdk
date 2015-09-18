package com.palomamobile.android.sdk.auth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Facebook user credentials required to retrieve an {@link AccessToken} for a given user, this token must be present in API calls
 * that require {@link AuthType#User}.
 * <br/>
 *
 */
public class FbUserCredential implements IUserCredential {
    public static final String FB_CREDENTIAL_TYPE = "facebook";

    @Nullable private String username;
    private Map<String, String> credential;

    /**
     * Construct a {@code FbUserCredential} using Facebook credentials. See https://developers.facebook.com/docs/android/login-with-facebook/
     * <br/>
     * @param username optional username for paloma platform, if {@code null} user name as returned by facebook is used
     * @param fbUserId as returned from com.facebook.AccessToken#getUserId()
     * @param fbAuthToken as returned from com.facebook.AccessToken#getToken()
     */
    public FbUserCredential(@Nullable String username, @NonNull String fbUserId, @NonNull String fbAuthToken) {
        this.username = username;
        credential = new HashMap<>();
        credential.put(PROP_CREDENTIAL_TYPE, FB_CREDENTIAL_TYPE);
        credential.put("userId", fbUserId);
        credential.put("accessToken", fbAuthToken);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUserPassword() {
        return credential.get("accessToken");
    }

    @Override
    public String getCredentialType() {
        return FB_CREDENTIAL_TYPE;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FbUserCredential{");
        sb.append("username='").append(username).append('\'');
        sb.append(", credential=").append(credential);
        sb.append('}');
        return sb.toString();
    }
}
