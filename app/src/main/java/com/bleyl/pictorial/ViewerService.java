package com.bleyl.pictorial;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bleyl.pictorial.models.Image;
import com.bleyl.pictorial.layouts.ViewPager;
import com.bleyl.pictorial.layouts.WrapperLayout;
import com.bleyl.pictorial.utils.IntentUtil;
import com.bleyl.pictorial.widgets.FractionView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewerService extends Service implements ViewerView, ViewerAdapter.ViewPagerListener {

    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.error_text) TextView errorText;
    @BindView(R.id.info_texts) LinearLayout linearLayout;
    @BindView(R.id.fraction_view) FractionView fractionView;

    private ViewerPresenter presenter;
    private WrapperLayout layout;
    private WindowManager windowManager;
    private String url;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getStringExtra("URL");
        presenter.loadUrl(url);
        return startForegroundService();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        presenter = new ViewerPresenter(this);
        layout = (WrapperLayout) LayoutInflater.from(this).inflate(R.layout.main_service, new LinearLayout(this), false);
        ButterKnife.bind(this, layout);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                PixelFormat.TRANSLUCENT);
        params.dimAmount = 0.7f;
        windowManager.addView(layout, params);

        layout.addOnCloseDialogsListener(new WrapperLayout.OnCloseDialogsListener() {
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

    @OnClick({ R.id.browser_button, R.id.main_browser_button })
    public void openUrl() {
        Intent intent = IntentUtil.getDefaultBrowser(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        startActivity(intent);
        stopSelf();
    }

    @OnClick(R.id.share)
    public void shareUrl() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        Intent shareIntent = Intent.createChooser(intent, getString(R.string.share));
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(shareIntent);
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void showImages(List<Image> imageList) {
        ViewerAdapter adapter = new ViewerAdapter(this, imageList, this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        if (imageList.size() > 1) {
            showAlbumIndicator(imageList);
        }
    }

    public void showAlbumIndicator(List<Image> imageList) {
        fractionView.setVisibility(View.VISIBLE);
        fractionView.setMaxNumber(imageList.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrollStateChanged(int state) {}
            @Override public void onPageSelected(int position) {}

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                fractionView.setCurrentNumber(position + 1);
            }
        });
    }

    @Override
    public void showError(String error) {
        errorText.setText(error);
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (layout != null) {
            windowManager.removeView(layout);
        }
    }

    public int startForegroundService() {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .build();
        startForeground(1, notification);
        return START_NOT_STICKY;
    }
}