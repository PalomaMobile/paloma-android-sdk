package com.palomamobile.android.sdk.notification;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * whenever a {@code GCM registration id} for the client application has been updated on the server.
 * The event contains either the current {@code gcmRegistrationId} on request success or {@code throwable} on
 * request failure.<br/>
 */
public class EventGcmRegistrationIdUpdated extends BaseJobEvent<JobPostUserGcmRegistrationIdUpdate, String> {

    public EventGcmRegistrationIdUpdated(JobPostUserGcmRegistrationIdUpdate job, Throwable failure) {
        super(job, failure);
    }

    public EventGcmRegistrationIdUpdated(JobPostUserGcmRegistrationIdUpdate job, String s) {
        super(job, s);
    }
}
