package com.bleyl.pictorial.models.imgur;

import com.bleyl.pictorial.BuildConfig;
import com.bleyl.pictorial.models.imgur.responses.AlbumResponse;
import com.bleyl.pictorial.models.imgur.responses.GalleryResponse;
import com.bleyl.pictorial.models.imgur.responses.ImageResponse;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.Single;

public interface ImgurService {

    @GET("3/image/{id}")
    Single<ImageResponse> getImageDetails(@Path("id") String imageId);

    @GET("3/album/{id}")
    Observable<AlbumResponse> getAlbumImages(@Path("id") String albumId);

    @GET("3/gallery/{id}")
    Single<GalleryResponse> getGalleryDetails(@Path("id") String galleryId);

    @GET("3/gallery/image/{id}")
    Single<ImageResponse> getGalleryImage(@Path("id") String imageId);

    @GET("3/gallery/album/{id}")
    Observable<AlbumResponse> getGalleryAlbum(@Path("id") String albumId);

    class Factory {

        public static ImgurService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.imgur.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getClient())
                    .build();
            return retrofit.create(ImgurService.class);
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
}