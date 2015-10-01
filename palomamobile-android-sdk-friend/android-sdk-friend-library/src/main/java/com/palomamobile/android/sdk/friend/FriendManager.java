package com.palomamobile.android.sdk.friend;

import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.core.IServiceSupport;

class FriendManager implements IFriendManager {

    private static final String TAG = FriendManager.class.getSimpleName();

    private IFriendService friendService;

    public FriendManager(IServiceSupport serviceSupport) {
        this.friendService = serviceSupport.getRestAdapter().create(IFriendService.class);
        serviceSupport.registerServiceManager(IFriendManager.class, this);
    }

    @Override
    public JobPostSocialUserCredential createJobPostSocialUserCredential(SocialUserCredential credential) {
        return new JobPostSocialUserCredential(credential);
    }

    @Override
    public JobGetFriends createJobGetFriends() {
        return new JobGetFriends();
    }

    @Override
    public JobPutRelationship createJobPutRelationship(long reciprocalUserId, RelationAttributes relationAttributes) {
        return new JobPutRelationship(reciprocalUserId, relationAttributes);
    }

    @Override
    public JobPutRelationship createJobPutRelationship(String reciprocalUsername, RelationAttributes relationAttributes) {
        return new JobPutRelationship(reciprocalUsername, relationAttributes);
    }

    @Override
    public JobGetRelationships createJobGetRelationships() {
        return new JobGetRelationships();
    }

    @NonNull
    @Override
    public IFriendService getService() {
        return friendService;
    }
}
