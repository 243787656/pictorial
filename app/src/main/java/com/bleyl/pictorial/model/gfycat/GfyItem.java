package com.bleyl.pictorial.model.gfycat;

import com.bleyl.pictorial.model.Image;
import com.google.gson.annotations.SerializedName;

public class GfyItem implements Image {

    @SerializedName("mp4Url")
    private String mMp4Link;

    @Override
    public boolean hasDescription() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getLink() {
        return null;
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    @Override
    public boolean hasMP4Link() {
        return true;
    }

    @Override
    public String getMP4Link() {
        return mMp4Link;
    }
}