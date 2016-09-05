package com.bleyl.pictorial.api.imgur;

import com.bleyl.pictorial.BuildConfig;
import com.bleyl.pictorial.api.imgur.responses.AlbumResponse;
import com.bleyl.pictorial.api.imgur.responses.GalleryResponse;
import com.bleyl.pictorial.api.imgur.responses.ImageResponse;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ImgurClient {

    private static ImgurService imgurService;

    public interface ImgurService {

        @GET("3/image/{id}")
        Call<ImageResponse> getImageDetails(@Path("id") String imageId);

        @GET("3/album/{id}")
        Call<AlbumResponse> getAlbumImages(@Path("id") String albumId);

        @GET("3/gallery/{id}")
        Call<GalleryResponse> getGalleryDetails(@Path("id") String galleryId);

        @GET("3/gallery/image/{id}")
        Call<ImageResponse> getGalleryImage(@Path("id") String imageId);

        @GET("3/gallery/album/{id}")
        Call<AlbumResponse> getGalleryAlbum(@Path("id") String albumId);
    }

    public static ImgurService getService() {
        if (imgurService == null) {
            imgurService = create();
        }
        return imgurService;
    }

    public static ImgurService create() {
        return new Retrofit.Builder()
                .baseUrl("https://api.imgur.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build()
                .create(ImgurService.class);
    }

    private static OkHttpClient getClient() {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Client-ID " + BuildConfig.API_CLIENT_ID)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
    }
}