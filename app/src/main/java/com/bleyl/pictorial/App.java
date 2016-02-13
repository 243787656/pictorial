package com.bleyl.pictorial;

import android.app.Application;
import android.content.Context;

import com.bleyl.pictorial.model.gfycat.GfycatService;
import com.bleyl.pictorial.model.gfycat.GfycatUploadService;
import com.bleyl.pictorial.model.imgur.ImgurService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import rx.Scheduler;
import rx.schedulers.Schedulers;

public class App extends Application {

    private ImgurService mImgurService;
    private GfycatService mGfycatService;
    private GfycatUploadService mGfycatUploadService;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(400)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                        .Builder(this)
                        .defaultDisplayImageOptions(defaultOptions).build();

        ImageLoader.getInstance().init(config);
    }

    public ImgurService getImgurService() {
        if (mImgurService == null) {
            mImgurService = ImgurService.Factory.create();
        }
        return mImgurService;
    }

    public GfycatService getGfycatService() {
        if (mGfycatService == null) {
            mGfycatService = GfycatService.Factory.create();
        }
        return mGfycatService;
    }

    public GfycatUploadService getGfycatUploadService() {
        if (mGfycatUploadService == null) {
            mGfycatUploadService = GfycatUploadService.Factory.create();
        }
        return mGfycatUploadService;
    }
}