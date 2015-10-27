package com.palomamobile.android.sdk.notification;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.user.IUserManager;
import com.path.android.jobqueue.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience wrapper around {@link INotificationService#postEchoNotification(String, Long, Notification)}
 * Once this job is completed (with success or failure) it posts {@link EventEchoNotificationRequested} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * When the actual {@link Notification} is delivered (as a result of this request) a {@link EventNotificationReceived}
 * is posted on the event bus.
 * </br>
 */
public class JobPostEchoNotification extends BaseRetryPolicyAwareJob<Void> {

    private static final Logger logger = LoggerFactory.getLogger(JobPostEchoNotification.class);
    private Notification echo;


    /**
     * Create a new job to request an "echo" notification for testing purposes.
     * @param echo notification back to client
     */
    public JobPostEchoNotification(Notification echo) {
        this(new Params(0).requireNetwork(), echo);
    }

    /**
     * Create a new job to request an "echo" notification for testing purposes.
     * @param params custom job params
     * @param echo notification back to client
     */
    public JobPostEchoNotification(Params params, Notification echo) {
        super(params);
        this.echo = echo;
    }

    @Override
    public Void syncRun(boolean postEvent) throws Throwable {
        logger.debug("posting echo notification: " + echo);
        IUserManager userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);
        NotificationManager notificationManager = (NotificationManager) ServiceSupport.Instance.getServiceManager(INotificationManager.class);
        notificationManager.getService().postEchoNotification(getRetryId(), userManager.getUser().getId(), echo);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventEchoNotificationRequested(this, echo));
        }
        return null;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventEchoNotificationRequested(this, throwable));
    }

}
