package com.imadji.smoovies.data.source.remote;

import com.imadji.smoovies.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by imadji on 7/8/2018.
 */
public class RequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl originalHttpUrl = originalRequest.url();

        HttpUrl newUrl = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build();

        Request.Builder requestBuilder = originalRequest.newBuilder()
                .url(newUrl);

        Request newRequest = requestBuilder.build();

        return chain.proceed(newRequest);
    }

}
