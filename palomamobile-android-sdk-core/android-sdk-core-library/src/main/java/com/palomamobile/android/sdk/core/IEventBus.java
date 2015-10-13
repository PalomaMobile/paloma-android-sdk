package com.palomamobile.android.sdk.core;

/**
 * Implement this interface to prove own event bus adapter.
 */
public interface IEventBus {

    /**
     * Register as a subscriber to receive Events from the event bus.
     *
     * @param subscriber will receive events
     */
    void register(Object subscriber);

    /**
     * Unregister as a subscriber from the event bus to no longer receive events.
     *
     * @param subscriber will no longer receive events
     */
    void unregister(Object subscriber);

    /**
     * Post an event to the bus for propagation to registered subscribers.
     *
     * @param event to be propagate to subscribers
     */
    void post(Object event);
}
