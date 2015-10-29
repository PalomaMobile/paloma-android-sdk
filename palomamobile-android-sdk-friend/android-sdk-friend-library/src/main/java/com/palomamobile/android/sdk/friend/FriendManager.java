package com.palomamobile.android.sdk.friend;

import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.core.IServiceSupport;

class FriendManager implements IFriendManager {

    private IFriendService friendService;

    public FriendManager(IServiceSupport serviceSupport) {
        this.friendService = serviceSupport.getRestAdapter().create(IFriendService.class);
        serviceSupport.registerServiceManager(IFriendManager.class, this);
    }

    @NonNull
    @Override
    public IFriendService getService() {
        return friendService;
    }
}
