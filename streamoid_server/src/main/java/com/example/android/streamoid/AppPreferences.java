package com.example.android.streamoid;

import net.orange_box.storebox.annotations.method.DefaultValue;
import net.orange_box.storebox.annotations.method.KeyByString;

/**
 * Created by RadMushroom on 27.04.2016.
 */

public interface AppPreferences {

    @KeyByString("deviceName")
    @DefaultValue(R.string.default_name)
    String getDeviceName();
    @KeyByString("deviceName")
    AppPreferences setDeviceName(String deviceName);

}
