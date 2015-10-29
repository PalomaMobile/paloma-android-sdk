package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * This interface provides access
 * to the underlying {@link IFriendService} functionality. App developers can either use {@link BaseRetryPolicyAwareJob}
 * implementations in this package or create custom jobs that invoke
 * methods of the {@link IFriendService} returned by {@link IServiceManager#getService()}
 *
 * <br/>
 * To get a concrete implementation of this interface call
 * {@code ServiceSupport.Instance.getServiceManager(IFriendManager.class)}
 * <br/>
 *
 * <br/>
 *
 */
public interface IFriendManager extends IServiceManager<IFriendService> {
    //no helper methods to create complicated job instances required
}
