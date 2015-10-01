package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.message.MessageSent;

/**
 * Created by Karel Herink
 */
public interface IMessageThreadManager extends IServiceManager<IMessageThreadService> {

    @Override
    IMessageThreadService getService();

    /**
     * A convenience method o create a new {@link JobPostMessageThread} 
     *
     * @param newMessageThread
     * @return newly created job
     */
    JobPostMessageThread createJobPostMessageThread(NewMessageThread newMessageThread);

    /**
     * A convenience method o create a new {@link JobGetMessageThread} 
     *
     * @param messageThreadId
     * @return newly created job
     */
    JobGetMessageThread createJobGetMessageThread(long messageThreadId);

    /**
     * A convenience method o create a new {@link JobUpdateMessageThread} 
     *
     * @param messageThreadId
     * @param update
     * @return newly created job
     */
    JobUpdateMessageThread createJobUpdateMessageThread(long messageThreadId, MessageThreadUpdate update);

    /**
     * A convenience method o create a new {@link JobDeleteMessageThread} 
     *
     * @param messageThreadId
     * @return newly created job
     */
    JobDeleteMessageThread createJobDeleteMessageThread(long messageThreadId);

    /**
     * A convenience method o create a new {@link JobGetMessageThreadMembers} 
     *
     * @param messageThreadId
     * @return newly created job
     */
    JobGetMessageThreadMembers createJobGetMessageThreadMembers(long messageThreadId);

    /**
     * A convenience method o create a new {@link JobAddMessageThreadMember} 
     *
     * @param messageThreadId
     * @param userId
     * @return newly created job
     */
    JobAddMessageThreadMember createJobAddMessageThreadMember(long messageThreadId, long userId);

    /**
     * A convenience method o create a new {@link JobDeleteMessageThreadMember} 
     *
     * @param messageThreadId
     * @param userId
     * @return newly created job
     */
    JobDeleteMessageThreadMember createJobDeleteMessageThreadMember(long messageThreadId, long userId);

    /**
     * A convenience method o create a new {@link JobGetMessageThreadMessages} 
     *
     * @param messageThreadId
     * @return newly created job
     */
    JobGetMessageThreadMessages createJobGetMessageThreadMessages(long messageThreadId);

    /**
     * A convenience method o create a new {@link JobPostMessageThreadMessage} 
     *
     * @param messageThreadId
     * @param newMessage
     * @return newly created job
     */
    JobPostMessageThreadMessage createJobPostMessageThreadMessage(long messageThreadId, MessageSent newMessage);

    /**
     * A convenience method o create a new {@link JobGetMessageThreads} 
     *
     * @return newly created job
     */
    JobGetMessageThreads createJobGetMessageThreads();

    /**
     * A convenience method o create a new {@link JobDeleteMessageThreads} 
     *
     * @return newly created job
     */
    JobDeleteMessageThreads createJobDeleteMessageThreads();
}
