package com.palomamobile.android.sdk.friend;

import java.io.Serializable;

/**
 * Class holds information about a 1:1 relationship between 2 users.
 * <br/>
 *
 */
public class RelationAttributes implements Serializable {

    private Type type;
    private Trigger trigger;
    private String customType;

    /**
     * Type of relationship.
     */
    public enum Type {
        friend, blocked, unknown
    }

    /**
     * Trigger for relationship creation.
     */
    public enum Trigger {
        facebook, contact, request, reciprocal
    }

    public RelationAttributes(Type type) {
        this.type = type;
    }

    public RelationAttributes(String customType) {
        this.customType = customType;
    }

    public String getCustomType() {
        return customType;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public Type getType() {
        return type;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationAttributes that = (RelationAttributes) o;

        if (type != that.type) return false;
        if (trigger != that.trigger) return false;
        return !(customType != null ? !customType.equals(that.customType) : that.customType != null);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (trigger != null ? trigger.hashCode() : 0);
        result = 31 * result + (customType != null ? customType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RelationAttributes{");
        sb.append("type=").append(type);
        sb.append(", customType='").append(customType).append('\'');
        sb.append(", trigger=").append(trigger);
        sb.append('}');
        return sb.toString();
    }
}
