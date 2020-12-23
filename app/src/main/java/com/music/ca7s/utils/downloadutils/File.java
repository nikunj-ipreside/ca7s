package com.music.ca7s.utils.downloadutils;

import android.os.Parcel;
import android.os.Parcelable;

public class File implements Parcelable {
    private final long id;
    private final String url;
    private int progress;

    public File(long id, String url) {
        this.id = id;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url + " " + progress + " %";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.url);
        dest.writeInt(this.progress);
    }

    private File(Parcel in) {
        this.id = in.readLong();
        this.url = in.readString();
        this.progress = in.readInt();
    }

    public static final Parcelable.Creator<File> CREATOR = new Parcelable.Creator<File>() {
        public File createFromParcel(Parcel source) {
            return new File(source);
        }

        public File[] newArray(int size) {
            return new File[size];
        }
    };
}
