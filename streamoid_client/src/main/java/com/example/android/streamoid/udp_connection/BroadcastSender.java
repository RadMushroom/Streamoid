package com.example.android.streamoid.udp_connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by RadMushroom on 16.04.2016.
 */
public class BroadcastSender implements Runnable {
    private int clientPort;
    private DatagramSocket socket;

    public BroadcastSender(int serverPort) {
        this.clientPort = serverPort;
    }

    @Override
    public void run() {
        try {
            //Keep a socket open to listen to all the UDP traffic that is destined for this port
            socket = new DatagramSocket(clientPort);
            socket.setBroadcast(true);
            byte[] sendData = NetworkProtocol.DISCOVER.getBytes();
            //Send a response
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 9001);
            try {
                socket.send(sendPacket);
            } catch (IOException e) {
                Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
            }
        } catch (SocketException se) {
            Logger.getGlobal().log(Level.WARNING, "Socket closed");
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.SEVERE, "IO Exception", ex);
        } finally {
            Logger.getGlobal().info(">>>Sender Stop listening broadcast...");
        }
    }

    public void stop() {
        Thread.currentThread().interrupt();
        socket.close();
    }
}
