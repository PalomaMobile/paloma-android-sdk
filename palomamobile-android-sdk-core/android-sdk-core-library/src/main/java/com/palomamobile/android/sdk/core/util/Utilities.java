package com.palomamobile.android.sdk.core.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import retrofit.RetrofitError;
import retrofit.client.Response;
import com.palomamobile.android.sdk.core.R;

/**
 * Assorted utilities.
 * <br/>
 *
 */
public class Utilities {

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
