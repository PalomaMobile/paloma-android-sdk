package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.qos.BaseJobEvent;

import java.io.File;

/**
 */
public class EventReliableDownloadCompleted extends BaseJobEvent<JobReliableDownload, File> {
    public EventReliableDownloadCompleted(JobReliableDownload job, Throwable failure) {
        super(job, failure);
    }

    public EventReliableDownloadCompleted(JobReliableDownload job, File file) {
        super(job, file);
    }
}
