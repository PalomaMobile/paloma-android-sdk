package com.palomamobile.android.sdk.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.palomamobile.android.sdk.core.util.Utilities;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Locale;

class CustomHeadersInterceptor implements Interceptor {

    private static final String TAG = CustomHeadersInterceptor.class.getSimpleName();

    private static final String HEADER_NAME_USER_AGENT = "User-Agent";
    private static final String HEADER_NAME_PALOMA_DEVICE = "X-Paloma-Device";

    private static final String HEADER_PALOMA_CLIENT_APP_VERSION = "Paloma-Client-App-Version";

    private Context context;

    CustomHeadersInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request requestWithUserAgent = originalRequest.newBuilder()
                .removeHeader(HEADER_NAME_USER_AGENT)
                .addHeader(HEADER_NAME_USER_AGENT, getUserAgentHeaderValue(originalRequest))
                .removeHeader(CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION) //instead placed into "User-Agent"
                .removeHeader(CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION) //instead placed into "User-Agent"
                .removeHeader(HEADER_NAME_PALOMA_DEVICE)
                .addHeader(HEADER_NAME_PALOMA_DEVICE, getPalomaDeviceHeaderValue(context))
                .build();

        String requestId = requestWithUserAgent.header(CustomHeader.HEADER_NAME_PALOMA_REQUEST);
        if (requestId == null) {
            throw new RuntimeException("'" + CustomHeader.HEADER_NAME_PALOMA_REQUEST + "' header must be present on all requests.");
        }

        return chain.proceed(requestWithUserAgent);
    }

    private static String getPalomaDeviceHeaderValue(Context context) {
        String value = Utilities.getDeviceId(context) + "," + Locale.getDefault().getLanguage() + "," + Utilities.getDeviceDisplaySizeDescription(context);
        Log.d(TAG, HEADER_NAME_PALOMA_DEVICE + " : " + value);
        return value;
    }

    private String getUserAgentHeaderValue(Request originalRequest) {
        String targetServiceVersion = originalRequest.header(CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION);
        String sdkVersion = originalRequest.header(CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION);

        if (targetServiceVersion == null || targetServiceVersion.length() == 0) throw new RuntimeException("Missing required header: " + CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION);
        if (sdkVersion == null || sdkVersion.length() == 0) throw new RuntimeException("Missing required header: " + CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION);

        StringBuilder userAgent = new StringBuilder(System.getProperty("http.agent"));
        userAgent.append(" ").append(CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION).append("/").append(targetServiceVersion);
        userAgent.append(" ").append(CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION).append("/").append(sdkVersion);

        userAgent.append(getClientAppVersionValues(context));
        Log.d(TAG, HEADER_NAME_USER_AGENT + " : " + userAgent);
        return userAgent.toString();
    }

    private static String getClientAppVersionValues(Context context) {
        StringBuilder versionValue = new StringBuilder();
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionValue.append(" ").append(HEADER_PALOMA_CLIENT_APP_VERSION).append("/").append(pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Unable to determine application versionCode", e);
        }
        return versionValue.toString();
    }

}
