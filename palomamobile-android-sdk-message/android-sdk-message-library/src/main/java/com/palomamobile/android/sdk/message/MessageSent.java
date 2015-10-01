package com.palomamobile.android.sdk.message;

import java.util.List;

/**
 * Class {@code MessageSent} represents the information available about a message sent from current user to a list of recipient user ids.
 * <br/>
 *
 */
public class MessageSent extends BaseMessage {

    private List<Long> recipients;
    private boolean includeRecipients;

    public List<Long> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Long> recipients) {
        this.recipients = recipients;
    }

    public boolean isIncludeRecipients() {
        return includeRecipients;
    }

    public void setIncludeRecipients(boolean includeRecipients) {
        this.includeRecipients = includeRecipients;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MessageRequest{");
        sb.append("recipients=").append(recipients);
        sb.append(", includeRecipients=").append(includeRecipients);
        sb.append(", " + super.toString());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MessageSent that = (MessageSent) o;

        if (includeRecipients != that.includeRecipients) return false;
        return !(recipients != null ? !recipients.equals(that.recipients) : that.recipients != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (recipients != null ? recipients.hashCode() : 0);
        result = 31 * result + (includeRecipients ? 1 : 0);
        return result;
    }
}
