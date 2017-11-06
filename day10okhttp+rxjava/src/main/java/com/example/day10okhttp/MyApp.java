package com.example.day10okhttp;

import android.app.Application;
import android.content.Context;

/**
 * Created by yfeng on 2017/10/12.
 */

public class MyApp extends Application{
    public static Context context ;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
