package com.bleyl.pictorial.view;

import android.content.Context;

import com.bleyl.pictorial.model.Image;

import java.util.List;

public interface MainMvpView {

    Context getContext();

    void showImages(List<Image> imageList);

    void showError(String string);
}