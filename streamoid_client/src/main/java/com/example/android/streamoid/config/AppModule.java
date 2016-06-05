package com.example.android.streamoid.config;

import android.app.Application;
import android.content.Context;

import com.example.android.streamoid.AppPreferences;
import com.example.android.streamoid.tcp_connection.StreamingManager;
import com.google.gson.Gson;

import net.orange_box.storebox.StoreBox;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Application application;
    public AppModule(Application application) {
        this.application = application;
    }
    @Singleton
    @Provides
    public Context provideContext(){
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    public StreamingManager provideStreamingManager(Gson gson,Context context, AppPreferences preferences){
        return new StreamingManager(gson,context, preferences);
    }

    @Singleton
    @Provides
    public Gson provideGson(){
        return new Gson();
    }

    @Singleton
    @Provides
    public AppPreferences provideAppPreferences(Context context){
        return StoreBox.create(context,AppPreferences.class);
    }
}

