package com.palomamobile.android.sdk.friend;

import java.io.Serializable;

/**
 * Describes a 1:1 relationship between the current user and a the reciprocal user at the other end of this relationship.
 * View of the relationship from the point of current user to the reciprocal user is returned by {@link #getUserRelation()}.
 * View of the relationship from the point of reciprocal user to the  current user is returned by {@link #getReciprocalRelation()}.
 *
 *
 */
public class Relationship implements Serializable {

    private final long userId;
    private final RelationAttributes userRelation;
    private final long reciprocalUserId;
    private final String reciprocalUsername;
    private final RelationAttributes reciprocalRelation;

    public Relationship(long userId, RelationAttributes userRelation, long reciprocalUserId, String reciprocalUsername, RelationAttributes reciprocalRelation) {
        this.userId = userId;
        this.userRelation = userRelation;
        this.reciprocalUserId = reciprocalUserId;
        this.reciprocalUsername = reciprocalUsername;
        this.reciprocalRelation = reciprocalRelation;
    }

    public RelationAttributes getReciprocalRelation() {
        return reciprocalRelation;
    }

    public long getReciprocalUserId() {
        return reciprocalUserId;
    }

    public String getReciprocalUsername() {
        return reciprocalUsername;
    }

    public long getUserId() {
        return userId;
    }

    public RelationAttributes getUserRelation() {
        return userRelation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Relationship that = (Relationship) o;

        if (userId != that.userId) return false;
        if (reciprocalUserId != that.reciprocalUserId) return false;
        if (userRelation != null ? !userRelation.equals(that.userRelation) : that.userRelation != null) return false;
        if (reciprocalUsername != null ? !reciprocalUsername.equals(that.reciprocalUsername) : that.reciprocalUsername != null)
            return false;
        return !(reciprocalRelation != null ? !reciprocalRelation.equals(that.reciprocalRelation) : that.reciprocalRelation != null);
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (userRelation != null ? userRelation.hashCode() : 0);
        result = 31 * result + (int) (reciprocalUserId ^ (reciprocalUserId >>> 32));
        result = 31 * result + (reciprocalUsername != null ? reciprocalUsername.hashCode() : 0);
        result = 31 * result + (reciprocalRelation != null ? reciprocalRelation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Relationship{");
        sb.append("userId=").append(userId);
        sb.append(", userRelation=").append(userRelation);
        sb.append(", reciprocalUserId=").append(reciprocalUserId);
        sb.append(", reciprocalUsername='").append(reciprocalUsername).append('\'');
        sb.append(", reciprocalRelation=").append(reciprocalRelation);
        sb.append('}');
        return sb.toString();
    }
}
