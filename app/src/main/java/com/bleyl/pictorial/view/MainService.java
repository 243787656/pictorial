package com.bleyl.pictorial.view;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bleyl.pictorial.R;
import com.bleyl.pictorial.model.Image;
import com.bleyl.pictorial.presenter.MainPresenter;
import com.bleyl.pictorial.view.layouts.ViewPager;
import com.bleyl.pictorial.view.layouts.WrapperLayout;
import com.bleyl.pictorial.view.widgets.FractionView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainService extends Service implements MainMvpView, ViewPagerListener {

    @Bind(R.id.view_pager) ViewPager mViewPager;
    @Bind(R.id.error_text) TextView mErrorText;
    @Bind(R.id.fraction_view) FractionView mFractionView;

    private MainPresenter mPresenter;
    private WrapperLayout mLayout;
    private WindowManager mWindowManager;

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        startForegroundService();
        mPresenter.loadUrl(intent.getStringExtra("URL"));
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
        mLayout = (WrapperLayout) LayoutInflater.from(this).inflate(R.layout.main_service, new LinearLayout(this), false);
        ButterKnife.bind(this, mLayout);

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                PixelFormat.TRANSLUCENT);
        params.dimAmount = 0.7f;
        mWindowManager.addView(mLayout, params);

        mLayout.addOnCloseDialogsListener(new WrapperLayout.OnCloseDialogsListener() {
            @Override
            public void onCloseDialogs(WrapperLayout.Reason reason) {
                stopSelf();
            }
        });
    }

    @Override
    @OnClick({ R.id.close_button, R.id.wrapper_layout })
    public void stopService() {
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void showImages(List<Image> imageList) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageList, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        if (imageList.size() > 1) {
            showAlbumIndicator(imageList);
        }
    }

    public void showAlbumIndicator(List<Image> imageList) {
        mFractionView.setVisibility(View.VISIBLE);
        mFractionView.setMaxNumber(imageList.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrollStateChanged(int state) {}
            @Override public void onPageSelected(int position) {}

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mFractionView.setCurrentNumber(position + 1);
            }
        });
    }

    @Override
    public void showError(String error) {
        mErrorText.setText(error);
        mErrorText.setVisibility(View.VISIBLE);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        if (mLayout != null) {
            mWindowManager.removeView(mLayout);
        }
    }

    public int startForegroundService() {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .build();
        startForeground(1, notification);
        return START_STICKY;
    }
}