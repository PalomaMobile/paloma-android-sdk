package com.palomamobile.android.sdk.user;

import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.auth.IUserCredential;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.IJobEvent;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * whenever the {@code localUser} is created or updated (e.g. during user registration).
 * The event contains either the {@code localUser} on success or {@code throwable} on
 * failure.<br/>
 * To register a local {@code user} call {@link IUserManager#createJobRegisterUser(IUserCredential)} or one of the related convenience methods.
 * <br/>
 *
 */
public class EventLocalUserUpdated implements IJobEvent<BaseRetryPolicyAwareJob<User>, User> {

    private User localUser;
    private BaseRetryPolicyAwareJob<User> job;
    private Throwable throwable;

    EventLocalUserUpdated(BaseRetryPolicyAwareJob<User> job, @NonNull Throwable throwable) {
        this.job = job;
        this.throwable = throwable;
    }

    EventLocalUserUpdated(BaseRetryPolicyAwareJob<User> job, @NonNull User localUser) {
        this.job = job;
        this.localUser = localUser;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventLocalUserUpdated{");
        sb.append("throwable=").append(throwable);
        sb.append(", localUser=").append(localUser);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public BaseRetryPolicyAwareJob<User> getJob() {
        return job;
    }

    @Override
    public User getSuccess() {
        return localUser;
    }

    @Override
    public Throwable getFailure() {
        return throwable;
    }
}
