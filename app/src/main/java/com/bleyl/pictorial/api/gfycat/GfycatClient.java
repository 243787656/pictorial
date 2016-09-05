package com.bleyl.pictorial.api.gfycat;

import com.bleyl.pictorial.api.gfycat.responses.MetadataResponse;
import com.bleyl.pictorial.models.GfyItem;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class GfycatClient {

    private static GfycatService gfycatService;

    public interface GfycatService {

        @GET("checkUrl/{url}")
        Call<GfyItem> checkUrl(@Path("url") String url);

        @GET("get/{name}")
        Call<MetadataResponse> getMetadata(@Path("name") String gfyName);
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
                .build()
                .create(GfycatService.class);
    }
}