package com.imadji.smoovies;

import android.app.Application;
import android.content.Context;

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

    public static synchronized Context getContext() {
        return MyApplication.context;
    }

}
