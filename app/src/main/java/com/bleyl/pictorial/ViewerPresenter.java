package com.bleyl.pictorial;

import com.bleyl.pictorial.models.gfycat.GfycatClient;
import com.bleyl.pictorial.models.gfycat.GfycatUploadClient;
import com.bleyl.pictorial.models.imgur.ImgurClient;
import com.bleyl.pictorial.utils.LinkUtil;
import com.bleyl.pictorial.models.DirectImage;
import com.bleyl.pictorial.models.Image;
import com.bleyl.pictorial.models.gfycat.GfyItem;
import com.bleyl.pictorial.models.gfycat.responses.MetadataResponse;
import com.bleyl.pictorial.models.imgur.responses.AlbumResponse;
import com.bleyl.pictorial.models.imgur.responses.GalleryResponse;
import com.bleyl.pictorial.models.imgur.responses.ImageResponse;

import java.util.ArrayList;
import java.util.List;

import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewerPresenter {

    private ViewerView mView;
    private Subscription mSubscription;
    private List<Image> mImageList = new ArrayList<>();

    public ViewerPresenter(ViewerView view) {
        mView = view;
    }

    public void detachView() {
        mView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadUrl(String url) {
        switch (LinkUtil.getLinkType(url)) {
            case IMGUR_GALLERY: loadImgurGallery(url); break;
            case IMGUR_ALBUM: loadImgurAlbum(url); break;
            case IMGUR_IMAGE: loadImgurImage(url); break;
            case GFYCAT: loadGfycat(url); break;
            case DIRECT_GIF: loadGif(url); break;
            case DIRECT_IMAGE: loadImage(url); break;
            case NONE: mView.showError("Link not supported"); break;
        }
    }

    public void loadImgurImage(String url) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = ImgurClient.getService().getImageDetails(LinkUtil.getImgurId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ImageResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.showError("Error loading Image " + error);
                    }

                    @Override
                    public void onSuccess(ImageResponse response) {
                        if (response.data != null && response.success) {
                            mImageList.add(response.data);
                            mView.showImages(mImageList);
                        } else {
                            mView.showError("Error loading Image");
                        }
                    }
                });
    }

    public void loadImgurAlbum(String url) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = ImgurClient.getService().getAlbumImages(LinkUtil.getImgurAlbumId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AlbumResponse>() {
                    @Override
                    public void onCompleted() {
                        mView.showImages(mImageList);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mView.showError("Error loading album " + error);
                    }

                    @Override
                    public void onNext(AlbumResponse response) {
                        if (response.data != null && response.success) {
                            mImageList.addAll(response.data.getAlbumImages());
                        } else {
                            mView.showError("Error loading album");
                        }
                    }
                });
    }

    public void loadImgurGallery(final String url) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = ImgurClient.getService().getGalleryDetails(LinkUtil.getImgurGalleryId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<GalleryResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.showError("Error getting gallery details " + error);
                    }

                    @Override
                    public void onSuccess(GalleryResponse response) {
                        if (response.data != null && response.success) {
                            if (response.data.isAlbum()) {
                                loadImgurGalleryAlbum(url);
                            } else {
                                loadImgurGalleryImage(url);
                            }
                        } else {
                            mView.showError("Error getting gallery details");
                        }
                    }
                });
    }

    public void loadImgurGalleryAlbum(String url) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = ImgurClient.getService().getGalleryAlbum(LinkUtil.getImgurGalleryId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AlbumResponse>() {
                    @Override
                    public void onCompleted() {
                        mView.showImages(mImageList);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mView.showError("Error loading gallery album " + error);
                    }

                    @Override
                    public void onNext(AlbumResponse response) {
                        if (response.data != null && response.success) {
                            mImageList.addAll(response.data.getAlbumImages());
                        } else {
                            mView.showError("Error loading gallery album");
                        }
                    }
                });
    }

    public void loadImgurGalleryImage(String url) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = ImgurClient.getService().getGalleryImage(LinkUtil.getImgurGalleryId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ImageResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.showError("Error loading gallery image " + error);
                    }

                    @Override
                    public void onSuccess(ImageResponse response) {
                        if (response.data != null && response.success) {
                            mImageList.add(response.data);
                            mView.showImages(mImageList);
                        } else {
                            mView.showError("Error loading gallery image");
                        }
                    }
                });
    }

    public void loadGfycat(String url) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = GfycatClient.getService().getMetadata(LinkUtil.getGfycatId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<MetadataResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.showError("Error loading gfycat " + error);
                    }

                    @Override
                    public void onSuccess(MetadataResponse response) {
                        if (response.getGfyItem() != null && response.getGfyItem().getMP4Link() != null) {
                            mImageList.add(response.getGfyItem());
                            mView.showImages(mImageList);
                        }  else {
                            mView.showError("Error loading gfycat");
                        }
                    }
                });
    }

    public void loadGif(final String url) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = GfycatClient.getService().checkUrl(LinkUtil.getGfycatCompatibleUrl(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<GfyItem>() {
                    @Override
                    public void onError(Throwable error) {
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
                            mView.showError("Error checking gfycat url");
                        }
                    }
                });
    }

    public void convertAndLoadGif(String url) {
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = GfycatUploadClient.getService().uploadGif(LinkUtil.getGfycatCompatibleUrl(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<GfyItem>() {
                    @Override
                    public void onError(Throwable error) {
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
        mImageList.add(new DirectImage(url));
        mView.showImages(mImageList);
    }
}