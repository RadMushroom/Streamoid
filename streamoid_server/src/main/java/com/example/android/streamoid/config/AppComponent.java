package com.example.android.streamoid.config;

import com.example.android.streamoid.activities.BaseActivity;
import com.example.android.streamoid.udp_connection.BroadcastListener;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(BaseActivity baseActivity);

    void inject(BroadcastListener broadcastListener);
}
