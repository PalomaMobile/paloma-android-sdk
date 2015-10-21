package com.palomamobile.android.sdk.friend.sampleApp;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.friend.EventFriendsListReceived;
import com.palomamobile.android.sdk.friend.Friend;
import com.palomamobile.android.sdk.friend.IFriendManager;
import com.palomamobile.android.sdk.friend.JobGetFriends;
import com.palomamobile.android.sdk.friend.JobPostRelationship;
import com.palomamobile.android.sdk.friend.RelationAttributes;
import com.palomamobile.android.sdk.user.IUserManager;

import java.util.List;

/**
 *
 */
public class FriendSampleActivity extends Activity {

    private static final String TAG = FriendSampleActivity.class.getSimpleName();

    private TextView textViewUserId;

    private EditText editTextFriendUserId;
    private Button buttonAddFriend;

    private Button buttonRefresh;
    private EditText editTextFriendsList;

    private IUserManager userManager;
    private IFriendManager friendManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceSupport.Instance.getEventBus().register(this);
        userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);
        friendManager = ServiceSupport.Instance.getServiceManager(IFriendManager.class);
        setContentView(R.layout.friend);

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
                    JobPostRelationship jobPostRelationship = friendManager.createJobPutRelationship(reciprocalUserId, new RelationAttributes(RelationAttributes.Type.friend));
                    ServiceSupport.Instance.getJobManager().addJobInBackground(jobPostRelationship);
                } catch (Throwable throwable) {
                    Log.w(TAG, throwable);
                    Toast.makeText(FriendSampleActivity.this, "Friend User ID invalid.", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FriendSampleActivity.this, R.string.please_wait, Toast.LENGTH_LONG).show();
                JobGetFriends jobGetFriends = friendManager.createJobGetFriends();
                ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetFriends);

            }
        });
    }

    private void displayFriends(List<Friend> friendList) {
        StringBuffer friendNameBuffer = new StringBuffer();
        for(Friend friend : friendList){
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
        Log.d(TAG, "onEventMainThread(): " + event);
        Throwable throwable = event.getFailure();
        if (throwable == null) {
            displayFriends(event.getSuccess().getEmbedded().getItems());
        }
        else {
            Toast.makeText(getApplicationContext(), "Err: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            Log.w(TAG, throwable.getMessage());
        }
    }
}
