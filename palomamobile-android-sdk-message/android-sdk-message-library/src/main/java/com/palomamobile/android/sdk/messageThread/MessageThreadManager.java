package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.IServiceSupport;
import com.palomamobile.android.sdk.message.MessageSent;

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
        NewMessageThread newMessageThread = new NewMessageThread();
        newMessageThread.setName(name);
        newMessageThread.setRelatedTo(relatedTo);
        newMessageThread.setType(type);
        newMessageThread.setCustom(custom);
        newMessageThread.setMembers(members);
        return createJobPostMessageThread(newMessageThread);
    }

    @Override
    public JobPostMessageThread createJobPostMessageThread(NewMessageThread newMessageThread) {
        return new JobPostMessageThread(newMessageThread);
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

    @Override
    public JobAddMessageThreadMember createJobAddMessageThreadMember(long messageThreadId, long userId) {
        return new JobAddMessageThreadMember(messageThreadId, userId);
    }

    public JobDeleteMessageThreadMember createJobDeleteMessageThreadMember(long messageThreadId, long userId) {
        return new JobDeleteMessageThreadMember(messageThreadId, userId);
    }

    @Override
    public JobGetMessageThreadMessages createJobGetMessageThreadMessages(long messageThreadId) {
        return new JobGetMessageThreadMessages(messageThreadId);
    }

    @Override
    public JobPostMessageThreadMessage createJobPostMessageThreadMessage(long messageThreadId, MessageSent newMessage) {
        return new JobPostMessageThreadMessage(messageThreadId, newMessage);
    }

}
