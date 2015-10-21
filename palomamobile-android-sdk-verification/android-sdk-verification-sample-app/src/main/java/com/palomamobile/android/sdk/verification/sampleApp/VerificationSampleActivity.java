package com.palomamobile.android.sdk.verification.sampleApp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.EventLocalUserUpdated;
import com.palomamobile.android.sdk.verification.email.EventEmailVerificationCreated;
import com.palomamobile.android.sdk.verification.email.IEmailVerificationManager;
import com.palomamobile.android.sdk.verification.email.JobCreateEmailVerification;
import com.palomamobile.android.sdk.verification.email.JobPostUserVerifiedEmail;
import com.path.android.jobqueue.JobManager;

public class VerificationSampleActivity extends Activity {

    private static final String TAG = VerificationSampleActivity.class.getSimpleName();

    private IEmailVerificationManager emailVerificationManager = ServiceSupport.Instance.getServiceManager(IEmailVerificationManager.class);
    private JobManager jobManager = ServiceSupport.Instance.getJobManager();

    private TextView textViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceSupport.Instance.getEventBus().register(this);
        setContentView(R.layout.activity_verification_sample);

        textViewStatus = (TextView) findViewById(R.id.textViewStatus);

        final EditText etEmail = (EditText) findViewById(R.id.editTextEmail) ;
        Button btnEmail = (Button) findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(VerificationSampleActivity.this, "Enter an email address", Toast.LENGTH_LONG).show();
                }
                else {
                    JobCreateEmailVerification jobCreateEmailVerification = emailVerificationManager.createJobCreateEmailVerification(email);
                    jobManager.addJob(jobCreateEmailVerification);
                    textViewStatus.setText("Started step 1. of Email verification");
                }
            }
        });
        final EditText etCode = (EditText) findViewById(R.id.editTextCode);
        Button btnVerify = (Button) findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(VerificationSampleActivity.this, "Enter an email address", Toast.LENGTH_LONG).show();
                    return;
                }
                String code = etCode.getText().toString();
                if (code.isEmpty()) {
                    Toast.makeText(VerificationSampleActivity.this, "Enter verification code", Toast.LENGTH_LONG).show();
                    return;
                }
                JobPostUserVerifiedEmail jobPostUserVerifiedEmail = emailVerificationManager.createJobPostUserVerifiedEmail(email, code);
                jobManager.addJob(jobPostUserVerifiedEmail);
                textViewStatus.setText("Started step 2. of Email verification");
            }
        });
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventEmailVerificationCreated event) {
        Log.d(TAG, "onEventMainThread(): " + event);
        Throwable throwable = event.getFailure();
        if (throwable == null) {
            textViewStatus.setText("Step 1. of User email verification successful. Check your email for the verification code needed in the next step.");
        }
        else {
            textViewStatus.setText("Step 1. of User email verification failed: " + event.getFailure());
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventLocalUserUpdated event) {
        Log.d(TAG, "onEventMainThread(): " + event);
        Throwable throwable = event.getFailure();
        if (throwable == null) {
            textViewStatus.setText("Step 2. of User email verification successful. Email verification complete.\n" + event.getSuccess().toString());
        }
        else {
            textViewStatus.setText("Step 2. of User email verification failed: " + event.getFailure());
        }
    }

    @Override
    protected void onDestroy() {
        ServiceSupport.Instance.getEventBus().unregister(this);
        super.onDestroy();
    }
}
