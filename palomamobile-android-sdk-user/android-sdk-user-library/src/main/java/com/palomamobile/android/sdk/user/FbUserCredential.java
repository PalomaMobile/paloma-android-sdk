package com.palomamobile.android.sdk.user;

import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.auth.AuthType;

import java.util.HashMap;

/**
 * Facebook user credentials required to retrieve an access token for a given user, this token must be present in API calls
 * that require {@link AuthType#User}.
 * <br/>
 *
 */
public class FbUserCredential extends BaseUserCredential {
    public static final String FB_CREDENTIAL_TYPE = "facebook";

    /**
     * Construct a {@code FbUserCredential} using Facebook credentials. See https://developers.facebook.com/docs/android/login-with-facebook/
     * <br/>
     * @param fbUserId as returned from com.facebook.AccessToken#getUserId()
     * @param fbAuthToken as returned from com.facebook.AccessToken#getToken()
     */
    public FbUserCredential(@NonNull String fbUserId, @NonNull String fbAuthToken) {
        credential = new HashMap<>();
        credential.put(PROP_CREDENTIAL_TYPE, FB_CREDENTIAL_TYPE);
        credential.put("userId", fbUserId);
        credential.put("accessToken", fbAuthToken);
    }

    /**
     * Set optional username for paloma platform, if {@code null} user name as returned by facebook is used
     * @param userName
     */
    @Override
    public void setUsername(String userName) {
        super.setUsername(userName);
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
        return "FbUserCredential{} " + super.toString();
    }
}
