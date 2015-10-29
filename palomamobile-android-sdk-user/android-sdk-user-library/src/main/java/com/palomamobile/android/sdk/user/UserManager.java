package com.palomamobile.android.sdk.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.palomamobile.android.sdk.auth.IAuthManager;
import com.palomamobile.android.sdk.auth.IUserCredential;
import com.palomamobile.android.sdk.core.EventServiceManagerRegistered;
import com.palomamobile.android.sdk.core.IServiceSupport;
import com.palomamobile.android.sdk.core.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UserManager implements IUserManager {

    public static final Logger logger = LoggerFactory.getLogger(UserManager.class);

    static final String CACHE_KEY_LOCAL_USER = "cache_key_local_user";

    private IUserService userService;

    private IUserCredential userCredential;


    public UserManager(IServiceSupport serviceSupport) {
        serviceSupport.getInternalEventBus().register(this);
        serviceSupport.registerServiceManager(IUserManager.class, this);
        IAuthManager authManager = serviceSupport.getServiceManager(IAuthManager.class);
        if (authManager != null) {
            ServiceSupport.Instance.getServiceManager(IAuthManager.class).setUserCredentialsProvider(this);
        }
    }

    @SuppressWarnings("unused")
    public void onEventBackgroundThread(EventServiceManagerRegistered event) {
        logger.debug("onEventBackgroundThread(" + event + ")");
        if (IAuthManager.class == event.getIntrface()) {
            logger.debug("IAuthManager instance available -> setUserCredentialsProvider(this)");
            ServiceSupport.Instance.getServiceManager(IAuthManager.class).setUserCredentialsProvider(this);
        }
    }


    @Override
    public @Nullable User getUser() {
        return ServiceSupport.Instance.getCache().get(CACHE_KEY_LOCAL_USER, User.class);
    }

    @Override
    @NonNull
    public IUserService getService() {
        if (userService == null) {
            this.userService = ServiceSupport.Instance.cloneNonRedirectingRestAdapter().create(IUserService.class);
        }
        return userService;
    }


    void updateLocalUser(User updatedLocalUser) {
        this.userCredential.setUsername(updatedLocalUser.getUsername());
        ServiceSupport.Instance.getCache().put(CACHE_KEY_LOCAL_USER, updatedLocalUser);
    }

    @Override
    public IUserCredential getUserCredential() {
        return userCredential;
    }

    void setUserCredential(IUserCredential userCredential) {
        this.userCredential = userCredential;
    }
}
