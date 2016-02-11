package com.bleyl.pictorial.views;

import android.content.Context;

import com.bleyl.pictorial.models.Image;

import java.util.List;

public interface MainMvpView {

    Context getContext();

    void showImages(List<Image> imageList);

    void showError(String string);
}