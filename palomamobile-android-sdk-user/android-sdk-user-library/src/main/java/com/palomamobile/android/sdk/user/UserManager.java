package com.palomamobile.android.sdk.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.palomamobile.android.sdk.auth.FbUserCredential;
import com.palomamobile.android.sdk.auth.IAuthManager;
import com.palomamobile.android.sdk.auth.IUserCredential;
import com.palomamobile.android.sdk.auth.PasswordUserCredential;
import com.palomamobile.android.sdk.core.EventServiceManagerRegistered;
import com.palomamobile.android.sdk.core.IServiceSupport;
import com.palomamobile.android.sdk.core.ServiceSupport;

/**
 *
 */
class UserManager implements IUserManager {

    public static final String TAG = UserManager.class.getSimpleName();

    static final String CACHE_KEY_LOCAL_USER = "cache_key_local_user";

    private IUserService userService;

    private IUserCredential userCredential;

    private User localUser;

    public UserManager(IServiceSupport serviceSupport) {
        this.userService = serviceSupport.getRestAdapter().create(IUserService.class);
        serviceSupport.getEventBus().register(this);
        serviceSupport.registerServiceManager(IUserManager.class, this);
        IAuthManager authManager = serviceSupport.getServiceManager(IAuthManager.class);
        if (authManager != null) {
            ServiceSupport.Instance.getServiceManager(IAuthManager.class).setUserCredentialsProvider(this);
        }
    }

    @SuppressWarnings("unused")
    public void onEventBackgroundThread(EventServiceManagerRegistered event) {
        Log.d(TAG, "onEventBackgroundThread(" + event + ")");
        if (IAuthManager.class == event.getIntrface()) {
            Log.d(TAG, "IAuthManager instance available -> setUserCredentialsProvider(this)");
            ServiceSupport.Instance.getServiceManager(IAuthManager.class).setUserCredentialsProvider(this);
        }
    }


    @Override
    public @Nullable User getUser() {
        return ServiceSupport.Instance.getCache().get(CACHE_KEY_LOCAL_USER, User.class);
    }

    public JobRegisterUser createJobRegisterUserViaFacebook(@NonNull String fbUserId, @NonNull String fbAuthToken) {
        return createJobRegisterUserViaFacebook(null, fbUserId, fbAuthToken);
    }

    public JobRegisterUser createJobRegisterUserViaFacebook(@Nullable String userName, @NonNull String fbUserId, @NonNull String fbAuthToken) {
        FbUserCredential facebookCredential = new FbUserCredential(userName, fbUserId, fbAuthToken);
        return createJobRegisterUser(facebookCredential);
    }

    public JobRegisterUser createJobRegisterUserViaPassword(@NonNull String userName, @NonNull String password) {
        PasswordUserCredential passwordUserCredential = new PasswordUserCredential(userName, password);
        return createJobRegisterUser(passwordUserCredential);
    }

    public JobRegisterUser createJobRegisterUser(@NonNull IUserCredential userCredential) {
        this.userCredential = userCredential;
        return new JobRegisterUser(userCredential);
    }

    public JobLoginUser createJobLoginUserViaFacebook(@NonNull String fbUserId, @NonNull String fbAuthToken) {
        return createJobLoginUserViaFacebook(null, fbUserId, fbAuthToken);
    }

    public JobLoginUser createJobLoginUserViaFacebook(@Nullable String userName, @NonNull String fbUserId, @NonNull String fbAuthToken) {
        FbUserCredential facebookCredential = new FbUserCredential(userName, fbUserId, fbAuthToken);
        return createJobLoginUser(facebookCredential);
    }

    public JobLoginUser createJobLoginUserViaPassword(@NonNull String userName, @NonNull String password) {
        PasswordUserCredential passwordUserCredential = new PasswordUserCredential(userName, password);
        return createJobLoginUser(passwordUserCredential);
    }

    public JobLoginUser createJobLoginUser(@NonNull IUserCredential userCredential) {
        this.userCredential = userCredential;
        return new JobLoginUser(userCredential);
    }

    @Override
    @NonNull
    public IUserService getService() {
        return userService;
    }


    void updateLocalUser(User updatedLocalUser) {
        this.userCredential.setUsername(updatedLocalUser.getUsername());
        localUser = updatedLocalUser;
        ServiceSupport.Instance.getCache().put(CACHE_KEY_LOCAL_USER, updatedLocalUser);
    }

    @Override
    public IUserCredential getUserCredential() {
        return userCredential;
    }

}
