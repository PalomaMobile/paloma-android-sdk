package com.palomamobile.android.sdk.auth;

import android.content.Context;
import android.util.Base64;

/**
 * <br/>
 *
 */
class AuthUtils {

    public static String getBasicAuthHeaderValueBase64(String user, String password) {
        String value = "Basic " + Base64.encodeToString((user + ":" + password).getBytes(), Base64.NO_WRAP);
        return value;
    }

    //Auth token
    public static void setBearerAuthTokenValue(Context context, String authToken) {
        context.getSharedPreferences("Bearer", Context.MODE_PRIVATE)
                .edit()
                .putString("authToken", authToken)
                .commit();
    }

    public static String getBearerAuthHeaderValue(String clientAuthToken) {
        String headerValue = "Bearer " + clientAuthToken;
        return headerValue;
    }

    //Refresh token
    public static void setBearerRefreshTokenValue(Context context, String refreshToken) {
        context.getSharedPreferences("Bearer", Context.MODE_PRIVATE)
                .edit()
                .putString("refreshToken", refreshToken)
                .commit();
    }
}
