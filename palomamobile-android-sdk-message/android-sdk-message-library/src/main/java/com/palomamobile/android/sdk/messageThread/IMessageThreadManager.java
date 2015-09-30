package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.message.MessageSent;

/**
 * Created by Karel Herink
 */
public interface IMessageThreadManager extends IServiceManager<IMessageThreadService> {

    @Override
    public IMessageThreadService getService();

    JobPostMessageThread createJobPostMessageThread(NewMessageThread newMessageThread);

    JobGetMessageThread createJobGetMessageThread(long messageThreadId);

    JobUpdateMessageThread createJobUpdateMessageThread(long messageThreadId, MessageThreadUpdate update);

    JobDeleteMessageThread createJobDeleteMessageThread(long messageThreadId);

    JobGetMessageThreadMembers createJobGetMessageThreadMembers(long messageThreadId);

    JobAddMessageThreadMember createJobAddMessageThreadMember(long messageThreadId, long userId);

    JobDeleteMessageThreadMember createJobDeleteMessageThreadMember(long messageThreadId, long userId);

    JobGetMessageThreadMessages createJobGetMessageThreadMessages(long messageThreadId);

    JobPostMessageThreadMessage createJobPostMessageThreadMessage(long messageThreadId, MessageSent newMessage);

    JobGetMessageThreads createJobGetMessageThreads();

    JobDeleteMessageThreads createJobDeleteMessageThreads();
}
