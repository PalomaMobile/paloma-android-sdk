package com.palomamobile.android.sdk.friend;

import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Methods in this interface provide convenient job creation methods that provide easy access
 * to the underlying {@link IFriendService} functionality. App developers can either use {@link BaseRetryPolicyAwareJob}
 * job instances returned by the {@code createJob...()} methods, or create custom jobs that invoke
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

    /**
     * Create a new {@link JobGetFriends}.
     * @return new job instance
     */
    JobGetFriends createJobGetFriends();

    /**
     * Create a new {@link JobPostSocialUserCredential}.
     * @param credential 3rd party credential that can be used by the platform for friend discovery
     * @return new job instance
     */
    JobPostSocialUserCredential createJobPostSocialUserCredential(SocialUserCredential credential);

    /**
     * Create a new {@link JobPutRelationship}. This enables local user to set relationship attributes between
     * themselves and another user to request or confirm friendship via {@link com.palomamobile.android.sdk.friend.RelationAttributes.Type#friend}
     * or block another user via  {@link com.palomamobile.android.sdk.friend.RelationAttributes.Type#blocked}
     * @param reciprocalUserId id of the user on the other end of this 1:1 relationship
     * @param relationAttributes description of the relationship
     * @return new job instance
     */
    JobPutRelationship createJobPutRelationship(long reciprocalUserId, RelationAttributes relationAttributes);

    /**
     * Create a new {@link JobGetRelationships}.
     * @return new job instance
     */
    JobGetRelationships createJobGetRelationships();

}
