package com.bleyl.pictorial.models.imgur;

import android.text.TextUtils;

import com.bleyl.pictorial.models.Image;
import com.google.gson.annotations.SerializedName;

public class ImgurImage implements Image {

    @SerializedName("animated")
    private boolean isAnimated;

    @SerializedName("size")
    private long size;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("link")
    private String link;

    @SerializedName("mp4")
    private String mp4Link;

    @SerializedName("favorite")
    private boolean isFavourite;

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public boolean isAnimated() {
        return isAnimated;
    }

    @Override
    public boolean hasMP4Link() {
        return !TextUtils.isEmpty(mp4Link);
    }

    @Override
    public String getMP4Link() {
        return mp4Link;
    }

    @Override
    public boolean hasDescription() {
        return !TextUtils.isEmpty(description);
    }

    @Override
    public String getDescription() {
        return description;
    }
}