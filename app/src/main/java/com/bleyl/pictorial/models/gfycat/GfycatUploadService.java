package com.bleyl.pictorial.models.gfycat;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

public interface GfycatUploadService {

    @GET("transcode/")
    Single<GfyItem> uploadGif(@Query("fetchUrl") String encodedUrl);

    class Factory {

        public static GfycatUploadService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://upload.gfycat.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(GfycatUploadService.class);
        }
    }
}