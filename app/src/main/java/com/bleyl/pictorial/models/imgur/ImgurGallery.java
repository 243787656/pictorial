package com.bleyl.pictorial.models.imgur;

import com.google.gson.annotations.SerializedName;

public class ImgurGallery {

    @SerializedName("is_album")
    private boolean mIsAlbum;

    public boolean isAlbum() {
        return mIsAlbum;
    }
}