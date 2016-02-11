package com.bleyl.pictorial.models.imgur;

import android.text.TextUtils;

import com.bleyl.pictorial.models.Image;
import com.google.gson.annotations.SerializedName;

public class ImgurImage implements Image {

    @SerializedName("animated")
    private boolean mIsAnimated;

    @SerializedName("size")
    private long mSize;

    @SerializedName("id")
    private String mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("link")
    private String mLink;

    @SerializedName("mp4")
    private String mMP4Link;

    @SerializedName("favorite")
    private boolean mIsFavourite;

    @Override
    public String getLink() {
        return mLink;
    }

    @Override
    public boolean isAnimated() {
        return mIsAnimated;
    }

    @Override
    public boolean hasMP4Link() {
        return !TextUtils.isEmpty(mMP4Link);
    }

    @Override
    public String getMP4Link() {
        return mMP4Link;
    }

    @Override
    public boolean hasDescription() {
        return !TextUtils.isEmpty(mDescription);
    }

    @Override
    public String getDescription() {
        return mDescription;
    }
}