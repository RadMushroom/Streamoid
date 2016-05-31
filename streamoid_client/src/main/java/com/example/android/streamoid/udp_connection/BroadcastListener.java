package com.example.android.streamoid.udp_connection;

import android.app.Activity;

import com.example.android.streamoid.StreamoidApp;
import com.example.android.streamoid.tcp_connection.StreamingManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

/**
 * Created by RadMushroom on 16.04.2016.
 */
public class BroadcastListener extends Activity implements Runnable {
    private int clientPort;
    private DatagramSocket socket;
    @Inject
    protected StreamingManager streamingManager;
    public BroadcastListener(int serverPort) {
        this.clientPort = serverPort;
        StreamoidApp.getAppComponent().inject(this);
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
                streamingManager.setServerAddress(serverAddress);
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
