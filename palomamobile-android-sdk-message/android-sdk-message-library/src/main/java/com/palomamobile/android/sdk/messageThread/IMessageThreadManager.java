package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * This interface provides access
 * to the underlying {@link IMessageThreadService} functionality. App developers can either use {@link BaseRetryPolicyAwareJob}
 * implementations in this package or create custom jobs that invoke
 * methods of the {@link IMessageThreadService} returned by {@link IServiceManager#getService()}
 *
 * <br/>
 * To get a concrete implementation of this interface call
 * {@code ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class)}
 * <br/>
 *
 * <br/>
 *
 */
public interface IMessageThreadManager extends IServiceManager<IMessageThreadService> {
    //no helper methods to create complicated job instances required
}
