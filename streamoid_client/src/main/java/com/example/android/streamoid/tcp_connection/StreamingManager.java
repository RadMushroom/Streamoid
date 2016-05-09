package com.example.android.streamoid.tcp_connection;

import com.example.android.streamoid.model.MusicTrack;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;


public class StreamingManager {
    private String serverAddress;
    private Gson gson;

    public StreamingManager(Gson gson) {
        this.gson = gson;
    }
    public void sendMetaData(final List<MusicTrack> musicTracks) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket datagramSocket = new DatagramSocket(10000);
                    for (MusicTrack musicTrack : musicTracks) {
                        byte[] metaData = gson.toJson(musicTrack).getBytes();
                        DatagramPacket datagramPacket = new DatagramPacket(metaData, metaData.length, InetAddress.getByName("255.255.255.255"), 9001);
                        datagramSocket.send(datagramPacket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public void setServerAddress(String serverAddress){
        this.serverAddress = serverAddress;
    }
}



