package com.palomamobile.android.sdk.friend.sampleApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.EventLocalUserUpdated;
import com.palomamobile.android.sdk.user.IUserManager;
import com.palomamobile.android.sdk.user.JobRegisterUser;
import com.palomamobile.android.sdk.user.PasswordUserCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RegisterUserActivity extends Activity {

    private static final Logger logger = LoggerFactory.getLogger(RegisterUserActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceSupport.Instance.getEventBus().register(this);
        setContentView(R.layout.activity_register_user);
        IUserManager userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);
        if (userManager.getUser() != null) {
            onUserAvailable();
        }
        else {
            long curTime = System.nanoTime();
            final String userName = "usr_" + curTime;
            final String userPassword = "pwd_" + curTime;

            JobRegisterUser jobRegisterUserViaPassword = new JobRegisterUser(new PasswordUserCredential(userName, userPassword));
            ServiceSupport.Instance.getJobManager().addJobInBackground(jobRegisterUserViaPassword);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceSupport.Instance.getEventBus().unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventLocalUserUpdated event) {
        logger.debug("onEventMainThread(): " + event);
        Throwable throwable = event.getFailure();
        if (throwable == null) {
            onUserAvailable();
        }
        else {
            TextView textView = (TextView) findViewById(R.id.textViewRegUser);
            textView.setText(R.string.err_register_user);
            View progressBar = findViewById(R.id.progressBarRegUser);
            progressBar.setVisibility(View.GONE);
        }
    }


    private void onUserAvailable() {
        Intent intent = new Intent(this, FriendSampleActivity.class);
        startActivity(intent);
        finish();
    }
}
