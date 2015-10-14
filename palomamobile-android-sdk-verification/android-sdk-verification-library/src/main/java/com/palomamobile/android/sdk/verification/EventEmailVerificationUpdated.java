package com.palomamobile.android.sdk.verification;

import com.palomamobile.android.sdk.core.qos.BaseJobEvent;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Created by Karel Herink
 */
public class EventEmailVerificationUpdated extends BaseJobEvent<BaseRetryPolicyAwareJob<Void>, Void> {

    protected EventEmailVerificationUpdated(BaseRetryPolicyAwareJob<Void> job, Throwable failure) {
        super(job, failure);
    }

    protected EventEmailVerificationUpdated(BaseRetryPolicyAwareJob<Void> job, Void aVoid) {
        super(job, aVoid);
    }
}
