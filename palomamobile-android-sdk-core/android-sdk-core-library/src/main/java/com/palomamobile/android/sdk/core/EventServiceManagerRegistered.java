package com.palomamobile.android.sdk.core;

import android.support.annotation.NonNull;

/**
 * Event published by when a {@link IServiceManager} is registered by a call to {@link IServiceSupport#registerServiceManager(Class, IServiceManager)}.
 * The order in which Service Managers are instantiated by the SDK is not defined, so other {@link IServiceManager} implementations may listen for this
 * event to implement their dependency requirements.
 * <br/>
 *
 */
public class EventServiceManagerRegistered {
    private @NonNull Class intrface;

    public EventServiceManagerRegistered(@NonNull Class intrface) {
        this.intrface = intrface;
    }

    /**
     * @return interface of the registered {@link IServiceManager}, call IServiceSupport#getServiceManager(Class) to
     * get the IServiceManager implementation
     * @see IServiceSupport#registerServiceManager(Class, IServiceManager)
     */
    @NonNull
    public Class getIntrface() {
        return intrface;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventServiceManagerRegistered{");
        sb.append("intrface=").append(intrface);
        sb.append('}');
        return sb.toString();
    }
}
