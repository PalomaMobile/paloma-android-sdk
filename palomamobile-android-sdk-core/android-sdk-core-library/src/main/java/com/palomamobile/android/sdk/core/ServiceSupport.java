package com.palomamobile.android.sdk.core;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.palomamobile.android.sdk.core.qos.IRetryPolicyProvider;
import com.palomamobile.android.sdk.core.util.Utilities;
import com.path.android.jobqueue.JobManager;
import com.squareup.okhttp.OkHttpClient;
import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Entry point to the SDK. Call {@link #init(Context)} or {@link ServiceSupport#init(ServiceSupportConfiguration)}
 * to initialize, prior to calling any other methods, a good place to do this is from
 * within {@link Application#onCreate()}. Unless one of the {@code }init(..)}
 * methods is called first all other methods will throw an {@link IllegalStateException}
 * It is possible to customize most of the behaviour of this class by providing custom
 * implementations of key components via the {@link #init(ServiceSupportConfiguration)} call.
 * <br/>
 *
 */
public enum ServiceSupport implements IServiceSupport {

    Instance;

    public static final String TAG = ServiceSupport.class.getSimpleName();
    public static final String CONFIG_NAME_ENDPOINT = "com.palomamobile.android.sdk.Endpoint";

    private Map<Class<?>, Object> serviceManagerMap;

    private Context context;
    private ICache cache;
    private RestAdapter restAdapter;
    private OkHttpClient okHttpClient;
    private JobManager jobManager;
    private EventBus eventBus;
    private IRetryPolicyProvider retryPolicyProvider;
    private Uri endpoint;


    public void init(Context context) {
        init(new ServiceSupportConfiguration(context));
    }

    /**
     * Initializes internal state of the SDK. This or the {@link #init(Context)} must be called before any other methods.
     * @param configuration
     */
    public void init(ServiceSupportConfiguration configuration) {
        //start customizable
        this.context = configuration.getContext();
        this.eventBus = configuration.getEventBus();
        this.cache = configuration.getCache();
        this.retryPolicyProvider = configuration.getRetryPolicyProvider();
        this.jobManager = new JobManager(context, configuration.getJobManagerBuilder().build());
        this.okHttpClient = configuration.getOkHttpClient();

        //end customizable

        okHttpClient.networkInterceptors().add(new CustomHeadersInterceptor(context));

        //configured via AppMetadata in the AndroidManifest.xml
        String endpointValueFromAppMetadata = Utilities.getValueFromAppMetadata(context, CONFIG_NAME_ENDPOINT);
        this.endpoint = Uri.parse(endpointValueFromAppMetadata);
        this.serviceManagerMap = new HashMap<>();

        this.restAdapter = configuration.getRestAdapterBuilder()
                .setEndpoint(endpointValueFromAppMetadata)
                .setClient(new OkClient(okHttpClient))
                .build();

        this.instantiateDeclaredServiceManagers();
    }

    /**
     * @return the cache implementation. Returned value can be customized at time of initialization via
     * {@link ServiceSupport#init(ServiceSupportConfiguration)}
     * @see IServiceSupport#getCache()
     */
    @Override
    public ICache getCache() {
        initCheck();
        return cache;
    }

    /**
     * @return the RestAdapter. Returned value can be customized at time of initialization via
     * {@link ServiceSupport#init(ServiceSupportConfiguration)}
     * @see IServiceSupport#getRestAdapter()
     */
    @Override
    public RestAdapter getRestAdapter() {
        initCheck();
        return restAdapter;
    }

    /**
     * @return http client. Returned value can be customized at time of initialization via
     * {@link ServiceSupport#init(ServiceSupportConfiguration)}
     * @see IServiceSupport#getOkHttpClient()
     */
    @Override
    public OkHttpClient getOkHttpClient() {
        initCheck();
        return okHttpClient;
    }

    @Override
    public Context getContext() {
        initCheck();
        return context;
    }

    /**
     * @return http JobManager. Returned value can be customized at time of initialization via
     * {@link ServiceSupport#init(ServiceSupportConfiguration)}
     * @see IServiceSupport#getJobManager()
     */
    @Override
    public JobManager getJobManager() {
        initCheck();
        return jobManager;
    }

    @Override
    public <T extends IServiceManager> void registerServiceManager(@NonNull Class<T> intrface, @NonNull T serviceManager) {
        initCheck();
        if (!intrface.isInterface()) {
            throw new IllegalArgumentException();
        }
        serviceManagerMap.put(intrface, serviceManager);
        eventBus.post(new EventServiceManagerRegistered(intrface));
    }

    @Override
    public <T extends IServiceManager> T getServiceManager(@NonNull Class<T> intrface) {
        initCheck();
        Object implementation = serviceManagerMap.get(intrface);
        if (implementation == null) {
            Log.i(TAG, "IServiceManager instance not yet registered for " + intrface);
            return null;
        }
        T t = intrface.cast(implementation);
        return t;
    }

    /**
     * @return EventBus used to propagate SDK related events. Returned value can be customized at time of initialization via
     * {@link ServiceSupport#init(ServiceSupportConfiguration)}
     * @see IServiceSupport#getEventBus()
     */
    @Override
    public EventBus getEventBus() {
        initCheck();
        return eventBus;
    }

    private void initCheck() {
        if (context == null) {
            throw new IllegalStateException("ServiceSupport.Instance.init(..) must be called before any other methods.");
        }
    }

    /**
     * Calls the no-arg constructors to create instances of all service managers registered via the
     * AndroidManifest.xml application metadata elements.
     */
    protected void instantiateDeclaredServiceManagers() {
        initCheck();
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            for (String key : bundle.keySet()) {
                if (key.startsWith("com.palomamobile.android.sdk.manager-class")) {
                    String className = bundle.getString(key);
                    Log.i(TAG, "found declared service manager " + key + " : " + className);
                    try {
                        Class.forName(className).getConstructor(IServiceSupport.class).newInstance(this);
                    } catch (Exception e) {
                        Log.e(TAG, "unable to instantiate service manager " + ServiceSupportConfiguration.class + " : " + className, e);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Failed to load meta-data, NameNotFound", e);
        }
    }

    @Override
    public IRetryPolicyProvider getRetryPolicyProvider() {
        initCheck();
        return retryPolicyProvider;
    }

    @Override
    public void setRetryPolicyProvider(IRetryPolicyProvider retryPolicyProvider) {
        this.retryPolicyProvider = retryPolicyProvider;
    }

    @Override
    public Uri getEndpoint() {
        initCheck();
        return endpoint;
    }


}
