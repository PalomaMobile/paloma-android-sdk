package com.palomamobile.android.sdk.notification.sampleApp;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.notification.EventEchoNotificationRequested;
import com.palomamobile.android.sdk.notification.EventNotificationReceived;
import com.palomamobile.android.sdk.notification.JobPostEchoNotification;
import com.palomamobile.android.sdk.notification.Notification;
import com.palomamobile.android.sdk.user.IUserManager;
import com.palomamobile.android.sdk.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class NotificationSampleActivity extends Activity {

    private static final Logger logger = LoggerFactory.getLogger(NotificationSampleActivity.class);

    private View contentLayout;
    private View progressIndicator;
    private TextView notificationContent;
    private Button requestNotificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceSupport.Instance.getEventBus().register(this);
        setContentView(R.layout.request_notification);

        progressIndicator = findViewById(R.id.progressBar);
        contentLayout = findViewById(R.id.contentLayout);
        notificationContent = (TextView) findViewById(R.id.notificationContent);
        requestNotificationButton = (Button) findViewById(R.id.requestButton);

        requestNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestEchoNotification();
                setUiBusy(true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceSupport.Instance.getEventBus().unregister(this);
    }

    private void requestEchoNotification() {
        User user = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser();
        Notification echo = new Notification();
        long curTime = System.currentTimeMillis();
        echo.setType("SampleApp");
        echo.setRecipientId(user.getId());
        echo.setContent("current time: " + curTime);
        echo.setDebug("just some debug info @ " + curTime);
        echo.setUrl("http://www.google.com");
        echo.setSenderId(user.getId());
        echo.setTimestamp(System.currentTimeMillis());

        JobPostEchoNotification jobPostEchoNotification = new JobPostEchoNotification(echo);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobPostEchoNotification);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventEchoNotificationRequested event) {
        logger.debug("onEventMainThread(): " + event);
        Throwable throwable = event.getFailure();
        int msgId = throwable == null ? R.string.notification_requested_success : R.string.notification_requested_err;
        Toast.makeText(this, msgId, Toast.LENGTH_LONG).show();
        if (throwable != null) {
            notificationContent.setText("Error: " + throwable.getLocalizedMessage());
        }
        setUiBusy(false);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventNotificationReceived event) {
        logger.debug("onEventMainThread(): " + event);
        notificationContent.setText(event.getSuccess().toString());
        setUiBusy(false);
    }

    private void setUiBusy(boolean busy) {
        progressIndicator.setVisibility(busy ? View.VISIBLE : View.GONE);
        contentLayout.setVisibility(busy ? View.GONE : View.VISIBLE);
    }
}
