package com.bleyl.pictorial.models.gfycat;

import com.bleyl.pictorial.models.gfycat.responses.MetadataResponse;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Single;
import rx.schedulers.Schedulers;

public class GfycatClient {

    private static GfycatService gfycatService;

    public interface GfycatService {

        @GET("checkUrl/{url}")
        Single<GfyItem> checkUrl(@Path("url") String url);

        @GET("get/{name}")
        Single<MetadataResponse> getMetadata(@Path("name") String gfyName);
    }

    public static GfycatService getService() {
        if (gfycatService == null) {
            gfycatService = create();
        }
        return gfycatService;
    }

    public static GfycatService create() {
        return new Retrofit.Builder()
                .baseUrl("http://gfycat.com/cajax/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(GfycatService.class);
    }
}