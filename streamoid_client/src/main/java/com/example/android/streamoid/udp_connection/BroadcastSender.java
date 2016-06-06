package com.example.android.streamoid.udp_connection;

import android.util.Log;

import com.example.android.streamoid.AppPreferences;
import com.example.android.streamoid.StreamoidApp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.inject.Inject;

public class BroadcastSender implements Runnable {

    @Inject
    AppPreferences appPreferences;
    private int clientPort;
    private DatagramSocket socket;

    public BroadcastSender(int serverPort) {
        this.clientPort = serverPort;
        StreamoidApp.getAppComponent().inject(this);
    }

    @Override
    public void run() {
        try {
            //Keep a socket open to listen to all the UDP traffic that is destined for this port
            socket = new DatagramSocket(clientPort);
            socket.setBroadcast(true);
            byte[] sendData = NetworkProtocol.DISCOVER.getBytes();
            //Send a response
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(appPreferences.getBroadcastAddress()), 9001);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    socket.send(sendPacket);
                    Log.i("Sent packet to", sendPacket.getAddress().getHostName());
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    if (!socket.isClosed()) {
                        socket.close();
                        e.printStackTrace();
                    }
                }
            }
        } catch (SocketException se) {
            Log.e("Sender socket exception", "Socket closed");
        } catch (IOException ex) {
            Log.e("Socket IO exception", ex.getMessage());
        } finally {
            Log.e("Sender",">>>Sender Stop listening broadcast...");
        }
    }

    public void stop() {
        Thread.currentThread().interrupt();
        socket.close();
    }
}
