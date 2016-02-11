package com.bleyl.pictorial.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bleyl.pictorial.views.GifVideoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import uk.co.senab.photoview.PhotoView;

public class LoaderUtil {

    public static void loadImage(final Context context, PhotoView photoView, final ProgressBar progressBar, String url) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .build();

        imageLoader.displayImage(url, photoView, options, new ImageLoadingListener() {
            @Override public void onLoadingStarted(String imageUri, View view) {}
            @Override public void onLoadingCancelled(String imageUri, View view) {}

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d("loadImage failure", failReason.getType().toString());
                Toast.makeText(context, "Failed loading image: " + failReason.getType().toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }
        });
        photoView.setVisibility(View.VISIBLE);
    }

    public static void loadVideo(GifVideoView gifVideoView, final ProgressBar progressBar, String url) {
        gifVideoView.setVisibility(View.VISIBLE);
        gifVideoView.setZOrderOnTop(true);
        gifVideoView.start();
        gifVideoView.setVideoPath(url);
        gifVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        if (percent == 100) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }
}