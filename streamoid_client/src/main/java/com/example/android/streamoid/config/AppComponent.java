package com.example.android.streamoid.config;

import com.example.android.streamoid.activities.BaseActivity;
import com.example.android.streamoid.activities.MainActivity;
import com.example.android.streamoid.udp_connection.BroadcastListener;
import com.example.android.streamoid.udp_connection.BroadcastSender;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by RadMushroom on 09.05.2016.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(BaseActivity baseActivity);

    void inject(BroadcastListener broadcastListener);
    void inject(MainActivity mainActivity);
    void inject(BroadcastSender broadcastSender);
}
