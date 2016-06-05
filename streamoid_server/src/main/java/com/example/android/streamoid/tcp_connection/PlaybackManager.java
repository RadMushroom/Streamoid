package com.example.android.streamoid.tcp_connection;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.example.android.streamoid.model.MusicTrack;
import com.example.android.streamoid.model.QueueItem;
import com.example.android.streamoid.udp_connection.NetworkProtocol;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by RadMushroom on 14.05.2016.
 */
public class PlaybackManager {

    private static Queue<QueueItem> playList = new LinkedList<>();
    private static boolean isPlaying = false;

    public static void addToPlayList(QueueItem queueItem) {
        if (playList.contains(queueItem)) {
            return;
        }
        playList.add(queueItem);
        if (!playList.isEmpty() && !isPlaying) {
            requestStream();
        }
    }

    public static void requestStream() {
        isPlaying = true;
        QueueItem queueItem = playList.poll();
        if (queueItem == null)
            return;
        InetAddress address = queueItem.getAddress();
        int port = queueItem.getPort();
        MusicTrack musicTrack = queueItem.getMusicTrack();
        try {
            final Socket socket = new Socket(address, port);
            final DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(NetworkProtocol.START_STREAM);
            dos.writeUTF(new Gson().toJson(musicTrack));
            int command;
            while ((command = dis.readInt()) != -1) {
                switch (command) {
                    case NetworkProtocol.START_STREAM:
                        int frequency = 44100;
                        int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
                        int encoding = AudioFormat.ENCODING_PCM_16BIT;
                        int bufferSize = AudioTrack.getMinBufferSize(frequency, channelConfig, encoding);
                        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, channelConfig, encoding,
                                bufferSize, AudioTrack.MODE_STREAM);
                        final long fileSize = dis.readLong();
                        audioTrack.play();
                        audioTrack.setNotificationMarkerPosition(musicTrack.getFileDuration()*frequency);
                        audioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
                            @Override
                            public void onMarkerReached(AudioTrack track) {
                                Log.i("MarkerReached","MarkerReached");
                                try {
                                    socket.close();
                                    isPlaying = false;
                                    requestStream();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onPeriodicNotification(AudioTrack track) {

                            }
                        });
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                long readBytes = 0;
                                while (readBytes != fileSize) {
                                    byte[] buffer = new byte[10240];
                                    try {
                                        readBytes += dis.read(buffer);
                                        Log.i("ReadBytes",""+readBytes);
                                        audioTrack.write(buffer, 0, buffer.length);
                                    } catch (IOException e) {
                                        isPlaying = false;
                                        e.printStackTrace();
                                    }
                                }
                                Log.i("njnjnj",""+audioTrack.getPlaybackHeadPosition());
                            }
                        }).start();
                        break;
                    case NetworkProtocol.STOP_STREAM:
                        socket.close();
                        isPlaying = false;
                        requestStream();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            isPlaying = false;
        }
    }
}
