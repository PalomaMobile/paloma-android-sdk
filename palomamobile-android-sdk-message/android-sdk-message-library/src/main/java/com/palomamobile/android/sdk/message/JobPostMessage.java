package com.palomamobile.android.sdk.message;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;

import java.util.ArrayList;
import java.util.List;

/**
 * Convenience wrapper around {@link IMessageService#postMessageSent(String, long, MessageSent)}
 * Once this job is completed (with success or failure) it posts {@link EventMessageSentPosted} on the
 * {@link de.greenrobot.event.EventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobPostMessage extends BaseRetryPolicyAwareJob<MessageSent> {

    public static final String TAG = JobPostMessage.class.getSimpleName();

    private long userId;
    private String contentType;
    private String payload;
    private String url;
    private List<Long> friendIds;

    /**
     * Create a new job to post a message to friends. At least one of {@code payload} or {@code url} must be non-null.
     * @param userId local user id
     * @param contentType payload content type as a MIME string
     * @param payload (optional) message payload as text
     * @param url (optional) payload url
     * @param friendIds list of recipient friend ids
     */
    public JobPostMessage(long userId, @Nullable String contentType, @Nullable String payload, @Nullable String url, @NonNull List<Long> friendIds) {
        this(new Params(0).requireNetwork().setPersistent(true), userId, contentType, payload, url, friendIds);
    }

    /**
     * Create a new job to post a message to friends. At least one of {@code payload} or {@code url} must be non-null.
     * @param params custom job params
     * @param userId local user id
     * @param contentType payload content type as a MIME string
     * @param payload (optional) message payload as text
     * @param url (optional) payload url
     * @param friendIds list of recipient friend ids
     */
    public JobPostMessage(Params params, long userId, @Nullable String contentType, @Nullable String payload, @Nullable String url, @NonNull List<Long> friendIds) {
        super(params);
        this.userId = userId;
        this.contentType = contentType;
        this.payload = payload;
        this.url = url;
        this.friendIds = friendIds;
    }

    @Override
    public MessageSent syncRun(boolean postEvent) throws Throwable {
        List<MessageContentDetail> contentDetails = new ArrayList<>();
        contentDetails.add(new MessageContentDetail(contentType, url, payload));
        MessageSent messageSent = new MessageSent();
        messageSent.setContentList(contentDetails);
        messageSent.setRecipients(friendIds);
        MessageManager messageManager = (MessageManager) ServiceSupport.Instance.getServiceManager(IMessageManager.class);
        MessageSent result = messageManager.getService().postMessageSent(getRetryId(), userId, messageSent);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMessageSentPosted(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMessageSentPosted(this, throwable));
    }
}
