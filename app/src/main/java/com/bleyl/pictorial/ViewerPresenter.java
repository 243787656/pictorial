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

    private ViewerView view;
    private Subscription subscription;
    private List<Image> imageList = new ArrayList<>();

    public ViewerPresenter(ViewerView view) {
        this.view = view;
    }

    public void detachView() {
        view = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadUrl(String url) {
        switch (LinkUtil.getLinkType(url)) {
            case IMGUR_GALLERY: loadImgurGallery(url); break;
            case IMGUR_ALBUM: loadImgurAlbum(url); break;
            case IMGUR_IMAGE: loadImgurImage(url); break;
            case GFYCAT: loadGfycat(url); break;
            case DIRECT_GIF: loadGif(url); break;
            case DIRECT_IMAGE: loadImage(url); break;
            case NONE: view.showError("Link not supported"); break;
        }
    }

    public void loadImgurImage(String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = ImgurClient.getService().getImageDetails(LinkUtil.getImgurId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ImageResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError("Error loading Image " + error);
                    }

                    @Override
                    public void onSuccess(ImageResponse response) {
                        if (response.data != null && response.success) {
                            imageList.add(response.data);
                            view.showImages(imageList);
                        } else {
                            view.showError("Error loading Image");
                        }
                    }
                });
    }

    public void loadImgurAlbum(String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = ImgurClient.getService().getAlbumImages(LinkUtil.getImgurAlbumId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AlbumResponse>() {
                    @Override
                    public void onCompleted() {
                        view.showImages(imageList);
                    }

                    @Override
                    public void onError(Throwable error) {
                        view.showError("Error loading album " + error);
                    }

                    @Override
                    public void onNext(AlbumResponse response) {
                        if (response.data != null && response.success) {
                            imageList.addAll(response.data.getAlbumImages());
                        } else {
                            view.showError("Error loading album");
                        }
                    }
                });
    }

    public void loadImgurGallery(final String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = ImgurClient.getService().getGalleryDetails(LinkUtil.getImgurGalleryId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<GalleryResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError("Error getting gallery details " + error);
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
                            view.showError("Error getting gallery details");
                        }
                    }
                });
    }

    public void loadImgurGalleryAlbum(String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = ImgurClient.getService().getGalleryAlbum(LinkUtil.getImgurGalleryId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AlbumResponse>() {
                    @Override
                    public void onCompleted() {
                        view.showImages(imageList);
                    }

                    @Override
                    public void onError(Throwable error) {
                        view.showError("Error loading gallery album " + error);
                    }

                    @Override
                    public void onNext(AlbumResponse response) {
                        if (response.data != null && response.success) {
                            imageList.addAll(response.data.getAlbumImages());
                        } else {
                            view.showError("Error loading gallery album");
                        }
                    }
                });
    }

    public void loadImgurGalleryImage(String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = ImgurClient.getService().getGalleryImage(LinkUtil.getImgurGalleryId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ImageResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError("Error loading gallery image " + error);
                    }

                    @Override
                    public void onSuccess(ImageResponse response) {
                        if (response.data != null && response.success) {
                            imageList.add(response.data);
                            view.showImages(imageList);
                        } else {
                            view.showError("Error loading gallery image");
                        }
                    }
                });
    }

    public void loadGfycat(String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = GfycatClient.getService().getMetadata(LinkUtil.getGfycatId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<MetadataResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError("Error loading gfycat " + error);
                    }

                    @Override
                    public void onSuccess(MetadataResponse response) {
                        if (response.getGfyItem() != null && response.getGfyItem().getMP4Link() != null) {
                            imageList.add(response.getGfyItem());
                            view.showImages(imageList);
                        }  else {
                            view.showError("Error loading gfycat");
                        }
                    }
                });
    }

    public void loadGif(final String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = GfycatClient.getService().checkUrl(LinkUtil.getGfycatCompatibleUrl(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<GfyItem>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError("Error checking gfycat url " + error);
                    }

                    @Override
                    public void onSuccess(GfyItem gfyItem) {
                        if (gfyItem != null) {
                            if (gfyItem.getMP4Link() != null) {
                                imageList.add(gfyItem);
                                view.showImages(imageList);
                            } else {
                                convertAndLoadGif(url);
                            }
                        }  else {
                            view.showError("Error checking gfycat url");
                        }
                    }
                });
    }

    public void convertAndLoadGif(String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = GfycatUploadClient.getService().uploadGif(LinkUtil.getGfycatCompatibleUrl(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<GfyItem>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError("Error uploading gfycat url " + error);
                    }

                    @Override
                    public void onSuccess(GfyItem gfyItem) {
                        if (gfyItem != null) {
                            if (gfyItem.getMP4Link() != null) {
                                imageList.add(gfyItem);
                                view.showImages(imageList);
                            }
                        }  else {
                            onError(new Throwable());
                        }
                    }
                });
    }

    public void loadImage(String url) {
        imageList.add(new DirectImage(url));
        view.showImages(imageList);
    }
}