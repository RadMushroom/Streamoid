package com.example.android.streamoid.tcp_connection;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.example.android.streamoid.model.MusicTrack;
import com.example.android.streamoid.udp_connection.NetworkProtocol;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by RadMushroom on 14.05.2016.
 */
public class PlaybackManager {
    public static void requestStream(InetAddress address, int port, MusicTrack musicTrack){
        try {
            final Socket socket  = new Socket(address,port);
            final DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(NetworkProtocol.START_STREAM);
            dos.writeUTF(new Gson().toJson(musicTrack));
            int command;
            while ((command = dis.readInt()) != -1){
                switch (command){
                    case NetworkProtocol.START_STREAM:
                        int frequency = 44100;
                        int channelConfig =AudioFormat.CHANNEL_OUT_STEREO;
                        int encoding = AudioFormat.ENCODING_PCM_16BIT;
                        int bufferSize = AudioTrack.getMinBufferSize(frequency,channelConfig,encoding);
                        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,frequency,channelConfig,encoding,
                                bufferSize,AudioTrack.MODE_STREAM);
                        final long fileSize = dis.readLong();
                        audioTrack.play();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                long readBytes = 0;
                                while (readBytes != fileSize){
                                    byte[] buffer = new byte[10240];
                                    try{
                                        readBytes+= dis.read(buffer);
                                        audioTrack.write(buffer,0,buffer.length);
                                    }catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }).start();
                        break;
                    case NetworkProtocol.STOP_STREAM:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
