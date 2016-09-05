package com.bleyl.pictorial.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImgurAlbum {

    @SerializedName("images")
    private List<ImgurImage> imageList;

    public List<ImgurImage> getAlbumImages() {
        return imageList;
    }

    public boolean hasAlbumImages() {
        return imageList != null && !imageList.isEmpty();
    }
}