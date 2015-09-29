package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.message.MessageSent;

import java.util.List;
import java.util.Map;

/**
 * Created by Karel Herink
 */
public interface IMessageThreadManager extends IServiceManager<IMessageThreadService> {

    @Override
    public IMessageThreadService getService();

    JobPostMessageThread createJobPostMessageThread(String name, Long relatedTo, String type, Map<String, String> custom, List<Long> members);

    JobPostMessageThread createJobPostMessageThread(NewMessageThread newMessageThread);

    JobGetMessageThread createJobGetMessageThread(long messageThreadId);

    JobUpdateMessageThread createJobUpdateMessageThread(long messageThreadId, MessageThreadUpdate update);

    JobDeleteMessageThread createJobDeleteMessageThread(long messageThreadId);

    JobGetMessageThreadMembers createJobGetMessageThreadMembers(long messageThreadId);

    JobAddMessageThreadMember createJobAddMessageThreadMember(long messageThreadId, long userId);

    JobDeleteMessageThreadMember createJobDeleteMessageThreadMember(long messageThreadId, long userId);

    JobGetMessageThreadMessages createJobGetMessageThreadMessages(long messageThreadId);

    JobPostMessageThreadMessage createJobPostMessageThreadMessage(long messageThreadId, MessageSent newMessage);
}
