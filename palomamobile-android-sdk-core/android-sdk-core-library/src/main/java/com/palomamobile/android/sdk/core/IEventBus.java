package com.palomamobile.android.sdk.core;

/**
 * Created by Karel Herink
 */
public interface IEventBus {

    void register(Object subscriber);

    void unregister(Object subscriber);

    void post(Object event);
}
