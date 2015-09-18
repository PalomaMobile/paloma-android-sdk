package com.palomamobile.android.sdk.core;

/**
 * Implement this interface to enable caching of specific objects.
 * <br/>
 *
 */
public interface ICacheable {

    /**
     * @return value to be cached
     */
    Object getCacheValue();

    /**
     * @return cache key to identify the value next time we wish to retrieve it
     */
    String getCacheKey();

}
