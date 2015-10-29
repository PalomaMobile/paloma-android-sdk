package com.palomamobile.android.sdk.message;


import java.util.List;

/**
 * Class {@code MessageReceived} represents the information available about a message received from another {@link UserDetails} in the context of the Message SDK.
 * To retrieve the local user's list of received messages call {@link  JobGetMessagesReceived}
 * <br/>
 *
 */
public class MessageReceived extends BaseMessage {

    @SuppressWarnings("unused")
    private UserDetails sender;

    @SuppressWarnings("unused")
    private List<Long> otherRecipients;

    public UserDetails getSender() {
        return sender;
    }


    public List<Long> getOtherRecipients() {
        return otherRecipients;
    }

    @Override
    public String toString() {
        return "MessageReceived{" +
                "otherRecipients=" + otherRecipients +
                ", sender=" + sender +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MessageReceived that = (MessageReceived) o;

        if (sender != null ? !sender.equals(that.sender) : that.sender != null) return false;
        return !(otherRecipients != null ? !otherRecipients.equals(that.otherRecipients) : that.otherRecipients != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (otherRecipients != null ? otherRecipients.hashCode() : 0);
        return result;
    }
}
