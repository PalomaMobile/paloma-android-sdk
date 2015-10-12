package com.palomamobile.android.sdk.core;

import de.greenrobot.event.EventBus;

public class GreenRobotEventBusAdapter implements IEventBus {

    private EventBus eventBus;

    public GreenRobotEventBusAdapter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void register(Object subscriber) {
        eventBus.register(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        eventBus.unregister(subscriber);
    }

    @Override
    public void post(Object event) {
        eventBus.post(event);
    }

}
