package com.palomamobile.android.sdk.media.sampleApp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.media.BaseJobUploadMedia;
import com.palomamobile.android.sdk.media.EventMediaUploaded;
import com.palomamobile.android.sdk.media.IMediaManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MediaSampleActivity extends Activity {

    private static final Logger logger = LoggerFactory.getLogger(MediaSampleActivity.class);

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int PICK_FROM_GALLERY = 2;

    private View contentLayout;
    private View progressIndicator;
    private TextView tvUrl;
    private Button requestUploadButton;
    private Button showInBrowserButton;
    private CheckBox checkBoxPrivate;
    private IMediaManager mediaManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceSupport.Instance.getEventBus().register(this);
        mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);

        setContentView(R.layout.activity_media_sample);

        progressIndicator = findViewById(R.id.progressBar);
        contentLayout = findViewById(R.id.contentLayout);
        tvUrl = (TextView) findViewById(R.id.textViewUrl);

        checkBoxPrivate = (CheckBox) findViewById(R.id.checkBoxPrivate);

        requestUploadButton = (Button) findViewById(R.id.buttonUpload);
        requestUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                in.setType("image/*");
                startActivityForResult(in, RESULT_LOAD_IMAGE);
            }
        });
        showInBrowserButton = (Button) findViewById(R.id.buttonView);
        showInBrowserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = tvUrl.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private void setUiBusy(boolean busy) {
        progressIndicator.setVisibility(busy ? View.VISIBLE : View.GONE);
        contentLayout.setVisibility(busy ? View.GONE : View.VISIBLE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            BaseJobUploadMedia jobUploadMedia = checkBoxPrivate.isChecked()
                    ? mediaManager.createJobMediaUploadPrivate("image/jpg", picturePath)
                    : mediaManager.createJobMediaUploadPublic("image/jpg", picturePath);
            ServiceSupport.Instance.getJobManager().addJobInBackground(jobUploadMedia);
            setUiBusy(true);
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventMediaUploaded event) {
        logger.debug("onEventMainThread(): " + event);
        Throwable throwable = event.getFailure();
        if (throwable != null) {
            Toast.makeText(this, "Media upload failed", Toast.LENGTH_LONG).show();
        }
        else {
            String expiringPublicUrl = event.getSuccess().getExpiringPublicUrl();
            String baseMediaUrl = event.getSuccess().getUrl();

            String publicMediaUrl = null;
            if (expiringPublicUrl != null) {
                //this means that baseMediaUrl is private and wouldn't be accessible through browser
                publicMediaUrl = expiringPublicUrl;
            }
            else {
                publicMediaUrl = baseMediaUrl;
            }
            tvUrl.setText(publicMediaUrl);
            setUiBusy(false);
        }
    }

}
