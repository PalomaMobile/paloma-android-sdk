package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.message.UserDetails;

import java.io.Serializable;

/**
 *
 */
public class MessageThreadMember implements Serializable {
    private long threadId;
    private UserDetails user;
}
