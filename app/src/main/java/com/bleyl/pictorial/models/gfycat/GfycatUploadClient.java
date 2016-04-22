package com.bleyl.pictorial.models.gfycat;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

public class GfycatUploadClient {

    private static GfycatUploadService gfycatUploadService;

    public interface GfycatUploadService {

        @GET("transcode/")
        Single<GfyItem> uploadGif(@Query("fetchUrl") String encodedUrl);
    }

    public static GfycatUploadService getService() {
        if (gfycatUploadService == null) {
            gfycatUploadService = create();
        }
        return gfycatUploadService;
    }

    public static GfycatUploadService create() {
        return new Retrofit.Builder()
                .baseUrl("http://upload.gfycat.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(GfycatUploadService.class);
    }
}