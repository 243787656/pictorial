package com.bleyl.pictorial.model.imgur;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImgurAlbum {

    @SerializedName("images")
    private List<ImgurImage> mImageList;

    public List<ImgurImage> getAlbumImages() {
        return mImageList;
    }

    public boolean hasAlbumImages() {
        return mImageList != null && !mImageList.isEmpty();
    }
}