package com.palomamobile.android.sdk.notification;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.IJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * whenever a {@code GCM registration id} for the client application has been updated on the server.
 * The event contains either the current {@code gcmRegistrationId} on request success or {@code throwable} on
 * request failure.<br/>
 */
public class EventGcmRegistrationIdUpdated implements IJobEvent<JobUserUpdateGcmRegistrationId, String> {

    private JobUserUpdateGcmRegistrationId job;
    protected String gcmRegistrationId;
    protected Throwable throwable;

    public EventGcmRegistrationIdUpdated(JobUserUpdateGcmRegistrationId job, String gcmRegistrationId) {
        this.job = job;
        this.gcmRegistrationId = gcmRegistrationId;
    }

    public EventGcmRegistrationIdUpdated(JobUserUpdateGcmRegistrationId job, Throwable throwable) {
        this.job = job;
        this.throwable = throwable;
    }

    @Override
    public JobUserUpdateGcmRegistrationId getJob() {
        return job;
    }

    @Override
    public String getSuccess() {
        return gcmRegistrationId;
    }

    @Override
    public Throwable getFailure() {
        return throwable;
    }

    @Override
    public String toString() {
        return "EventGcmRegistrationIdUpdated{" +
                "gcmRegistrationId='" + gcmRegistrationId + '\'' +
                ", job=" + job +
                ", throwable=" + throwable +
                '}';
    }
}
