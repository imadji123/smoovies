package com.imadji.smoovies.data.source.remote;

import com.imadji.smoovies.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by imadji on 7/8/2018.
 */
public class ApiService {
    private static Endpoint endpoint;

    public static Endpoint getEndpoint() {
        if (endpoint == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addInterceptor(new RequestInterceptor());

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                client.addInterceptor(loggingInterceptor);
            }

            Retrofit retrofit = new Retrofit.Builder().client(client.build())
                    .baseUrl(BuildConfig.TMDB_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            endpoint = retrofit.create(Endpoint.class);
        }

        return endpoint;
    }

}
