package com.bleyl.pictorial.model.gfycat;

import com.bleyl.pictorial.model.gfycat.responses.MetadataResponse;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Single;

public class GfycatClient {

    private static GfycatService sGfycatService;

    public interface GfycatService {

        @GET("checkUrl/{url}")
        Single<GfyItem> checkUrl(@Path("url") String url);

        @GET("get/{name}")
        Single<MetadataResponse> getMetadata(@Path("name") String gfyName);
    }

    public static GfycatService getService() {
        if (sGfycatService == null) {
            sGfycatService = create();
        }
        return sGfycatService;
    }

    public static GfycatService create() {
        return new Retrofit.Builder()
                .baseUrl("http://gfycat.com/cajax/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(GfycatService.class);
    }
}