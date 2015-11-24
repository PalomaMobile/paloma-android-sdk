package com.palomamobile.android.sdk.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.palomamobile.android.sdk.auth.AuthType;

import java.util.HashMap;

/**
 * Username/Email & Password user credentials required to retrieve an access token for a given user, this token must be present in API calls
 * that require {@link AuthType#User}.
 * <br/>
 *
 */
public class PasswordUserCredential extends BaseUserCredential {
    public static final String PWD_CREDENTIAL_TYPE = "password";

    private String emailAddress;
    private String verificationCode;

    /**
     * * Construct a user credential for registration via a username and password.
     * @param username
     * @param password
     */
    public PasswordUserCredential(@NonNull String username, @NonNull String password) {
        setUserPassword(password);
        this.username = username;
    }

    /**
     * Construct a user credential for registration via a verified email address and password.
     * @param verifiedEmail
     * @param password
     */
    public PasswordUserCredential(@NonNull VerifiedEmail verifiedEmail, @NonNull String password) {
        setUserPassword(password);
        setVerifiedEmail(verifiedEmail);
    }

    /**
     * Construct a user credential for registration. At least one of {@code username} or {@code verifiedEmail} must contain a valid value.
     * @param username user name
     * @param verifiedEmail email address verified by the email verification service
     * @param password
     */
    public PasswordUserCredential(@Nullable String username, @Nullable VerifiedEmail verifiedEmail, @NonNull String password) {
        setUserPassword(password);
        this.username = username;
        if (verifiedEmail != null) {
            setVerifiedEmail(verifiedEmail);
        }
    }

    private void setUserPassword(@NonNull String password) {
        credential = new HashMap<>();
        credential.put(PROP_CREDENTIAL_TYPE, PWD_CREDENTIAL_TYPE);
        credential.put("password", password);
    }

    @Override
    public String getUserPassword() {
        return credential.get("password");
    }

    public void setVerifiedEmail(@NonNull VerifiedEmail verifiedEmail) {
        emailAddress = verifiedEmail.getVerifiedEmailAddress();
        verificationCode = verifiedEmail.getVerificationCode();
    }

    @Override
    public String getCredentialType() {
        return PWD_CREDENTIAL_TYPE;
    }

    @Override
    public String toString() {
        return "PasswordUserCredential{} " + super.toString();
    }

    public String getVerifiedEmailAddress() {
        return emailAddress;
    }

    public String getEmailVerificationCode() {
        return verificationCode;
    }
}
