package com.palomamobile.android.sdk.notification;

import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * This interface provides access
 * to the underlying {@link INotificationService} functionality. App developers can either use {@link BaseRetryPolicyAwareJob}
 * implementations in this package or create custom jobs that invoke
 * methods of the {@link INotificationService} returned by {@link IServiceManager#getService()}
 *
 * <br/>
 * To get a concrete implementation of this interface call
 * {@code ServiceSupport.Instance.getServiceManager(INotificationManager.class)}
 * <br/>
 *
 * <br/>
 *
 */
public interface INotificationManager extends IServiceManager<INotificationService> {
    //no helper methods to create complicated job instances required
}
