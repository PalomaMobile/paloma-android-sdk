package com.palomamobile.android.sdk.user;

import android.support.annotation.Nullable;
import com.palomamobile.android.sdk.auth.IUserCredentialsProvider;
import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Methods in this interface provide convenient job creation methods that provide easy access
 * to the underlying {@link IUserService} functionality. App developers can either use {@link BaseRetryPolicyAwareJob}
 * job instances returned by the {@code createJob...()} methods, or create custom jobs that invoke
 * methods of the {@link IUserService} returned by {@link IServiceManager#getService()}
 *
 * <br/>
 * To get a concrete implementation of this interface call
 * {@code ServiceSupport.Instance.getServiceManager(IUserManager.class)}
 * <br/>
 *
 * <br/>
 *
 */
public interface IUserManager extends IUserCredentialsProvider, IServiceManager<IUserService> {

    /**
     * Synchronous call returns the current {@link User} from local cache.
     *
     * @return current user
     */
    @Nullable User getUser();

}
