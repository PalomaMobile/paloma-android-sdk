package com.palomamobile.android.sdk.message;


import java.util.List;

/**
 * Class {@code MessageReceived} represents the information available about a message received from another {@link UserDetails} in the context of the Message SDK.
 * To retrieve the local user's list of received messages call {@link IMessageManager#createJobGetMessagesReceived()}
 * <br/>
 *
 */
public class MessageReceived extends BaseMessage {

    private UserDetails sender;
    private long timeSent;
    private long replyToRootId;
    private long sentMessageId;
    private List<Long> otherRecipients;

    public UserDetails getSender() {
        return sender;
    }

    public void setSender(UserDetails sender) {
        this.sender = sender;
    }

    public long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
    }

    public List<Long> getOtherRecipients() {
        return otherRecipients;
    }

    public void setOtherRecipients(List<Long> otherRecipients) {
        this.otherRecipients = otherRecipients;
    }

    public long getReplyToRootId() {
        return replyToRootId;
    }

    public void setReplyToRootId(long replyToRootId) {
        this.replyToRootId = replyToRootId;
    }

    public long getSentMessageId() {
        return sentMessageId;
    }

    public void setSentMessageId(long sentMessageId) {
        this.sentMessageId = sentMessageId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MessageReceived{");
        sb.append("sender=").append(sender);
        sb.append(", timeSent=").append(timeSent);
        sb.append(", replyToRootId=").append(replyToRootId);
        sb.append(", sentMessageId=").append(sentMessageId);
        sb.append(", otherRecipients=").append(otherRecipients);
        sb.append(", " + super.toString());
        sb.append('}');
        return sb.toString();
    }
}
