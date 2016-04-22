package com.bleyl.pictorial.view;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bleyl.pictorial.R;
import com.bleyl.pictorial.model.Image;
import com.bleyl.pictorial.view.widgets.GifVideoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewPagerAdapter extends PagerAdapter {

    public static String TAG = ViewPagerAdapter.class.getSimpleName();

    private List<Image> mImageList;
    private Context mContext;
    private ViewPagerListener mListener;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    public static class ViewHolder {
        @Bind(R.id.relative_layout) RelativeLayout mRootLayout;
        @Bind(R.id.gif) GifVideoView mGifVideoView;
        @Bind(R.id.error_text) TextView mErrorText;
        @Bind(R.id.image) PhotoView mPhotoView;
        @Bind(R.id.progress_bar) ProgressBar mProgressBar;
        @Bind(R.id.gif_frame) FrameLayout mFrameLayout;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

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

    public View getView(int position, ViewGroup parent) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.view_page_view, parent, false);
        ViewHolder holder = new ViewHolder(layout);
        if (mImageList.get(position).isAnimated()) {
            showVideo(mImageList.get(position), holder);
        } else {
            showImage(mImageList.get(position), holder);
        }
        holder.mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.stopService();
            }
        });
        return layout;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        View layout = getView(position, viewGroup);
        viewGroup.addView(layout);
        return layout;
    }

    public void showImage(Image image, final ViewHolder holder) {
        mImageLoader.displayImage(image.getLink(), holder.mPhotoView, mOptions, new ImageLoadingListener() {
            @Override public void onLoadingStarted(String imageUri, View view) {}
            @Override public void onLoadingCancelled(String imageUri, View view) {}

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                showError(holder.mErrorText, "Failed loading image: " + failReason.getType().toString());
                holder.mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.mProgressBar.setVisibility(View.GONE);
            }
        });
        holder.mPhotoView.setVisibility(View.VISIBLE);
        holder.mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                mListener.stopService();
            }
        });
    }

    public void showVideo(Image image, final ViewHolder holder) {
        if (image.hasMP4Link()) {
            holder.mFrameLayout.setVisibility(View.VISIBLE);
            holder.mGifVideoView.setZOrderOnTop(true);
            holder.mGifVideoView.start();
            holder.mGifVideoView.setVideoPath(image.getMP4Link());
            holder.mGifVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    holder.mProgressBar.setVisibility(View.GONE);
                }
            });
            holder.mGifVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                            showError(holder.mErrorText, "Unknown media playback error");
                            break;
                        case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                            showError(holder.mErrorText, "Server connection died");
                    }
                    return true;
                }
            });
            holder.mFrameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.stopService();
                }
            });
        }
    }

    public void showError(TextView errorText, String string) {
        errorText.setText(string);
        errorText.setVisibility(View.VISIBLE);
        Log.e(TAG, string);
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