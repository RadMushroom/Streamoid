package com.example.android.streamoid;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.android.streamoid.model.MusicTrack;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by RadMushroom on 21.04.2016.
 */
public class Utils {

    public static boolean checkWiFiConnection(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();
        return mWifi != null && mWifi.getType() == ConnectivityManager.TYPE_WIFI && mWifi.isConnected();
    }

    public static boolean isTrackJson(String json) {
        try {
            new Gson().fromJson(json, MusicTrack.class);
            return true;
        } catch (JsonSyntaxException e){
            Log.e(Utils.class.getSimpleName(),e.getMessage());
            return false;
        }
    }
}
