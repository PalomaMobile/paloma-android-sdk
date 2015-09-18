package com.palomamobile.android.sdk.media.sampleApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.EventLocalUserUpdated;
import com.palomamobile.android.sdk.user.IUserManager;
import com.palomamobile.android.sdk.user.JobRegisterUser;


public class RegisterUserActivity extends Activity {

    private static final String TAG = RegisterUserActivity.class.getSimpleName();

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

            JobRegisterUser jobRegisterUserViaPassword = userManager.createJobRegisterUserViaPassword(userName, userPassword);
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
        Log.d(TAG, "onEventMainThread(): " + event);
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
        Intent intent = new Intent(this, MediaSampleActivity.class);
        startActivity(intent);
        finish();
    }
}
