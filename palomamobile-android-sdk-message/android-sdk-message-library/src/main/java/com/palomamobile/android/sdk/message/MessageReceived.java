package com.palomamobile.android.sdk.message;


/**
 * Class {@code MessageReceived} represents the information available about a message received from another {@link Sender} in the context of the Message SDK.
 * To retrieve the local user's list of received messages call {@link IMessageManager#createJobGetMessagesReceived()}
 * <br/>
 *
 */
public class MessageReceived extends BaseMessage {

    private Sender sender;
    private long timeSent;

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MessageReceived{");
        sb.append("sender=").append(sender);
        sb.append(", timeSent=").append(timeSent);
        sb.append(", " + super.toString());
        sb.append('}');
        return sb.toString();
    }
}
