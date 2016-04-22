package com.bleyl.pictorial;

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

import com.bleyl.pictorial.models.Image;
import com.bleyl.pictorial.widgets.GifVideoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewerAdapter extends PagerAdapter {

    public static String TAG = ViewerAdapter.class.getSimpleName();

    private List<Image> imageList;
    private Context context;
    private ViewPagerListener listener;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public static class ViewHolder {
        @Bind(R.id.relative_layout) RelativeLayout rootLayout;
        @Bind(R.id.gif) GifVideoView gifVideoView;
        @Bind(R.id.error_text) TextView errorText;
        @Bind(R.id.image) PhotoView photoView;
        @Bind(R.id.progress_bar) ProgressBar progressBar;
        @Bind(R.id.gif_frame) FrameLayout frameLayout;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface ViewPagerListener {
        void stopService();
    }

    public ViewerAdapter(Context context, List<Image> imageList, ViewPagerListener viewPagerListener) {
        this.context = context;
        this.imageList = imageList;
        listener = viewPagerListener;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .build();
    }

    public View getView(int position, ViewGroup parent) {
        View layout = LayoutInflater.from(context).inflate(R.layout.view_page_view, parent, false);
        ViewHolder holder = new ViewHolder(layout);
        if (imageList.get(position).isAnimated()) {
            showVideo(imageList.get(position), holder);
        } else {
            showImage(imageList.get(position), holder);
        }
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.stopService();
            }
        });
        return layout;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        View layout = getView(position, viewGroup);
        viewGroup.addView(layout);
        return layout;
    }

    public void showImage(Image image, final ViewHolder holder) {
        imageLoader.displayImage(image.getLink(), holder.photoView, options, new ImageLoadingListener() {
            @Override public void onLoadingStarted(String imageUri, View view) {}
            @Override public void onLoadingCancelled(String imageUri, View view) {}

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                showError(holder.errorText, "Failed loading image: " + failReason.getType().toString());
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);
            }
        });
        holder.photoView.setVisibility(View.VISIBLE);
        holder.photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                listener.stopService();
            }
        });
    }

    public void showVideo(Image image, final ViewHolder holder) {
        if (image.hasMP4Link()) {
            holder.frameLayout.setVisibility(View.VISIBLE);
            holder.gifVideoView.setZOrderOnTop(true);
            holder.gifVideoView.start();
            holder.gifVideoView.setVideoPath(image.getMP4Link());
            holder.gifVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    holder.progressBar.setVisibility(View.GONE);
                }
            });
            holder.gifVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                            showError(holder.errorText, "Unknown media playback error");
                            break;
                        case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                            showError(holder.errorText, "Server connection died");
                    }
                    return true;
                }
            });
            holder.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.stopService();
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