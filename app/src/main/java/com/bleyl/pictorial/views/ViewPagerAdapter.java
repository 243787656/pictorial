package com.bleyl.pictorial.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.bleyl.pictorial.R;
import com.bleyl.pictorial.models.Image;
import com.bleyl.pictorial.utils.LoaderUtil;
import com.bleyl.pictorial.views.widgets.GifVideoView;

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

    public ViewPagerAdapter(Context context, List<Image> imageList, ViewPagerListener viewPagerListener){
        mContext = context;
        mImageList = imageList;
        mListener = viewPagerListener;
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
        LoaderUtil.loadImage(mContext, mPhotoView, mProgressBar, mImageList.get(position).getLink());
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
            LoaderUtil.loadVideo(mGifVideoView, mProgressBar, mImageList.get(position).getMP4Link());
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