package com.palomamobile.android.sdk.notification;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.palomamobile.android.sdk.core.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GCM (Google Cloud Messaging) utilities.
 */
public class GcmUtils {

    public static final Logger logger = LoggerFactory.getLogger(GcmUtils.class);

    private static final String PROPERTY_REG_ID = "registration_id";

    private GcmUtils(){}

    /**
     * Check if the google play services are available on device.
     * @param context application context
     * @return {@code true} if google play services are available, {@code false} otherwise
     */
    public static boolean checkPlayServices(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        boolean playServicesAvailable = resultCode == ConnectionResult.SUCCESS;
        logger.debug("playServicesAvailable: " + playServicesAvailable);
        return playServicesAvailable;
    }

    public static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = Utilities.getAppVersion(context);
        logger.info("Saving registrationId " + regId + " on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.apply();
    }

    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, null);
        if (registrationId == null) {
            logger.info("getRegistrationId() - registration not found.");
            return null;
        }
        logger.info("getRegistrationId(): " + registrationId);
        return registrationId;
    }

    public static void clearRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        prefs.edit().clear().apply();
    }


    private static SharedPreferences getGcmPreferences(Context context) {
        return context.getSharedPreferences(GcmUtils.class.getSimpleName(), Context.MODE_PRIVATE);
    }

}
