package com.palomamobile.android.sdk.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.palomamobile.android.sdk.core.ICache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

/**
 * Simple, not very scalable, implementation of {@link ICache} interface. It can be used directly by invoking the {@link #put(String, Object)}
 * Cache uses {@link Gson} as serialization mechanism and {@link SharedPreferences} for storage, in terms of performance this is fine for
 * caching tokens and other small bits and bobs but likely not much more than that.
 * <br/>
 */
public class SimpleGsonPrefsCache implements ICache {

    public static final Logger logger = LoggerFactory.getLogger(SimpleGsonPrefsCache.class);

    private static final String CACHE_PREFS_NAME = "CACHE_PREFS";

    private Context context;
    private Gson gson;

    public SimpleGsonPrefsCache(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public <T> T get(@NonNull String key, @NonNull Class<T> type) {
        logger.debug("get key=" + key + ", type=" + type);
        SharedPreferences prefs = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE);
        String jsonValue = prefs.getString(key, null);
        logger.debug("got: " + jsonValue);
        if (jsonValue != null) {
            return gson.fromJson(jsonValue, type);
        }
        return null;
    }

    public <T> T getTypedCollection(@NonNull String key, @NonNull Type typeOfT) {
        logger.debug("get key=" + key + ", typeOfT=" + typeOfT);
        SharedPreferences prefs = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE);
        String jsonValue = prefs.getString(key, null);
        logger.debug("got: " + jsonValue);
        if (jsonValue != null) {
            return gson.fromJson(jsonValue, typeOfT);
        }
        return null;
    }

    public void put(@NonNull String key, @NonNull Object value) {
        logger.debug("put key=" + key + ", value=" + value);
        SharedPreferences prefs = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(key, gson.toJson(value)).apply();
    }

    public void remove(@NonNull String key) {
        logger.debug("remove key=" + key);
        SharedPreferences prefs = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(key).apply();
    }

    @Override
    public void clear() {
        logger.debug("clear");
        SharedPreferences prefs = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}
