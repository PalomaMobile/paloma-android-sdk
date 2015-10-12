package com.palomamobile.android.sdk.core;

import com.squareup.otto.Bus;

public class OttoEventBusAdapter implements IEventBus {

    private Bus bus;

    public OttoEventBusAdapter(Bus bus) {
        this.bus = bus;
    }

    @Override
    public void register(Object subscriber) {
        bus.register(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        bus.unregister(subscriber);
    }

    @Override
    public void post(Object event) {
        bus.post(event);
    }
}
