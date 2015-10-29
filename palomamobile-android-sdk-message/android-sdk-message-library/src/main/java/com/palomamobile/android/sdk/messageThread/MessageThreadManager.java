package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.IServiceSupport;

class MessageThreadManager implements IMessageThreadManager {

    private IMessageThreadService messageThreadService;

    public MessageThreadManager(IServiceSupport serviceSupport) {
        this.messageThreadService = serviceSupport.getRestAdapter().create(IMessageThreadService.class);
        serviceSupport.registerServiceManager(IMessageThreadManager.class, this);
    }

    @Override
    public IMessageThreadService getService() {
        return messageThreadService;
    }

}
