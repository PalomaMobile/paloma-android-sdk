package com.palomamobile.android.sdk.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.gson.Gson;
import com.palomamobile.android.sdk.core.ICacheable;
import com.palomamobile.android.sdk.core.ICache;
import com.palomamobile.android.sdk.core.ServiceSupport;

import java.lang.reflect.Type;

/**
 * Simple, not very scalable, implementation of {@link ICache} interface. It can be used directly by invoking the {@link #put(String, Object)}
 * It also listens to the event bus {@link ServiceSupport#getEventBus()} and caches values from events that implement {@link ICacheable}.
 * Note: cache uses the highest receiver priority so that when another subscriber (unless it also listens with the highest priority)
 * receives an event it can expect the cache to already contain correct values as contained in the event.
 * Cache uses {@link Gson} as serialization mechanism and {@link SharedPreferences} for storage, in terms of performance this is fine for
 * caching tokens and other small bits and bobs but likely not much more than that.
 * <br/>
 *
 */
public class SimpleGsonPrefsCache implements ICache {

    public static final String TAG = SimpleGsonPrefsCache.class.getSimpleName();

    private static final String CACHE_PREFS_NAME = "CACHE_PREFS";

    private Context context;
    private Gson gson;

    public SimpleGsonPrefsCache(Context context) {
        this.context = context;
        this.gson = new Gson();
        ServiceSupport.Instance.getEventBus().register(this, Integer.MAX_VALUE);
    }

    @SuppressWarnings("unused")
    public void onEvent(ICacheable cacheable) {
        put(cacheable.getCacheKey(), cacheable.getCacheValue());
    }

    public <T> T get(@NonNull String key, @NonNull Class<T> type) {
        Log.d(TAG, "get key=" + key + ", type=" + type);
        SharedPreferences prefs = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE);
        String jsonValue = prefs.getString(key, null);
        Log.d(TAG, "got: " + jsonValue);
        if (jsonValue != null) {
            return gson.fromJson(jsonValue, type);
        }
        return null;
    }

    public <T> T getTypedCollection(@NonNull String key, @NonNull Type typeOfT) {
        Log.d(TAG, "get key=" + key + ", typeOfT=" + typeOfT);
        SharedPreferences prefs = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE);
        String jsonValue = prefs.getString(key, null);
        Log.d(TAG, "got: " + jsonValue);
        if (jsonValue != null) {
            return gson.fromJson(jsonValue, typeOfT);
        }
        return null;
    }

    public void put(@NonNull String key, @NonNull Object value) {
        Log.d(TAG, "put key=" + key + ", value=" + value);
        SharedPreferences prefs = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(key, gson.toJson(value)).apply();
    }

    public void remove(@NonNull String key) {
        Log.d(TAG, "remove key=" + key);
        SharedPreferences prefs = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(key).apply();
    }

    @Override
    public void clear() {
        Log.d(TAG, "clear");
        SharedPreferences prefs = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}
