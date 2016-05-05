package com.example.android.streamoid.udp_connection;

import android.app.Activity;
import android.util.Log;

import com.example.android.streamoid.activities.MyCallback;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by RadMushroom on 16.04.2016.
 */
public class BroadcastListener extends Activity implements Runnable {
    MyCallback myCallback = null;
    private int clientPort;
    private DatagramSocket socket;

    public BroadcastListener(int serverPort, MyCallback callback) {
        this.clientPort = serverPort;
        this.myCallback = callback;
    }


    @Override
    public void run() {
        try {
            //Keep a socket open to listen to all the UDP trafic that is destined for this port
            socket = new DatagramSocket(clientPort);
            socket.setBroadcast(true);
            while (!Thread.currentThread().isInterrupted()) {
                Logger.getGlobal().info(">>>Ready to receive broadcast packets..."+ clientPort);
                //Receive a packet
                byte[] recvBuf = new byte[512];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);
                final String serverAddress = packet.getAddress().getHostAddress();
                this.myCallback.updateText("Connected to host: " + serverAddress);
                Log.e("Check",serverAddress);
                //Proceed TCP connection
            }
        } catch (SocketException se) {
            Logger.getGlobal().log(Level.WARNING, "Socket closed: " + se.getMessage());
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.SEVERE, "IO Exception", ex);
        } finally {
            Logger.getGlobal().info(">>>Stop listening broadcast...");
        }
    }

    public void stop() {
        Thread.currentThread().interrupt();
        socket.close();
    }
}
