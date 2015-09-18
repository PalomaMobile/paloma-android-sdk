package com.palomamobile.android.sdk.message;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;

import java.util.List;

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
     * The actual message content must be specified by at least one of {@code payload} or {@code url} parameters.
     * If both {@code payload} and {@code url} are set the content found at the {@code url} doesn't have to match the value of
     * {@code payload}, especially since the content at the {@code url} can change over time.
     * If only {@code payload} is set  then service will create a url with content type "text/plain" containing the payload.
     * When the request is completed an {@link EventMessageSentPosted} is published on the {@link de.greenrobot.event.EventBus}
     * as returned by {@link ServiceSupport#getEventBus()}.
     *
     * @param contentType optional content type, if missing server will determine by checking url or if url null default to "text/plain"
     * @param payload text representation of payload, can be used together with url, if url is missing then service will create a url
     *                with content type "text/plain" containing the payload, there is no enforcement to ensure that url and payload
     *                have the same payload
     * @param url url that can points to the full message payload
     * @param friendId recipient of message
     */
    JobPostMessage createJobPostMessageToFriend(@Nullable String contentType, @Nullable String payload, @Nullable String url, long friendId);

    /**
     * @param contentType
     * @param payload
     * @param url
     * @param friendIds message recipients
     *
     * @see #createJobPostMessageToFriend(String, String, String, long)
     */
    JobPostMessage createJobPostMessageToFriends(@Nullable String contentType, @NonNull String payload, @Nullable String url, @NonNull List<Long> friendIds);

    /**
     * Async request to refresh the list of messages received by the current user.
     * When the request is completed an {@link EventMessagesReceived} is published on the {@link de.greenrobot.event.EventBus}
     * as returned by {@link ServiceSupport#getEventBus()}.
     */
    JobGetMessagesReceived createJobGetMessagesReceived();


    /**
     * Async request to refresh the list of messages sent by the current user.
     * When the request is completed an {@link EventMessagesSent} is published on the {@link de.greenrobot.event.EventBus}
     * as returned by {@link ServiceSupport#getEventBus()}.
     */
    JobGetMessagesSent createJobGetMessagesSent();

    /**
     * Async request to delete a received message identified by {@code messageId}. Deletion of a message will publish
     * {@link EventMessagesReceived} with an updated list of messages immediately after the local caches are updated
     * (prior to any network access). Event is published on the {@link de.greenrobot.event.EventBus}
     * as returned by {@link ServiceSupport#getEventBus()}.
     *
     * @param messageId
     */
    JobDeleteMessageReceived createJobDeleteMessageReceived(@NonNull long messageId);

}
