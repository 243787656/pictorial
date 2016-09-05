package com.bleyl.pictorial;

import com.bleyl.pictorial.api.gfycat.GfycatClient;
import com.bleyl.pictorial.api.imgur.ImgurClient;
import com.bleyl.pictorial.utils.LinkUtil;
import com.bleyl.pictorial.models.DirectImage;
import com.bleyl.pictorial.models.Image;
import com.bleyl.pictorial.models.GfyItem;
import com.bleyl.pictorial.api.gfycat.responses.MetadataResponse;
import com.bleyl.pictorial.api.imgur.responses.AlbumResponse;
import com.bleyl.pictorial.api.imgur.responses.GalleryResponse;
import com.bleyl.pictorial.api.imgur.responses.ImageResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewerPresenter {

    private ViewerView view;
    private List<Image> imageList = new ArrayList<>();

    public ViewerPresenter(ViewerView view) {
        this.view = view;
    }

    public void detachView() {
        view = null;
    }

    public void loadUrl(String url) {
        switch (LinkUtil.getLinkType(url)) {
            case IMGUR_GALLERY: loadImgurGallery(url); break;
            case IMGUR_ALBUM: loadImgurAlbum(url); break;
            case IMGUR_DIRECT: loadImgurImage(url); break;
            case GFYCAT: loadGfycat(url); break;
            case DIRECT_GIF: loadGfycatGif(url); break;
            case DIRECT_IMAGE: loadImage(url); break;
        }
    }

    public void loadImgurImage(String url) {
        ImgurClient.getService().getImageDetails(LinkUtil.getImgurId(url)).enqueue(new ApiCallback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful()) {
                    imageList.add(response.body().data);
                    view.showImages(imageList);
                }
            }
        });
    }

    public void loadImgurAlbum(String url) {
        ImgurClient.getService().getAlbumImages(LinkUtil.getImgurAlbumId(url)).enqueue(new ApiCallback<AlbumResponse>() {
            @Override
            public void onResponse(Call<AlbumResponse> call, Response<AlbumResponse> response) {
                if (response.isSuccessful()) {
                    imageList.addAll(response.body().data.getAlbumImages());
                    view.showImages(imageList);
                }
            }
        });
    }

    public void loadImgurGallery(final String url) {
        ImgurClient.getService().getGalleryDetails(LinkUtil.getImgurGalleryId(url)).enqueue(new ApiCallback<GalleryResponse>() {
            @Override
            public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().data.isAlbum()) {
                        loadImgurGalleryAlbum(url);
                    } else {
                        loadImgurGalleryImage(url);
                    }
                }
            }
        });
    }

    public void loadImgurGalleryAlbum(String url) {
        ImgurClient.getService().getGalleryAlbum(LinkUtil.getImgurGalleryId(url)).enqueue(new ApiCallback<AlbumResponse>() {
            @Override
            public void onResponse(Call<AlbumResponse> call, Response<AlbumResponse> response) {
                if (response.isSuccessful()) {
                    imageList.addAll(response.body().data.getAlbumImages());
                    view.showImages(imageList);
                }
            }
        });
    }

    public void loadImgurGalleryImage(String url) {
        ImgurClient.getService().getGalleryImage(LinkUtil.getImgurGalleryId(url)).enqueue(new ApiCallback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful()) {
                    imageList.add(response.body().data);
                    view.showImages(imageList);
                }
            }
        });
    }

    public void loadGfycat(String url) {
        GfycatClient.getService().getMetadata(LinkUtil.getGfycatId(url)).enqueue(new ApiCallback<MetadataResponse>() {
            @Override
            public void onResponse(Call<MetadataResponse> call, Response<MetadataResponse> response) {
                if (response.isSuccessful()) {
                    imageList.add(response.body().getGfyItem());
                    view.showImages(imageList);
                }
            }
        });
    }

    public void loadGfycatGif(final String url) {
        GfycatClient.getService().checkUrl(LinkUtil.getGfycatCompatibleUrl(url)).enqueue(new ApiCallback<GfyItem>() {
            @Override
            public void onResponse(Call<GfyItem> call, Response<GfyItem> response) {
                if (response.isSuccessful()) {
                    if (response.body().hasMP4Link()) {
                        imageList.add(response.body());
                        view.showImages(imageList);
                    } else {
                        // TODO - Load gif
                    }
                }
            }
        });
    }

    public void loadImage(String url) {
        imageList.add(new DirectImage(url));
        view.showImages(imageList);
    }

    abstract class ApiCallback<T> implements Callback<T> {

        @Override
        public abstract void onResponse(Call<T> call, Response<T> response);

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            view.showError(t.getLocalizedMessage());
        }
    }
}