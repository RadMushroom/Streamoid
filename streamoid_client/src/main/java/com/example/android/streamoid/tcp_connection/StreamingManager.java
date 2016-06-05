package com.example.android.streamoid.tcp_connection;

import android.content.Context;
import android.util.Log;

import com.example.android.streamoid.AppPreferences;
import com.example.android.streamoid.Utils;
import com.example.android.streamoid.model.MusicTrack;
import com.example.android.streamoid.udp_connection.NetworkProtocol;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javax.inject.Inject;

public class StreamingManager {
    private String serverAddress;
    private Gson gson;
    protected Context context;
    @Inject
    AppPreferences preferences;
    public StreamingManager(Gson gson, Context context, AppPreferences preferences) {
        this.gson = gson;
        this.preferences = preferences;
        this.context = context;
        System.setProperty("http.keepAlive","false");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket ss = new ServerSocket(10001);
                    while (!Thread.currentThread().isInterrupted()) {
                        Socket socket = ss.accept();
                        Log.i("Handle", socket.toString());
                        handle(socket);
                    }
                } catch (IOException e) {
                   Log.e("SocketError",e.getMessage());
                }
            }
        }).start();
    }

    private void handle(final Socket socket) {
                try {
                    int command;
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    while ((command = dis.readInt()) != -1) {
                        switch (command) {
                            case NetworkProtocol.START_STREAM:
                                MusicTrack musicTrack = gson.fromJson(dis.readUTF(), MusicTrack.class);
                                final File file = new File(musicTrack.getFilePath().substring(0, musicTrack.getFilePath().lastIndexOf('.')) + ".wav");
                                if (file.exists()){
                                    file.delete();
                                }
                                Utils.decode(musicTrack.getFilePath(), new ExecuteBinaryResponseHandler() {
                                    @Override
                                    public void onProgress(String message) {
                                        Log.e("onProgress", message);
                                    }

                                    @Override
                                    public void onFailure(String message) {
                                        Log.e("onFailure", message);
                                    }

                                    @Override
                                    public void onStart() {
                                        Log.e("onStart", "onStart");
                                    }

                                    @Override
                                    public void onFinish() {
                                        Log.e("onFinish", "onFinish");
                                        BufferedInputStream bis = null;
                                        try {
                                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                                            dos.writeInt(NetworkProtocol.START_STREAM);
                                            dos.writeLong(file.length());
                                            dos.flush();
                                            bis = new BufferedInputStream(new FileInputStream(file));
                                            byte[] buffer = new byte[10240];
                                            long bytesRead = 0;
                                            while ((bytesRead += bis.read(buffer, 0, buffer.length)) != file.length()) {
                                                dos.write(buffer);
                                                Log.i("Chunk",""+bytesRead);
                                                dos.flush();
                                            }
                                            bis.close();
                                        } catch (IOException e) {
                                            Log.e("onFinishError",e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onSuccess(String message) {
                                        Log.e("Check", message);
                                    }
                                }, context);

                                break;
                            case NetworkProtocol.STOP_STREAM:
                                break;
                        }
                    }
                } catch (IOException e) {
                    Log.e("HandleError","HandleError");
                }
    }

    public void sendMetaData(final List<MusicTrack> musicTracks) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket datagramSocket = new DatagramSocket(10000);
                    for (MusicTrack musicTrack : musicTracks) {
                        byte[] metaData = gson.toJson(musicTrack).getBytes();
                        DatagramPacket datagramPacket = new DatagramPacket(metaData, metaData.length, InetAddress.getByName(preferences.getBroadcastAddress()), 9001);
                        datagramSocket.send(datagramPacket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

}




