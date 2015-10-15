package com.palomamobile.android.sdk.verification;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * whenever email address verification updated request is completed.
 * The event contains {@code throwable} on request failure.<br/>
 */
public class EventEmailVerificationUpdated extends BaseJobEvent<BaseRetryPolicyAwareJob<Void>, Void> {

    protected EventEmailVerificationUpdated(BaseRetryPolicyAwareJob<Void> job, Throwable failure) {
        super(job, failure);
    }

    protected EventEmailVerificationUpdated(BaseRetryPolicyAwareJob<Void> job, Void aVoid) {
        super(job, aVoid);
    }
}
