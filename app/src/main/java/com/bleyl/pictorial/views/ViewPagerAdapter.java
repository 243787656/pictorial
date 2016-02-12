package com.bleyl.pictorial.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.bleyl.pictorial.R;
import com.bleyl.pictorial.models.Image;
import com.bleyl.pictorial.views.widgets.GifVideoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewPagerAdapter extends PagerAdapter {

    @Bind(R.id.gif) GifVideoView mGifVideoView;
    @Bind(R.id.image) PhotoView mPhotoView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.gif_frame) FrameLayout mFrameLayout;

    private List<Image> mImageList;
    private Context mContext;
    private ViewPagerListener mListener;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    public ViewPagerAdapter(Context context, List<Image> imageList, ViewPagerListener viewPagerListener) {
        mContext = context;
        mImageList = imageList;
        mListener = viewPagerListener;
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .build();
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        ViewGroup layout = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.view_page_view, viewGroup, false);
        ButterKnife.bind(this, layout);
        if (mImageList.get(position).isAnimated()) {
            showVideo(position);
        } else {
            showImage(position);
        }
        viewGroup.addView(layout);
        return layout;
    }

    public void showImage(int position) {
        mImageLoader.displayImage(mImageList.get(position).getLink(), mPhotoView, mOptions, new ImageLoadingListener() {
            @Override public void onLoadingStarted(String imageUri, View view) {}
            @Override public void onLoadingCancelled(String imageUri, View view) {}

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d("loadImage failure", failReason.getType().toString());
                mListener.showError("Failed loading image: " + failReason.getType().toString());
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
        mPhotoView.setVisibility(View.VISIBLE);
        mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                mListener.stopService();
            }
        });
    }

    public void showVideo(int position) {
        if (mImageList.get(position).hasMP4Link()) {
            mFrameLayout.setVisibility(View.VISIBLE);
            mGifVideoView.setZOrderOnTop(true);
            mGifVideoView.start();
            mGifVideoView.setVideoPath(mImageList.get(position).getMP4Link());
            mGifVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                        @Override
                        public void onBufferingUpdate(MediaPlayer mp, int percent) {
                            if (percent == 100) {
                                mProgressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });
        }
    }

    @OnClick({ R.id.gif_frame, R.id.relative_layout })
    public void closeService() {
        mListener.stopService();
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object object) {
        collection.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}