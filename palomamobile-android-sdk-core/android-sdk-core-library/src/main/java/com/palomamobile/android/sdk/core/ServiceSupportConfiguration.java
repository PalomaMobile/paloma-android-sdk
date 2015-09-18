package com.palomamobile.android.sdk.core;

import android.content.Context;
import android.util.Log;
import com.palomamobile.android.sdk.core.qos.DefaultRetryPolicyProvider;
import com.palomamobile.android.sdk.core.qos.IRetryPolicyProvider;
import com.palomamobile.android.sdk.core.util.SimpleGsonPrefsCache;
import com.palomamobile.android.sdk.core.util.TruncatedAndroidLog;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;
import com.squareup.okhttp.OkHttpClient;
import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import retrofit.client.Client;

/**
 * Builder allows ServiceSupport initialisation with custom:
 * <ul><li/>{@link EventBus}
 * <li/>{@link Context}
 * <li/>{@link EventBus}
 * <li/>{@link ICache}
 * <li/>{@link IRetryPolicyProvider}
 * <li/>{@link JobManager}
 * <li/>{@link OkHttpClient}
 * <li/>{@link RestAdapter.Builder}
 * </ul>
 * @see ServiceSupport#init(ServiceSupportConfiguration)
 */
public class ServiceSupportConfiguration {

    private Context context;
    private EventBus eventBus;
    private ICache cache;
    private IRetryPolicyProvider retryPolicyProvider;
    private Configuration.Builder jobManagerBuilder;
    private OkHttpClient okHttpClient;
    private RestAdapter.Builder restAdapterBuilder;

    public ServiceSupportConfiguration(Context context) {
        this.context = context;
    }

    /**
     * Optionally set a custom eventBus
     * @param eventBus
     * @return {@code this} for chaining calls
     */
    public ServiceSupportConfiguration setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        return this;
    }

    /**
     * Optionally set a custom cache
     * @param cache
     * @return {@code this} for chaining calls
     */
    public ServiceSupportConfiguration setCache(ICache cache) {
        this.cache = cache;
        return this;
    }

    /**
     * Optionally set a custom  global retryPolicyProvider
     * @param retryPolicyProvider global implementation, can be overridden at job level
     * @return {@code this} for chaining calls
     */
    public ServiceSupportConfiguration setRetryPolicyProvider(IRetryPolicyProvider retryPolicyProvider) {
        this.retryPolicyProvider = retryPolicyProvider;
        return this;
    }

    /**
     * Optionally set a custom job manager builder
     * @param jobManagerBuilder
     * @return {@code this} for chaining calls
     */
    public ServiceSupportConfiguration setJobManagerBuilder(Configuration.Builder jobManagerBuilder) {
        this.jobManagerBuilder = jobManagerBuilder;
        return this;
    }

    /**
     * Optionally set a custom okHttpClient
     * @param okHttpClient
     * @return {@code this} for chaining calls
     */
    public ServiceSupportConfiguration setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        return this;
    }

    /**
     * Optionally set a custom RestAdapter.Builder. Note that the SDK will effectively override
     * any values set by {@link RestAdapter.Builder#setClient(Client)} or {@link RestAdapter.Builder#setEndpoint(String)}
     * by calling
     * {@link RestAdapter.Builder#setClient(Client)} with the value returned from
     * {@link ServiceSupportConfiguration#getOkHttpClient()} and also calling
     * {@link RestAdapter.Builder#setEndpoint(String)} and supplying the endpoint value
     * provided in the AndroidManifest.xml {@code com.palomamobile.android.sdk.Endpoint} metadata element.
     * @param restAdapterBuilder
     * @return this
     */
    public ServiceSupportConfiguration setRestAdapterBuilder(RestAdapter.Builder restAdapterBuilder) {
        this.restAdapterBuilder = restAdapterBuilder;
        return this;
    }

    public Context getContext() {
        return context;
    }

    /**
     * @return the optionally set restAdapterBuilder or a reasonable default value
     */
    public RestAdapter.Builder getRestAdapterBuilder() {
        if (this.restAdapterBuilder == null) {
            restAdapterBuilder = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog(new TruncatedAndroidLog());
        }
        return restAdapterBuilder;
    }

    /**
     * @return the optionally set cache or a {@link SimpleGsonPrefsCache} instance
     */
    public ICache getCache() {
        if (cache == null) {
            cache = new SimpleGsonPrefsCache(context);
        }
        return cache;
    }

    /**
     * @return the optionally set eventBus or a default as returned by {@link EventBus#getDefault()}
     */
    public EventBus getEventBus() {
        if (eventBus == null) {
            eventBus = EventBus.getDefault();
        }
        return eventBus;
    }

    /**
     * @return the optionally set retryPolicyProvider or a {@link DefaultRetryPolicyProvider} instance
     */
    public IRetryPolicyProvider getRetryPolicyProvider() {
        if (retryPolicyProvider == null) {
            retryPolicyProvider = new DefaultRetryPolicyProvider();
        }
        return retryPolicyProvider;
    }

    /**
     * @return the optionally set OkHttpClient or a {@link OkHttpClient} instance
     */
    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }


    /**
     * @return the optionally set job manager configuration builder or a {@link JobManager} instance configured with a custom debug logger
     */
    public Configuration.Builder getJobManagerBuilder() {
        if (jobManagerBuilder == null) {
            jobManagerBuilder = new Configuration.Builder(context).customLogger(new CustomLogger() {
                @Override
                public boolean isDebugEnabled() {
                    return true;
                }

                @Override
                public void d(String text, Object... args) {
                    Log.d("JobManager", String.format(text, args));
                }

                @Override
                public void e(Throwable t, String text, Object... args) {
                    Log.e("JobManager", String.format(text, args), t);
                }

                @Override
                public void e(String text, Object... args) {
                    Log.e("JobManager", String.format(text, args));
                }
            });
        }
        return jobManagerBuilder;
    }
}
