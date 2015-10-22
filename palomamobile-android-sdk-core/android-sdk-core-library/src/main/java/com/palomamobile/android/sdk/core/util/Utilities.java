package com.palomamobile.android.sdk.core.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;
import com.palomamobile.android.sdk.core.R;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Assorted utilities.
 * <br/>
 *
 */
public class Utilities {

    public static final String CONFIG_NAME_CLIENT_ID = "com.palomamobile.android.sdk.ClientId";

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String toString(RetrofitError error) {
        final StringBuilder sb = new StringBuilder("RetrofitError{");
        sb.append("url=").append(error.getUrl());
        sb.append(", kind='").append(error.getKind()).append('\'');
        Response response = error.getResponse();
        if (response == null) {
            sb.append(", response='null'");
        }
        else {
            sb.append(", response= {'").append(response).append('\'');
            sb.append(", status='").append(response.getStatus()).append('\'');
            sb.append(", reason='").append(response.getReason()).append('\'');
            sb.append(", body='").append(response.getBody()).append('\'');
            sb.append(", headers='").append(response.getHeaders()).append('\'');
            sb.append('}');
        }
        sb.append('}');
        return sb.toString();
    }

    public static String getDeviceId(Context context) {
        //despite the infamous http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id
        //for our purposes until proven wrong we go with Secure.ANDROID_ID (no additional permissions, simple code etc.)
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceDisplaySizeDescription(Context context) {
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

        //width and height are returned interchangeably depending on the current physical orientation of the device so
        //we manually always return <bigger>x<smaller>
        return width >= height ? (width + "x" + height) : (height + "x" + width);
    }


    public static String getAppNameFromMetadata(Context context) {
        String clientId = getClientIdFromMetadata(context);
        return clientId.substring(0, clientId.indexOf('-'));
    }

    public static String getClientIdFromMetadata(Context context) {
        return getValueFromAppMetadata(context, CONFIG_NAME_CLIENT_ID);
    }

    public static String getValueFromAppMetadata(Context context, String name) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String value = bundle.getString(name);
            if (value == null || value.length() == 0) {
                throw new RuntimeException(context.getString(R.string.err_app_metadata, name));
            }
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Failed to load meta-data, NameNotFound", e);
        }
    }

}
