package com.palomamobile.android.sdk.user;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

/**
 * Event published on the {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * whenever the {@code JobResetPassword} execution is completed.
 * The event contains either a {@code User} on success or {@code throwable} on
 * failure.<br/>
 * To request a user password reset use {@link JobResetPassword}.
 * <br/>
 */
public class EventPasswordResetCompleted extends BaseJobEvent<JobResetPassword, User> {

    protected EventPasswordResetCompleted(JobResetPassword job, Throwable failure) {
        super(job, failure);
    }

    protected EventPasswordResetCompleted(JobResetPassword job, User user) {
        super(job, user);
    }

    /**
     * @return {@code true} if the {@link JobResetPassword} instance that posted this event is
     * configured to schedule a {@link JobLoginUser} in case the password reset is successful.
     * Same as calling {@link #getJob()#isTriggerLogin()}
     */
    public boolean isTriggerLogin() {
        return getJob().isTriggerLogin();
    }
}
