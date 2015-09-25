package com.palomamobile.android.sdk.messageThread;

import java.util.HashMap;

/**
 *
 */
public class MessageThreadUpdate extends BaseMessageThread {

    public MessageThreadUpdate() {
    }

    public MessageThreadUpdate withName(String name) {
        setName(name);
        return this;
    }

    public MessageThreadUpdate withRelatedTo(long relatedTo) {
        setRelatedTo(relatedTo);
        return this;
    }

    public MessageThreadUpdate withType(String type) {
        setType(type);
        return this;
    }

    public MessageThreadUpdate removeName() {
        setName("");
        return this;
    }

    public MessageThreadUpdate removeRelatedTo() {
        setRelatedTo(-1L);
        return this;
    }

    public MessageThreadUpdate removeType() {
        setType("");
        return this;
    }

    public MessageThreadUpdate withCustom(String customName, String customValue) {
        if (custom == null) {
            custom = new HashMap<>();
        }
        custom.put(customName, customValue);
        return this;
    }

    public MessageThreadUpdate removeCustom(String customName) {
        if (custom == null) {
            custom = new HashMap<>();
        }
        custom.put(customName, "");
        return this;
    }


}
