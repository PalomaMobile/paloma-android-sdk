package com.palomamobile.android.sdk.core.util;

import android.util.Log;
import retrofit.RestAdapter;

/**
 * Custom log implementation that truncates log lines to a maximum size. Useful when logging binary file transfers etc.
 * <br/>
 *
 */
public class TruncatedAndroidLog implements RestAdapter.Log {

    public static final int DEFAULT_TRUNCATED_LOG_CHUNK_SIZE = 4000;
    public static final String DEFAULT_LOG_TAG = "com.palomamobile";

    private int maxMsgLogLength;
    private String tag;

    /**
     * Same as calling {@link #TruncatedAndroidLog(int, String)} with {@link #DEFAULT_TRUNCATED_LOG_CHUNK_SIZE} and {@link #DEFAULT_LOG_TAG}.
     */
    public TruncatedAndroidLog() {
        this(DEFAULT_TRUNCATED_LOG_CHUNK_SIZE, DEFAULT_LOG_TAG);
    }

    /**
     * Construct a new instance with a specified maximum log line length and log tag.
     * @param maxMsgLogLength maximum log line length
     * @param tag log tag
     */
    public TruncatedAndroidLog(int maxMsgLogLength, String tag) {
        this.maxMsgLogLength = maxMsgLogLength;
        this.tag = tag;
    }

    @Override
    public void log(String message) {
        int end = Math.min(message.length(), getMaxMsgLogLength());
        Log.i(tag, message.substring(0, end));
        if (message.length() > DEFAULT_TRUNCATED_LOG_CHUNK_SIZE) {
            Log.i(tag, "[... truncated]");
        }
    }

    /**
     * @return maximum log line length
     */
    public int getMaxMsgLogLength() {
        return maxMsgLogLength;
    }

    /**
     * @param maxMsgLogLength maximum log line length
     */
    public void setMaxMsgLogLength(int maxMsgLogLength) {
        this.maxMsgLogLength = maxMsgLogLength;
    }

    /**
     * @return log tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag log tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
}
