package com.palomamobile.android.sdk.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Utility class mostly used in tests. Allows us to mimic synchronous calls while executing a job asynchronously to test
 * that the appropriate events are fired. Register an instance on this class on the appropriate event bus before calling
 * {@link #await(long, TimeUnit)}
 * <br/>
 *
 */
public class LatchedBusListener<T> {

    private static final Logger logger = LoggerFactory.getLogger(LatchedBusListener.class);

    private CountDownLatch countDownLatch;
    private T event;
    private Class<T> clazz;

    /**
     * Create a new instance.
     * @param clazz class of the event the listener will wait for
     */
    public LatchedBusListener(Class<T> clazz) {
        this.clazz = clazz;
        this.countDownLatch = new CountDownLatch(1);
    }

    @SuppressWarnings("unused")
    public void onEventBackgroundThread(T t) {
        logger.debug("onEventBackgroundThread() : " + t);
        if (clazz.isAssignableFrom(t.getClass())) {
            logger.debug("that works for us : " + t);
            this.event = t;
            this.countDownLatch.countDown();
        }
    }

    public T getEvent() {
        return event;
    }

    /**
     * @return class of the event that will cause the CountDownLatch to count down once received on the event bus
     */
    public Class<T> getClazz() {
        return clazz;
    }

    /**
     * Delegates to {@link CountDownLatch#await(long, TimeUnit)}. The latch will count down once an event of type
     * returned by {@link #getClazz()} is received on the event bus
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the {@code timeout} argument
     * @return {@code true} if the count reached zero and {@code false}
     *         if the waiting time elapsed before the count reached zero
     * @throws InterruptedException if the current thread is interrupted
     *         while waiting
     * @see CountDownLatch
     */
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return countDownLatch.await(timeout, unit);
    }
}
