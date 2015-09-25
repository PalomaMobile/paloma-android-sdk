package com.palomamobile.android.sdk.message;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.palomamobile.android.sdk.core.IServiceSupport;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.IUserManager;

import java.util.ArrayList;
import java.util.List;

class MessageManager implements IMessageManager {

    private IMessageService messageService;

    public MessageManager(IServiceSupport serviceSupport) {
        this.messageService = serviceSupport.getRestAdapter().create(IMessageService.class);
        serviceSupport.registerServiceManager(IMessageManager.class, this);
    }

    @Override
    public JobPostMessage createJobPostMessageToFriend(@Nullable String contentType, @NonNull String payload,  @Nullable String url, long friendId) {
        List<Long> friendIds = new ArrayList<>(1);
        friendIds.add(friendId);
        return createJobPostMessageToFriends(contentType, payload, url, friendIds);
    }

    @Override
    public JobPostMessage createJobPostMessageToFriends(@Nullable String contentType, @NonNull String payload, @Nullable String url, @NonNull List<Long> friendIds) {
        IUserManager userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);
        return new JobPostMessage(userManager.getUser().getId(), contentType, payload, url, friendIds);
    }

    @Override
    public JobGetMessagesReceived createJobGetMessagesReceived() {
        IUserManager userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);
        return new JobGetMessagesReceived(userManager.getUser().getId());
    }

    @Override
    public JobGetMessagesSent createJobGetMessagesSent() {
        IUserManager userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);
        return new JobGetMessagesSent(userManager.getUser().getId());
    }

    @Override
    public JobDeleteMessageReceived createJobDeleteMessageReceived(@NonNull long messageId) {
        IUserManager userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);
        return new JobDeleteMessageReceived(userManager.getUser().getId(), messageId);
    }

    @Override
    public @NonNull IMessageService getService() {
        return messageService;
    }
}
