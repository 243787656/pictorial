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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

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
            case IMGUR_DIRECT: loadImgurImage(url); break;
            case GFYCAT: loadGfycat(url); break;
            case DIRECT_GIF: loadGif(url); break;
            case DIRECT_IMAGE: loadImage(url); break;
        }
    }

    public void loadImgurImage(String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = ImgurClient.getService().getImageDetails(LinkUtil.getImgurId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ImageResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError(error.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(ImageResponse response) {
                        imageList.add(response.data);
                        view.showImages(imageList);
                    }
                });
    }

    public void loadImgurAlbum(String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = ImgurClient.getService().getAlbumImages(LinkUtil.getImgurAlbumId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<AlbumResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError(error.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(AlbumResponse response) {
                        imageList.addAll(response.data.getAlbumImages());
                        view.showImages(imageList);
                    }
                });
    }

    public void loadImgurGallery(final String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = ImgurClient.getService().getGalleryDetails(LinkUtil.getImgurGalleryId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<GalleryResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError(error.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(GalleryResponse response) {
                        if (response.data.isAlbum()) {
                            loadImgurGalleryAlbum(url);
                        } else {
                            loadImgurGalleryImage(url);
                        }
                    }
                });
    }

    public void loadImgurGalleryAlbum(String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = ImgurClient.getService().getGalleryAlbum(LinkUtil.getImgurGalleryId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<AlbumResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError(error.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(AlbumResponse response) {
                        imageList.addAll(response.data.getAlbumImages());
                        view.showImages(imageList);
                    }
                });
    }

    public void loadImgurGalleryImage(String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = ImgurClient.getService().getGalleryImage(LinkUtil.getImgurGalleryId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ImageResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError(error.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(ImageResponse response) {
                        imageList.add(response.data);
                        view.showImages(imageList);
                    }
                });
    }

    public void loadGfycat(String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = GfycatClient.getService().getMetadata(LinkUtil.getGfycatId(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<MetadataResponse>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError(error.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(MetadataResponse response) {
                        imageList.add(response.getGfyItem());
                        view.showImages(imageList);
                    }
                });
    }

    public void loadGif(final String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = GfycatClient.getService().checkUrl(LinkUtil.getGfycatCompatibleUrl(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<GfyItem>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError(error.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(GfyItem gfyItem) {
                        if (gfyItem.getMP4Link() != null) {
                            imageList.add(gfyItem);
                            view.showImages(imageList);
                        } else {
                            convertAndLoadGif(url);
                        }
                    }
                });
    }

    public void convertAndLoadGif(String url) {
        if (subscription != null) subscription.unsubscribe();
        subscription = GfycatUploadClient.getService().uploadGif(LinkUtil.getGfycatCompatibleUrl(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<GfyItem>() {
                    @Override
                    public void onError(Throwable error) {
                        view.showError(error.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(GfyItem gfyItem) {
                        imageList.add(gfyItem);
                        view.showImages(imageList);
                    }
                });
    }

    public void loadImage(String url) {
        imageList.add(new DirectImage(url));
        view.showImages(imageList);
    }
}