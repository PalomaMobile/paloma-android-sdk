package com.palomamobile.android.sdk.auth;

import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * Username & password user credentials required to retrieve an {@link AccessToken} for a given user, this token must be present in API calls
 * that require {@link AuthType#User}.
 * <br/>
 *
 */
public class PasswordUserCredential extends BaseUserCredential {
    public static final String PWD_CREDENTIAL_TYPE = "password";

    public PasswordUserCredential(@NonNull String username, @NonNull String password) {
        this.username = username;
        credential = new HashMap<>();
        credential.put(PROP_CREDENTIAL_TYPE, PWD_CREDENTIAL_TYPE);
        credential.put("password", password);
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
        return "PasswordUserCredential{} " + super.toString();
    }
}
