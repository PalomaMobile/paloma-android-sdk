package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.IServiceSupport;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class MessageThreadManager implements IMessageThreadManager {

    private IMessageThreadService messageThreadService;

    public MessageThreadManager(IServiceSupport serviceSupport) {
        this.messageThreadService = serviceSupport.getRestAdapter().create(IMessageThreadService.class);
        serviceSupport.registerServiceManager(IMessageThreadManager.class, this);
    }

    @Override
    public IMessageThreadService getService() {
        return messageThreadService;
    }

    @Override
    public JobPostMessageThread createJobPostMessageThread(String name, Long relatedTo, String type, Map<String, String> custom, List<Long> members) {
        return new JobPostMessageThread(name, relatedTo, type, custom, members);
    }

    @Override
    public JobGetMessageThread createJobGetMessageThread(long messageThreadId) {
        return new JobGetMessageThread(messageThreadId);
    }

    @Override
    public JobUpdateMessageThread createJobUpdateMessageThread(long messageThreadId, MessageThreadUpdate update) {
        return new JobUpdateMessageThread(messageThreadId, update);
    }

    @Override
    public JobDeleteMessageThread createJobDeleteMessageThread(long messageThreadId) {
        return new JobDeleteMessageThread(messageThreadId);
    }

    @Override
    public JobGetMessageThreadMembers createJobGetMessageThreadMembers(long messageThreadId) {
        return new JobGetMessageThreadMembers(messageThreadId);
    }
}
