package com.palomamobile.android.sdk.verification.email;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * whenever a new email address verification is request is completed.
 * The event contains {@code throwable} on request failure.<br/>
 */
public class EventEmailVerificationCreated extends BaseJobEvent<BaseRetryPolicyAwareJob<Void>, Void> {

    public EventEmailVerificationCreated(BaseRetryPolicyAwareJob<Void> job, Throwable failure) {
        super(job, failure);
    }

    public EventEmailVerificationCreated(BaseRetryPolicyAwareJob<Void> job, Void s) {
        super(job, s);
    }
}
