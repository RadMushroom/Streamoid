package com.example.android.streamoid;

import android.app.Application;
import android.content.Context;

import com.example.android.streamoid.config.AppComponent;
import com.example.android.streamoid.config.AppModule;
import com.example.android.streamoid.config.DaggerAppComponent;

public class StreamoidApp extends Application {
    private static Context context;
    private AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return ((StreamoidApp)context).appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
