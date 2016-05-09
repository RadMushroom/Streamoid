package com.example.android.streamoid;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by RadMushroom on 21.04.2016.
 */
public class Utils {
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor == null) return null;
                int columnIndex = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                Log.e(Utils.class.getSimpleName(), "Error while retrieving file path: " + e.getMessage());
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    @Nullable
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) return null;
        int startIndex = filePath.lastIndexOf("/");
        int endIndex = filePath.lastIndexOf(".");
        return filePath.substring(startIndex + 1, endIndex);
    }

    public static boolean checkWiFiConnection(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();
        return mWifi != null && mWifi.getType() == ConnectivityManager.TYPE_WIFI && mWifi.isConnected();
    }
}
