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
}
