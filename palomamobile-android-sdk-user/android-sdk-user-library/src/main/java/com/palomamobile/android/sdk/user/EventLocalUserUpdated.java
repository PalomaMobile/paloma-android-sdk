package com.palomamobile.android.sdk.user;

import com.palomamobile.android.sdk.auth.IUserCredential;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * whenever the {@code localUser} is created or updated (e.g. during user registration).
 * The event contains either the {@code localUser} on success or {@code throwable} on
 * failure.<br/>
 * To register a local {@code user} call {@link IUserManager#createJobRegisterUser(IUserCredential)} or one of the related convenience methods.
 * <br/>
 *
 */
public class EventLocalUserUpdated extends BaseJobEvent<BaseRetryPolicyAwareJob<User>, User> {

    protected EventLocalUserUpdated(BaseRetryPolicyAwareJob<User> job, Throwable failure) {
        super(job, failure);
    }

    protected EventLocalUserUpdated(BaseRetryPolicyAwareJob<User> job, User user) {
        super(job, user);
    }
}
