package com.palomamobile.android.sdk.core;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.core.qos.IRetryPolicyProvider;
import com.path.android.jobqueue.JobManager;
import com.squareup.okhttp.OkHttpClient;
import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

/**
 * Defines the contract of the central SDK singleton class.
 *
 * <br/>
 */
public interface IServiceSupport {

    /**
     * Method only of interest to developers looking to modify or extend SDK functionality.
     * @return {@code EventBus} instance that propagates events related to SDK infrastructure
     * such as {@link EventServiceManagerRegistered}.
     */
    EventBus getInternalEventBus();

    /**
     * @return cache implementation
     */
    ICache getCache();

    /**
     * @return rest adapter used to connect to server APIs
     */
    RestAdapter getRestAdapter();

    /**
     * @return http client used by the rest adapter to provide connectivity
     * @see #getRestAdapter()
     */
    OkHttpClient getOkHttpClient();

    /**
     * @return application context
     */
    Context getContext();

    /**
     * @return job manager
     */
    JobManager getJobManager();


    /**
     * Registers a new {@link IServiceManager} instance with the SDK. Once the registration is complete a {@link EventServiceManagerRegistered}
     * is posted to the {@link EventBus} as returned by {@link #getInternalEventBus()} this event can be used to implement dependency management
     * among various service managers. For an example see the code in {@code com.palomamobile.android.sdk.notification.NotificationManager}
     * as it implements its dependency on {@code com.palomamobile.android.sdk.user.IUserManager} implementation.
     * After registering this way the service manager is globally available via the {@link #getServiceManager(Class)} method.
     * @param intrface contract interface implemented by the serviceManager
     * @param serviceManager instance of the service manager
     * @param <T>
     */
    <T extends IServiceManager> void registerServiceManager(@NonNull Class<T> intrface, @NonNull T serviceManager);

    /**
     * Returns a service manager instance that was previously registered via {@link #registerServiceManager(Class, IServiceManager)}
     * @param intrface
     * @param <T>
     * @return service manager instance
     */
    <T extends IServiceManager> T getServiceManager(@NonNull Class<T> intrface);

    /**
     * @return event bus used to propagate SDK related {@link IEvent}s
     */
    IEventBus getEventBus();

    /**
     * @return retry policy provider. Controls the global retry policy, per job retry policy can be implemented by overriding
     * {@link BaseRetryPolicyAwareJob#shouldReRunOnThrowable(Throwable, int, int)}
     */
    IRetryPolicyProvider getRetryPolicyProvider();

    /**
     * Set global retry policy policy provider. Per job retry policy can be implemented by overriding
     * {@link BaseRetryPolicyAwareJob#shouldReRunOnThrowable(Throwable, int, int)}
     * @param retryPolicyProvider
     */
    void setRetryPolicyProvider(IRetryPolicyProvider retryPolicyProvider);

    /**
     * @return uri endpoint of the API services, as configured in the AndroidManifest.xml metadata element.
     */
    Uri getEndpoint();
}
