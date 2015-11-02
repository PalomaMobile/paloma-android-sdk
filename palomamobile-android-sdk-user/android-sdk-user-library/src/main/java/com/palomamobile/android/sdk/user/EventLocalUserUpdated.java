package com.palomamobile.android.sdk.user;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * whenever the local {@code user} is created or updated (e.g. during user registration, login, or update).
 * The event contains either the {@code user} on success or {@code throwable} on
 * failure.<br/>
 * To register a local {@code user} call {@link JobRegisterUser}, to login use {@link JobLoginUser}.
 * <br/>
 */
public class EventLocalUserUpdated extends BaseJobEvent<BaseRetryPolicyAwareJob<User>, User> {

    public EventLocalUserUpdated(BaseRetryPolicyAwareJob<User> job, Throwable failure) {
        super(job, failure);
    }

    public EventLocalUserUpdated(BaseRetryPolicyAwareJob<User> job, User user) {
        super(job, user);
    }
}
