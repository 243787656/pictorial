package com.bleyl.pictorial.models;

import com.google.gson.annotations.SerializedName;

public class ImgurGallery {

    @SerializedName("is_album")
    private boolean isAlbum;

    public boolean isAlbum() {
        return isAlbum;
    }
}