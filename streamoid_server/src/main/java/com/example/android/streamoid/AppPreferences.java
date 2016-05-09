package com.example.android.streamoid;

import net.orange_box.storebox.annotations.method.KeyByString;

/**
 * Created by RadMushroom on 27.04.2016.
 */

public interface AppPreferences {

    @KeyByString("deviceName")
    String getDeviceName();
    @KeyByString("deviceName")
    AppPreferences setDeviceName(String deviceName);

}
