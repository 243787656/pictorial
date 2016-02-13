package com.bleyl.pictorial.presenter;

import android.util.Log;

import com.bleyl.pictorial.App;
import com.bleyl.pictorial.utils.LinkUtil;
import com.bleyl.pictorial.model.DirectImage;
import com.bleyl.pictorial.model.Image;
import com.bleyl.pictorial.model.gfycat.GfyItem;
import com.bleyl.pictorial.model.gfycat.responses.MetadataResponse;
import com.bleyl.pictorial.model.imgur.ImgurGallery;
import com.bleyl.pictorial.model.imgur.responses.AlbumResponse;
import com.bleyl.pictorial.model.imgur.responses.GalleryResponse;
import com.bleyl.pictorial.model.imgur.responses.ImageResponse;
import com.bleyl.pictorial.utils.NetworkUtil;
import com.bleyl.pictorial.view.ViewerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewerPresenterImpl implements Presenter<ViewerView> {

    public static String TAG = ViewerPresenterImpl.class.getSimpleName();

    private ViewerView mView;
    private Subscription mSubscription;
    private List<Image> mImageList = new ArrayList<>();

    @Override
    public void attachView(ViewerView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadUrl(String url) {
        if (NetworkUtil.isOnline(mView.getContext())) {
            switch (LinkUtil.getLinkType(url)) {
                case IMGUR_GALLERY: loadImgurGallery(url); break;
                case IMGUR_ALBUM: loadImgurAlbum(url); break;
                case IMGUR_IMAGE: loadImgurImage(url); break;
                case GFYCAT: loadGfycat(url); break;
                case DIRECT_GIF: loadGif(url); break;
                case DIRECT_IMAGE: loadImage(url); break;
                case NONE: mView.showError("Link not supported"); break;
            }
        } else {
            mView.showError("No internet connection");
        }
    }

    public void loadImgurImage(String url) {
        Log.d(TAG, "Load Imgur image", null);
        if (mSubscription != null) mSubscription.unsubscribe();
        App application = App.get(mView.getContext());
        mSubscription = application.getImgurService().getImageDetails(LinkUtil.getImgurId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ImageResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error loading Image ", error);
                        mView.showError("Error loading Image " + error);
                    }

                    @Override
                    public void onSuccess(ImageResponse response) {
                        if (response.data != null) {
                            mImageList.add(response.data);
                            mView.showImages(mImageList);
                        } else {
                            onError(new Throwable());
                        }
                    }
                });
    }

    public void loadImgurAlbum(String url) {
        Log.d(TAG, "Load Imgur album", null);
        if (mSubscription != null) mSubscription.unsubscribe();
        App application = App.get(mView.getContext());
        mSubscription = application.getImgurService().getAlbumImages(LinkUtil.getImgurAlbumId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AlbumResponse>() {
                    @Override
                    public void onCompleted() {
                        mView.showImages(mImageList);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error loading album ", error);
                        mView.showError("Error loading album " + error);
                        if (error instanceof HttpException) {
                            Log.e(TAG, ((HttpException) error).response().message(), null);
                        }
                    }

                    @Override
                    public void onNext(AlbumResponse response) {
                        if (response.data != null) {
                            mImageList.addAll(response.data.getAlbumImages());
                        } else {
                            onError(new Throwable());
                        }
                    }
                });
    }

    public void loadImgurGallery(final String url) {
        Log.d(TAG, "Get gallery details", null);
        if (mSubscription != null) mSubscription.unsubscribe();
        App application = App.get(mView.getContext());
        mSubscription = application.getImgurService().getGalleryDetails(LinkUtil.getImgurGalleryId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<GalleryResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error getting gallery details ", error);
                        mView.showError("Error getting gallery details " + error);
                    }

                    @Override
                    public void onSuccess(GalleryResponse response) {
                        ImgurGallery gallery = response.data;
                        if (gallery != null) {
                            if (gallery.isAlbum()) {
                                loadImgurGalleryAlbum(url);
                            } else {
                                loadImgurGalleryImage(url);
                            }
                        } else {
                            onError(new Throwable());
                        }
                    }
                });
    }

    public void loadImgurGalleryAlbum(String url) {
        Log.d(TAG, "Load gallery album", null);
        if (mSubscription != null) mSubscription.unsubscribe();
        App application = App.get(mView.getContext());
        mSubscription = application.getImgurService().getGalleryAlbum(LinkUtil.getImgurGalleryId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AlbumResponse>() {
                    @Override
                    public void onCompleted() {
                        mView.showImages(mImageList);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error loading gallery album ", error);
                        mView.showError("Error loading gallery album " + error);
                    }

                    @Override
                    public void onNext(AlbumResponse response) {
                        if (response.data != null) {
                            mImageList.addAll(response.data.getAlbumImages());
                        } else {
                            onError(new Throwable());
                        }
                    }
                });
    }

    public void loadImgurGalleryImage(String url) {
        Log.d(TAG, "Get gallery details", null);
        if (mSubscription != null) mSubscription.unsubscribe();
        App application = App.get(mView.getContext());
        mSubscription = application.getImgurService().getGalleryImage(LinkUtil.getImgurGalleryId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ImageResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error loading gallery image ", error);
                        mView.showError("Error loading gallery image " + error);
                    }

                    @Override
                    public void onSuccess(ImageResponse response) {
                        if (response.data != null) {
                            mImageList.add(response.data);
                            mView.showImages(mImageList);
                        } else {
                            onError(new Throwable());
                        }
                    }
                });
    }

    public void loadGfycat(String url) {
        Log.d(TAG, "Get gallery details", null);
        if (mSubscription != null) mSubscription.unsubscribe();
        App application = App.get(mView.getContext());
        mSubscription = application.getGfycatService().getMetadata(LinkUtil.getGfycatId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<MetadataResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error loading gfycat ", error);
                        mView.showError("Error loading gfycat " + error);
                    }

                    @Override
                    public void onSuccess(MetadataResponse response) {
                        if (response != null) {
                            mImageList.add(response.getGfyItem());
                            mView.showImages(mImageList);
                        }  else {
                            onError(new Throwable());
                        }
                    }
                });
    }

    public void loadGif(final String url) {
        Log.d(TAG, "Load direct gif", null);
        if (mSubscription != null) mSubscription.unsubscribe();
        App application = App.get(mView.getContext());
        mSubscription = application.getGfycatService().checkUrl(LinkUtil.getGfycatCompatibleUrl(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<GfyItem>() {
                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error checking gfycat url ", error);
                        mView.showError("Error checking gfycat url " + error);
                    }

                    @Override
                    public void onSuccess(GfyItem gfyItem) {
                        if (gfyItem != null) {
                            if (gfyItem.getMP4Link() != null) {
                                mImageList.add(gfyItem);
                                mView.showImages(mImageList);
                            } else {
                                convertAndLoadGif(url);
                            }
                        }  else {
                            onError(new Throwable());
                        }
                    }
                });
    }

    public void convertAndLoadGif(String url) {
        Log.d(TAG, "Upload gif to Gfycat", null);
        if (mSubscription != null) mSubscription.unsubscribe();
        App application = App.get(mView.getContext());
        mSubscription = application.getGfycatUploadService().uploadGif(LinkUtil.getGfycatCompatibleUrl(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<GfyItem>() {
                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error uploading gfycat url ", error);
                        mView.showError("Error uploading gfycat url " + error);
                    }

                    @Override
                    public void onSuccess(GfyItem gfyItem) {
                        if (gfyItem != null) {
                            if (gfyItem.getMP4Link() != null) {
                                mImageList.add(gfyItem);
                                mView.showImages(mImageList);
                            }
                        }  else {
                            onError(new Throwable());
                        }
                    }
                });
    }

    public void loadImage(String url) {
        Log.d(TAG, "Load direct image", null);
        mImageList.add(new DirectImage(url));
        mView.showImages(mImageList);
    }
}