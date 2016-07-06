package com.bogueratcreations.eaftoolkit;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {
    String name;//, url;
    Integer url;
    public ImageModel() {

    }

    protected ImageModel(Parcel in) {
        name = in.readString();
        //url = in.readString();
        url = in.readInt();
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUrl() {
        return url;
    }  // Changed to Integer

    public void setUrl(Integer url) {  // Changed to integer
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        //dest.writeString(url);
        dest.writeInt(url);
    }
}
