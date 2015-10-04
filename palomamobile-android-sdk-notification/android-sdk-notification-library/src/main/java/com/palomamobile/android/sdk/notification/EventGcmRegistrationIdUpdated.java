package com.palomamobile.android.sdk.notification;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * whenever a {@code GCM registration id} for the client application has been updated on the server.
 * The event contains either the current {@code gcmRegistrationId} on request success or {@code throwable} on
 * request failure.<br/>
 */
public class EventGcmRegistrationIdUpdated extends BaseJobEvent<JobUserUpdateGcmRegistrationId, String> {

    protected EventGcmRegistrationIdUpdated(JobUserUpdateGcmRegistrationId job, Throwable failure) {
        super(job, failure);
    }

    protected EventGcmRegistrationIdUpdated(JobUserUpdateGcmRegistrationId job, String s) {
        super(job, s);
    }
}