package com.palomamobile.android.sdk.core;

/**
 * Defines a ServiceManager implementation in terms of the API service it manages. {@link IServiceSupport} provides
 * access to the various {@code IServiceManager} implementations via the {@link IServiceSupport#getServiceManager(Class)}
 * and {@link IServiceSupport#registerServiceManager(Class, IServiceManager)} methods.
 *
 * <br/>
 */
public interface IServiceManager<T> {

    /**
     * @return The managed service.
     */
    T getService();
}
