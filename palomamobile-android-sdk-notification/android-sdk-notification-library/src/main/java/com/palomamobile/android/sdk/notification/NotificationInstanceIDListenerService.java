package com.palomamobile.android.sdk.notification;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.palomamobile.android.sdk.core.ServiceSupport;

/**
 * Overrides {@link #onTokenRefresh()} to add a new {@link JobUserUpdateGcmRegistrationId} to the job queue as returned by {@link ServiceSupport#getJobManager()}.
 * This has the effect of keeping the Gcm registration id on client and server in sync.
 */
public class NotificationInstanceIDListenerService extends InstanceIDListenerService {

    /**
     * Add a new {@link JobUserUpdateGcmRegistrationId} to the job queue as returned by {@link ServiceSupport#getJobManager()}.
     * This has the effect of keeping the Gcm registration id on client and server in sync.
     */
    @Override
    public void onTokenRefresh() {
        GcmUtils.clearRegistrationId(getApplicationContext());
        ServiceSupport.Instance.getJobManager().addJobInBackground(new JobUserUpdateGcmRegistrationId());
    }
}
