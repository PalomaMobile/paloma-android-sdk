package com.palomamobile.android.sdk.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.palomamobile.android.sdk.auth.IUserCredential;
import com.palomamobile.android.sdk.auth.IUserCredentialsProvider;
import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.ServiceSupport;
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

    /**
     * Convenience method constructs the right {@link IUserCredential} instance and calls {@link #createJobRegisterUser(IUserCredential)}
     *
     * @param fbUserId
     * @param fbAuthToken
     */
    JobRegisterUser createJobRegisterUserViaFacebook(@NonNull String fbUserId, @NonNull String fbAuthToken);

    /**
     * Convenience method constructs the right {@link IUserCredential} instance and calls {@link #createJobRegisterUser(IUserCredential)}
     *
     * @param fbUserId
     * @param fbAuthToken
     */
    JobRegisterUser createJobRegisterUserViaFacebook(@Nullable String userName, @NonNull String fbUserId, @NonNull String fbAuthToken);

    /**
     * Convenience method constructs the correct {@link IUserCredential} instance and calls {@link #createJobRegisterUser(IUserCredential)}
     *
     * @param userName
     * @param password
     */
    JobRegisterUser createJobRegisterUserViaPassword(@NonNull String userName, @NonNull String password);

    /**
     * Async request to register a user using the provided {@link IUserCredential}. Credential can represent a new user for sign-up
     * or an existing user for login.<br/>
     * For convenience use either {@link #createJobRegisterUserViaFacebook(String, String)} or {@link #createJobRegisterUserViaPassword(String, String)}
     * When a response is received an {@link EventLocalUserUpdated} is published on the {@link de.greenrobot.event.EventBus}
     * as returned by {@link ServiceSupport#getEventBus()}.
     *
     * @param userCredential
     */
    JobRegisterUser createJobRegisterUser(@NonNull IUserCredential userCredential);

    /**
     * Convenience method constructs the right {@link IUserCredential} instance and calls {@link #createJobLoginUser(IUserCredential)}
     *
     * @param fbUserId
     * @param fbAuthToken
     * @return
     */
    JobLoginUser createJobLoginUserViaFacebook(@NonNull String fbUserId, @NonNull String fbAuthToken);

    /**
     * Convenience method constructs the right {@link IUserCredential} instance and calls {@link #createJobLoginUser(IUserCredential)}
     *
     * @param userName
     * @param password
     * @return
     */
    JobLoginUser createJobLoginUserViaPassword(@NonNull String userName, @NonNull String password);

    /**
     * Async request to login and existing user using the provided {@link IUserCredential}. Credential can only represent
     * an existing user for login or the call results in an throwable.<br/>
     * For convenience use either {@link #createJobLoginUserViaFacebook(String, String)} or {@link #createJobLoginUserViaPassword(String, String)}
     * When a response is received an {@link EventLocalUserUpdated} is published on the {@link de.greenrobot.event.EventBus}
     * as returned by {@link ServiceSupport#getEventBus()}.
     *
     * @param userCredential
     * @return
     */
    JobLoginUser createJobLoginUser(@NonNull IUserCredential userCredential);

}