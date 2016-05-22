package com.example.android.streamoid;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

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

    public static void decode(String path, final ExecuteBinaryResponseHandler responseHandler, Context context) {
        final String cmd = "-i "+path+" -acodec pcm_s16le -ar 44100 "+path.substring(0,path.lastIndexOf('.'))+".wav";
        Log.i("CMD",cmd);
        final FFmpeg ffmpeg = FFmpeg.getInstance(context);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.e("onStart","onStart");
                }

                @Override
                public void onFailure() {
                    Log.e("onFailure","onFailure");
                }

                @Override
                public void onSuccess() {
                    try {
                        ffmpeg.execute(cmd.split(" "), responseHandler);
                    } catch (FFmpegCommandAlreadyRunningException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    Log.e("onFinish","onFinish");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
    }
}
