package com.palomamobile.android.sdk.message.sampleApp;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.palomamobile.android.sdk.core.ServiceRequestParams;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.friend.EventFriendsListReceived;
import com.palomamobile.android.sdk.friend.Friend;
import com.palomamobile.android.sdk.friend.IFriendManager;
import com.palomamobile.android.sdk.friend.JobGetFriends;
import com.palomamobile.android.sdk.friend.JobPutRelationship;
import com.palomamobile.android.sdk.friend.RelationAttributes;
import com.palomamobile.android.sdk.message.EventMessagesReceived;
import com.palomamobile.android.sdk.message.IMessageManager;
import com.palomamobile.android.sdk.message.JobDeleteMessageReceived;
import com.palomamobile.android.sdk.message.JobGetMessagesReceived;
import com.palomamobile.android.sdk.message.JobPostMessage;
import com.palomamobile.android.sdk.message.MessageContentDetail;
import com.palomamobile.android.sdk.message.MessageReceived;
import com.palomamobile.android.sdk.message.MessageSent;
import com.palomamobile.android.sdk.user.IUserManager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MessageSampleActivity extends Activity {

    private static final String TAG = MessageSampleActivity.class.getSimpleName();

    private IUserManager userManager;
    private IFriendManager friendManager ;
    private IMessageManager messageManager;


    //local user id
    private TextView textViewUserId;

    //friends UI
    private EditText editTextFriendUserId;
    private Button buttonAddFriend;
    private Button buttonRefreshFriends;
    private EditText editTextFriendsList;

    //messages UI
    private EditText editTextMessage;
    private Button buttonSendMessageToAll;
    private Button buttonGetMessages;

    //data model
    private List<Friend> friends;
    private List<MessageReceived> messagesReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceSupport.Instance.getEventBus().register(this);
        userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);
        friendManager = ServiceSupport.Instance.getServiceManager(IFriendManager.class);
        messageManager = ServiceSupport.Instance.getServiceManager(IMessageManager.class);

        setContentView(R.layout.send_receive_messages);

        //friends
        textViewUserId = (TextView) findViewById(R.id.textViewUserId);
        textViewUserId.setText(Long.toString(userManager.getUser().getId()));
        editTextFriendsList = (EditText) findViewById(R.id.editTextFriends);
        editTextFriendUserId = (EditText) findViewById(R.id.editTextFriendId);
        buttonAddFriend = (Button) findViewById(R.id.buttonAddFriend);
        buttonAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long reciprocalUserId;
                try {
                    reciprocalUserId = Long.parseLong(editTextFriendUserId.getText().toString());
                    JobPutRelationship jobPutRelationship = friendManager.createJobPutRelationship(reciprocalUserId, new RelationAttributes(RelationAttributes.Type.friend));
                    ServiceSupport.Instance.getJobManager().addJobInBackground(jobPutRelationship);
                } catch (Throwable throwable) {
                    Log.w(TAG, throwable);
                    Toast.makeText(MessageSampleActivity.this, "Friend User ID invalid.", Toast.LENGTH_LONG).show();
                }
            }
        });
        buttonRefreshFriends = (Button) findViewById(R.id.buttonRefresh);
        buttonRefreshFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageSampleActivity.this, R.string.please_wait, Toast.LENGTH_LONG).show();
                JobGetFriends jobGetFriends = friendManager.createJobGetFriends();
                ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetFriends);

            }
        });

        //messages
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        buttonSendMessageToAll = (Button) findViewById(R.id.buttonSendMessageToAll);
        buttonSendMessageToAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFriends() && checkMessageText()) {
                    List<Long> friendIds = new ArrayList<>();
                    for (Friend friend : friends) {
                        friendIds.add(friend.getUserId());
                    }
                    List<MessageContentDetail> contentDetails = new ArrayList<>();
                    contentDetails.add(new MessageContentDetail("text/plain", editTextMessage.getText().toString(), null));
                    MessageSent messageSent = new MessageSent();
                    messageSent.setType("pic_share");
                    messageSent.setContentList(contentDetails);
                    messageSent.setRecipients(friendIds);

                    JobPostMessage jobPostMessageToFriends = messageManager.createJobPostMessage(messageSent);
                    ServiceSupport.Instance.getJobManager().addJobInBackground(jobPostMessageToFriends);
                }
            }
        });
        buttonGetMessages = (Button) findViewById(R.id.buttonGetMessages);
        buttonGetMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFriends()) {
                    JobGetMessagesReceived jobGetMessagesReceived = messageManager.createJobGetMessagesReceived();
                    jobGetMessagesReceived.setServiceRequestParams(new ServiceRequestParams().sort("id", ServiceRequestParams.Sort.Order.Desc));
                    ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetMessagesReceived);
                }
            }
        });
    }

    private boolean checkMessageText() {
        boolean valid = editTextMessage.getText().toString().length() != 0;
        if (!valid) {
            Toast.makeText(this, "Enter some text first.", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    private boolean checkFriends() {
        boolean valid = friends != null && friends.size() > 0;
        if (!valid) {
            Toast.makeText(this, "Get a friend first.", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }


    private void displayFriends() {
        StringBuffer friendNameBuffer = new StringBuffer();
        for(Friend friend : friends){
            friendNameBuffer.append(friend + "\n");
        }
        editTextFriendsList.setText(friendNameBuffer);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        ServiceSupport.Instance.getEventBus().unregister(this);
        super.onDestroy();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventFriendsListReceived event) {
        Log.d(TAG, "onEventMainThread(EventFriendsListReceived): " + event);
        Throwable throwable = event.getFailure();
        if (throwable == null) {
            this.friends = event.getSuccess().getEmbedded().getItems();
            displayFriends();
        }
        else {
            Toast.makeText(getApplicationContext(), "Err: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            Log.w(TAG, throwable.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventMessagesReceived event) {
        Log.d(TAG, "onEventMainThread(EventMessagesReceived): " + event);
        Throwable throwable = event.getFailure();
        if (throwable == null) {
            if (event.getSuccess().getEmbedded() != null) {
                messagesReceived = event.getSuccess().getEmbedded().getItems();
                showLastMessageReceived();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Err: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            Log.w(TAG, throwable.getMessage());
        }
    }

    private void showLastMessageReceived() {
        if (messagesReceived != null && messagesReceived.size() > 0) {
            MessageReceived messageReceived = messagesReceived.get(0);
            Toast.makeText(this, messageReceived.getContentList().get(0).getPayload(), Toast.LENGTH_LONG).show();
            deleteMessage(messageReceived);
        }
    }

    private void deleteMessage(MessageReceived messageReceived) {
        JobDeleteMessageReceived jobDeleteMessageReceived = messageManager.createJobDeleteMessageReceived(messageReceived.getId());
        messagesReceived.remove(messageReceived);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobDeleteMessageReceived);
    }

}
