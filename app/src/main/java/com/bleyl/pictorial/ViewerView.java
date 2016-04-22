package com.bleyl.pictorial;

import android.content.Context;

import com.bleyl.pictorial.models.Image;

import java.util.List;

public interface ViewerView {

    Context getContext();

    void showImages(List<Image> imageList);

    void showError(String string);
}