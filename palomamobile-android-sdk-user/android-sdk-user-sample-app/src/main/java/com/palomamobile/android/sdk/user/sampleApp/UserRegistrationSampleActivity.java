package com.palomamobile.android.sdk.user.sampleApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.EventLocalUserUpdated;
import com.palomamobile.android.sdk.user.IUserManager;
import com.palomamobile.android.sdk.user.JobRegisterUser;
import com.palomamobile.android.sdk.user.User;


public class UserRegistrationSampleActivity extends Activity {

    private static final String TAG = UserRegistrationSampleActivity.class.getSimpleName();

    private IUserManager userManager;

    private CallbackManager fbCallbackManager;
    private LoginButton buttonFbLogin;
    private Button buttonCreateAccount;
    private EditText editTextUsername;
    private EditText editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);

        setContentView(R.layout.activity_user_registration_sample);
        ServiceSupport.Instance.getEventBus().register(this);
        initializeFbLogin();

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if (userName.isEmpty() || password.isEmpty()) {
                    Toast.makeText(UserRegistrationSampleActivity.this, R.string.provide_credentials, Toast.LENGTH_LONG).show();
                }
                else {
                    JobRegisterUser jobRegisterUserViaPassword = userManager.createJobRegisterUserViaPassword(userName, password);
                    ServiceSupport.Instance.getJobManager().addJobInBackground(jobRegisterUserViaPassword);
                    setRegistrationInProgress(true);
                }
            }
        });
    }

    protected void initializeFbLogin() {

        fbCallbackManager = CallbackManager.Factory.create();

        buttonFbLogin = (LoginButton) findViewById(R.id.buttonFbLogin);
        buttonFbLogin.setReadPermissions("user_friends");
        buttonFbLogin.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Log.d(TAG, "FB login success, received FB AccessToken: " +
                        "userId = " + accessToken.getUserId() + ", " +
                        "applicationId = " + accessToken.getApplicationId() + ", " +
                        "token = " + accessToken.getToken());
                JobRegisterUser jobRegisterUserViaFacebook = userManager.createJobRegisterUserViaFacebook(accessToken.getUserId(), accessToken.getToken());
                ServiceSupport.Instance.getJobManager().addJobInBackground(jobRegisterUserViaFacebook);
                setRegistrationInProgress(false);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "FB login cancelled");
                setRegistrationInProgress(false);
            }

            @Override
            public void onError(FacebookException exception) {
                Log.w(TAG, "FB login throwable", exception);
                setRegistrationInProgress(false);
            }
        });
        buttonFbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRegistrationInProgress(AccessToken.getCurrentAccessToken() == null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        ServiceSupport.Instance.getEventBus().unregister(this);
        super.onDestroy();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventLocalUserUpdated event) {
        Throwable throwable = event.getFailure();
        if (throwable == null) {
            Log.d(TAG, "onEventMainThread(): " + event);
            setResult(RESULT_OK);
            User user = event.getSuccess();
            Toast.makeText(getApplicationContext(), getString(R.string.login_success_as, user.getUsername(), user.getId()), Toast.LENGTH_LONG).show();
            //finish();
        }
        else {
            Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        setRegistrationInProgress(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void setRegistrationInProgress(boolean inProgress) {
        boolean uiEnabled = !inProgress;
        buttonFbLogin.setEnabled(uiEnabled);
        editTextUsername.setEnabled(uiEnabled);
        editTextPassword.setEnabled(uiEnabled);
        buttonCreateAccount.setEnabled(uiEnabled);
    }

}
