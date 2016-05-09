package com.example.android.streamoid.activities;

import com.example.android.streamoid.R;

import net.orange_box.storebox.annotations.method.DefaultValue;
import net.orange_box.storebox.annotations.method.KeyByString;

/**
 * Created by RadMushroom on 27.04.2016.
 */

public interface AppPreferences {

    @KeyByString("deviceName")
    @DefaultValue(R.string.default_device_name)
    String getDeviceName();
    @KeyByString("deviceName")
    AppPreferences setDeviceName(String deviceName);

}
