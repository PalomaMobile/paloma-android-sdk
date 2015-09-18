package com.palomamobile.android.sdk.core;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;

/**
 * Defines the contract of a simple cache.
 * <br/>
 *
 */
public interface ICache {

    /**
     * get value for the given key
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    <T> T get(@NonNull String key, @NonNull Class<T> type);

    /**
     * get a collection for the given key
     * @param key
     * @param typeOfT
     * @param <T>
     * @return
     */
    <T> T getTypedCollection(@NonNull String key, @NonNull Type typeOfT);

    /**
     * assign a value to the given key
     * @param key
     * @param value
     */
    void put(@NonNull String key, @NonNull Object value);

    /**
     * remove the value for a given key
     * @param key
     */
    void remove(@NonNull String key);

    /**
     * remove all cached entries
     */
    void clear();

}
