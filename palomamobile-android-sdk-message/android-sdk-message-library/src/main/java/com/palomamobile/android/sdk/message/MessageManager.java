package com.palomamobile.android.sdk.message;

import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.core.IServiceSupport;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.IUserManager;

class MessageManager implements IMessageManager {

    private IMessageService messageService;

    public MessageManager(IServiceSupport serviceSupport) {
        this.messageService = serviceSupport.getRestAdapter().create(IMessageService.class);
        serviceSupport.registerServiceManager(IMessageManager.class, this);
    }

    @Override
    public JobPostMessage createJobPostMessage(MessageSent message) {
        IUserManager userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);
        return new JobPostMessage(userManager.getUser().getId(), message);
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
