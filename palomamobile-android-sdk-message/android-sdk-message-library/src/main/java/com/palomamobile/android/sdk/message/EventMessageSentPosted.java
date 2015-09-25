package com.palomamobile.android.sdk.message;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseJobEvent;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

import java.util.List;

/**
 * Event published on the {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()})
 * once a message is posted to a friend or friends. The event contains either {@code messageSent} on success or {@code throwable} on
 * failure.<br/>
 * To post a message use {@link IMessageManager#createJobPostMessageToFriend(String, String, String, long)} or {@link IMessageManager#createJobPostMessageToFriends(String, String, String, List)}
 * <br/>
 *
 */
public class EventMessageSentPosted extends BaseJobEvent<BaseRetryPolicyAwareJob<MessageSent>, MessageSent> {
    public EventMessageSentPosted(BaseRetryPolicyAwareJob<MessageSent> job, Throwable failure) {
        super(job, failure);
    }

    public EventMessageSentPosted(BaseRetryPolicyAwareJob<MessageSent> job, MessageSent messageSent) {
        super(job, messageSent);
    }
}
