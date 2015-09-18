package com.palomamobile.android.sdk.auth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Username & password user credentials required to retrieve an {@link AccessToken} for a given user, this token must be present in API calls
 * that require {@link AuthType#User}.
 * <br/>
 *
 */
public class PasswordUserCredential implements IUserCredential {
    public static final String PWD_CREDENTIAL_TYPE = "password";

    @Nullable private String username;
    private Map<String, String> credential;

    public PasswordUserCredential(@NonNull String username, @NonNull String password) {
        this.username = username;
        credential = new HashMap<>();
        credential.put(PROP_CREDENTIAL_TYPE, PWD_CREDENTIAL_TYPE);
        credential.put("password", password);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String userName) {
        this.username = userName;
    }

    @Override
    public String getUserPassword() {
        return credential.get("password");
    }

    @Override
    public String getCredentialType() {
        return PWD_CREDENTIAL_TYPE;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PasswordUserCredential{");
        sb.append("username='").append(username).append('\'');
        sb.append(", credential=").append(credential);
        sb.append('}');
        return sb.toString();
    }
}
