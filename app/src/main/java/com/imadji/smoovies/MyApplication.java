package com.imadji.smoovies;

import android.app.Application;
import android.content.Context;

import com.imadji.smoovies.data.source.local.MovieDatabase;
import com.imadji.smoovies.data.source.remote.ApiClient;
import com.imadji.smoovies.data.source.remote.ApiService;
import com.imadji.smoovies.repository.MovieRepository;

/**
 * Created by imadji on 7/8/2018.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        setContext(this);
    }

    public static void setContext(Context context) {
        MyApplication.context = context;
    }

    public static Context getContext() {
        return MyApplication.context;
    }

    public static ApiService getApiService() {
        return ApiClient.getInstance();
    }

    public static MovieDatabase getDatabase() {
        return MovieDatabase.getInstance(getContext());
    }

    public static MovieRepository getRepository() {
        return MovieRepository.getInstance(getApiService(), getDatabase());
    }

}
