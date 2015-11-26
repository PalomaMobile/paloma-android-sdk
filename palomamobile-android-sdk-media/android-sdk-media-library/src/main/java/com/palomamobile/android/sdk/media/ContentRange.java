package com.palomamobile.android.sdk.media;

import java.io.Serializable;

/**
 * Created by Karel Herink
 */
public class ContentRange implements Serializable {

    private int start;
    private int end;

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "ContentRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
