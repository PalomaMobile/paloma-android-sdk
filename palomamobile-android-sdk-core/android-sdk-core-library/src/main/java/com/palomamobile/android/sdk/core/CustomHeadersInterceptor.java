package com.palomamobile.android.sdk.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Locale;

/**
 *
 */
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

        return chain.proceed(requestWithUserAgent);
    }

    private static String getPalomaDeviceHeaderValue(Context context) {
        StringBuilder value = new StringBuilder();

        //despite the infamous http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id
        //for our purposes until proven wrong we go with Secure.ANDROID_ID (no additional permissions, simple code etc.)
        value.append(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)).append(",")
                .append(Locale.getDefault().getLanguage()).append(",")
                .append(getDeviceDisplaySizeDescription(context));

        Log.d(TAG, HEADER_NAME_PALOMA_DEVICE + " : " + value);

        return value.toString();
    }

    private static String getDeviceDisplaySizeDescription(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        //this is available in API 13+
        // Point size = new Point();
        // display.getSize(size);
        // int width = size.x;
        // int height = size.y;

        //but we support 9+
        int width = display.getWidth();
        int height = display.getHeight();

        return width + "x" + height;
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
