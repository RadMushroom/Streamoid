package com.example.android.streamoid.tcp_connection;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.example.android.streamoid.Callback;
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
    private QueueItem queueItem;
    private final Callback callback;

    public PlaybackManager(Callback callback) {
        this.callback = callback;
    }

    public void addToPlayList(QueueItem queueItem) {
        if (playList.contains(queueItem) || (this.queueItem != null && this.queueItem.equals(queueItem))) {
            return;
        }
        playList.add(queueItem);
        callback.addItem(queueItem);
        if (!playList.isEmpty() && !isPlaying) {
            requestStream();
        }
    }

    public void requestStream() {
        isPlaying = true;
        queueItem = playList.poll();
        if (queueItem == null)
            return;
        callback.updateItem(queueItem, true);
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
                        int frequency = musicTrack.getFrequency();
                        int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
                        int encoding = AudioFormat.ENCODING_PCM_16BIT;
                        int bufferSize = AudioTrack.getMinBufferSize(frequency, channelConfig, encoding);
                        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, channelConfig, encoding,
                                bufferSize, AudioTrack.MODE_STREAM);
                        final long fileSize = dis.readLong();
                        audioTrack.play();
                        long readBytes = 0;
                        Log.e("SIZE:", "" + fileSize);
                        while (readBytes <= fileSize) {
                            byte[] buffer = new byte[10240];
                            try {
                                readBytes += dis.read(buffer);
                                Log.i("ReadBytes", "" + readBytes);
                                audioTrack.write(buffer, 0, buffer.length);
                            } catch (IOException e) {
                                isPlaying = false;
                                Log.e("Player exception", e.getMessage());
                            }
                        }
                        try {
                            socket.close();
                            isPlaying = false;
                            callback.updateItem(queueItem, false);
                            queueItem = null;
                            requestStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case NetworkProtocol.STOP_STREAM:
                        socket.close();
                        callback.updateItem(queueItem ,false);
                        queueItem = null;
                        isPlaying = false;
                        requestStream();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            isPlaying = false;
            callback.updateItem(queueItem, false);
            queueItem = null;
        }
    }
}
