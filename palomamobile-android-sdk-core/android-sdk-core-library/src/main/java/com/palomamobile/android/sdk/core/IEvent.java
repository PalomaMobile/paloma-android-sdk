package com.palomamobile.android.sdk.core;

/**
 * Event is posted on the {@link de.greenrobot.event.EventBus} in order to notify interested listeners
 * of changes in SDK state or data availability.
 *
 */
public interface IEvent<Result> {

    /**
     * If event is the result of a successful operation this method returns the {@code result}.
     * If event is the result of a failed operation this method returns {@code null}.
     * @return result if successful, {@code null} if failed
     */
    Result getSuccess();

    /**
     * If event is the result of a failed operation this method returns the {@code Throwable} that caused the failure.
     * If event is the result of a successful operation this method returns {@code null}.
     * @return reason for failure, {@code null} if successful
     */
    Throwable getFailure();

}
