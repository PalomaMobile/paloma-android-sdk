package com.palomamobile.android.sdk.verification;

import com.palomamobile.android.sdk.core.qos.BaseJobEvent;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Created by Karel Herink
 */
public class EventEmailVerificationCreated extends BaseJobEvent<BaseRetryPolicyAwareJob<Void>, Void> {

    protected EventEmailVerificationCreated(BaseRetryPolicyAwareJob<Void> job, Throwable failure) {
        super(job, failure);
    }

    protected EventEmailVerificationCreated(BaseRetryPolicyAwareJob<Void> job, Void s) {
        super(job, s);
    }
}
