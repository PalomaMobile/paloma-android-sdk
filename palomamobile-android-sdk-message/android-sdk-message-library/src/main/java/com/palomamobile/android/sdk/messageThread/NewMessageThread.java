package com.palomamobile.android.sdk.messageThread;

import java.util.List;

/**
 *
 */
public class NewMessageThread extends BaseMessageThread {

    private List<Long> members;

    public List<Long> getMembers() {
        return members;
    }

    public void setMembers(List<Long> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "NewMessageThread{" +
                "members=" + members +
                "} " + super.toString();
    }

}
