package com.palomamobile.android.sdk.message;

import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.core.IServiceSupport;

class MessageManager implements IMessageManager {

    private IMessageService messageService;

    public MessageManager(IServiceSupport serviceSupport) {
        this.messageService = serviceSupport.getRestAdapter().create(IMessageService.class);
        serviceSupport.registerServiceManager(IMessageManager.class, this);
    }

    @Override
    public @NonNull IMessageService getService() {
        return messageService;
    }
}
