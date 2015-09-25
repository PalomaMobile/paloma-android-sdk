package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.core.IServiceManager;

import java.util.List;
import java.util.Map;

/**
 * Created by Karel Herink
 */
public interface IMessageThreadManager extends IServiceManager<IMessageThreadService> {

    @Override
    public IMessageThreadService getService();

    JobPostMessageThread createJobPostMessageThread(String name, Long relatedTo, String type, Map<String, String> custom, List<Long> members);

    JobGetMessageThread createJobGetMessageThread(long messageThreadId);

    JobUpdateMessageThread createJobUpdateMessageThread(long messageThreadId, MessageThreadUpdate update);

    JobDeleteMessageThread createJobDeleteMessageThread(long messageThreadId);
}
