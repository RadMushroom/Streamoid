package com.example.android.streamoid;

import net.orange_box.storebox.annotations.method.DefaultValue;
import net.orange_box.storebox.annotations.method.KeyByString;

public interface AppPreferences {

    @KeyByString("deviceName")
    @DefaultValue(R.string.default_device_name)
    String getDeviceName();
    @KeyByString("deviceName")
    AppPreferences setDeviceName(String deviceName);

    @KeyByString("userpicPath")
    String getUserpicPath();
    @KeyByString("userpicPath")
    AppPreferences setUserpicPath(String userpicPath);

    @KeyByString("broadcastAddress")
    String getBroadcastAddress();
    @KeyByString("broadcastAddress")
    AppPreferences setBroadcastAddress(String broadcastAddress);
}
