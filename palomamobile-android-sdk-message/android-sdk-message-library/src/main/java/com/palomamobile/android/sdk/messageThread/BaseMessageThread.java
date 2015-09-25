package com.palomamobile.android.sdk.messageThread;

import java.io.Serializable;
import java.util.Map;

/**
 *
 */
public abstract class BaseMessageThread implements Serializable {
    protected String name;
    protected Long relatedTo;
    protected String type;
    protected Map<String, String> custom;

    public Map<String, String> getCustom() {
        return custom;
    }

    public void setCustom(Map<String, String> custom) {
        this.custom = custom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(Long relatedTo) {
        this.relatedTo = relatedTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MessageThreadUpdate{" +
                "name='" + name + '\'' +
                ", relatedTo=" + relatedTo +
                ", type='" + type + '\'' +
                ", custom=" + custom +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageThreadUpdate that = (MessageThreadUpdate) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (relatedTo != null ? !relatedTo.equals(that.relatedTo) : that.relatedTo != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return !(custom != null ? !custom.equals(that.custom) : that.custom != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (relatedTo != null ? relatedTo.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (custom != null ? custom.hashCode() : 0);
        return result;
    }
}
