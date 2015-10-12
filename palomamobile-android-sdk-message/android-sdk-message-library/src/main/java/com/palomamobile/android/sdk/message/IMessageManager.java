package com.palomamobile.android.sdk.message;

import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

/**
 * Methods in this interface provide convenient job creation methods that provide easy access
 * to the underlying {@link IMessageService} functionality. App developers can either use {@link BaseRetryPolicyAwareJob}
 * job instances returned by the {@code createJob...()} methods, or create custom jobs that invoke
 * methods of the {@link IMessageService} returned by {@link IServiceManager#getService()}
 *
 * <br/>
 * To get a concrete implementation of this interface call
 * {@code ServiceSupport.Instance.getServiceManager(IMessageManager.class)}
 * <br/>
 *
 * <br/>
 *
 */
public interface IMessageManager extends IServiceManager<IMessageService> {

    /**
     * Request to send a message to a friend identified by {@code friendId} as returned by the Friend service.
     * The actual message must contain at least one of {@code payload} or {@code url} values.
     * If both {@code payload} and {@code url} are set the content found at the {@code url} doesn't have to match the value of
     * {@code payload}, especially since the content at the {@code url} can change over time.
     * If only {@code payload} is set  then service will create a url with content type "text/plain" containing the payload.
     * When the request is completed an {@link EventMessageSentPosted} is published on the {@link com.palomamobile.android.sdk.core.IEventBus}
     * as returned by {@link ServiceSupport#getEventBus()}.
     *
     * @param message message to be sent
     */
    JobPostMessage createJobPostMessage(MessageSent message);

    /**
     * Async request to refresh the list of messages received by the current user.
     * When the request is completed an {@link EventMessagesReceived} is published on the {@link com.palomamobile.android.sdk.core.IEventBus}
     * as returned by {@link ServiceSupport#getEventBus()}.
     */
    JobGetMessagesReceived createJobGetMessagesReceived();


    /**
     * Async request to refresh the list of messages sent by the current user.
     * When the request is completed an {@link EventMessagesSent} is published on the {@link com.palomamobile.android.sdk.core.IEventBus}
     * as returned by {@link ServiceSupport#getEventBus()}.
     */
    JobGetMessagesSent createJobGetMessagesSent();

    /**
     * Async request to delete a received message identified by {@code messageId}. Deletion of a message will publish
     * {@link EventMessagesReceived} with an updated list of messages immediately after the local caches are updated
     * (prior to any network access). Event is published on the {@link com.palomamobile.android.sdk.core.IEventBus}
     * as returned by {@link ServiceSupport#getEventBus()}.
     *
     * @param messageId
     */
    JobDeleteMessageReceived createJobDeleteMessageReceived(@NonNull long messageId);

}
