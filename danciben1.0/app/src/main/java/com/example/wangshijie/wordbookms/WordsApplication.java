package com.example.wangshijie.wordbookms;

import android.app.Application;
import android.content.Context;




/*
它是一个应用程序的全局环境，是Android系统的一个抽象类，可以通过它获取程序的资源*/
public class WordsApplication extends Application {
    private static Context context;
    public static Context getContext(){
        return WordsApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WordsApplication.context=getApplicationContext();
    }
}