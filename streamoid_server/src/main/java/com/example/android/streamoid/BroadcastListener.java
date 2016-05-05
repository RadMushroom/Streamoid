package com.example.android.streamoid;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by RadMushroom on 16.04.2016.
 */
public class BroadcastListener implements Runnable {
    private int serverPort;
    private DatagramSocket socket;
    private Queue<DatagramPacket> pool = new LinkedList<>();
    MyCallback callback = null;

    public BroadcastListener(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try {
            //Keep a socket open to listen to all the UDP trafic that is destined for this port
            socket = new DatagramSocket(serverPort, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            Thread poolHandler = createThread();
            while (!Thread.currentThread().isInterrupted()) {
                Logger.getGlobal().info(">>>Ready to receive broadcast packets...");
                //Receive a packet
                byte[] recvBuf = new byte[512];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);
                pool.add(packet);

                if (poolHandler.getState().equals(Thread.State.TERMINATED)) {
                    poolHandler = createThread();
                }

                if (poolHandler.getState().equals(Thread.State.NEW)) {
                    poolHandler.start();
                }
                Logger.getGlobal().info("Add packet to the pool");
            }
        } catch (SocketException se) {
            Logger.getGlobal().log(Level.WARNING, "Socket closed");
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.SEVERE, "IO Exception", ex);
        } finally {
            Logger.getGlobal().info(">>>Stop listening broadcast...");
        }
    }

    private Thread createThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.getGlobal().info("Ready to handle packet pool...");
                while (pool.size() != 0) {
                    DatagramPacket packet = pool.poll();
                    String message = new String(packet.getData()).trim();
                    Log.e("Recv",message);
                    if (message.equals(NetworkProtocol.DISCOVER)) {
                        Logger.getGlobal().info("<<<Discovery packet received from: " + packet.getAddress().getHostAddress());
                        byte[] sendData = NetworkProtocol.RESPONSE.getBytes();
                        //Send a response
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), 8999);
                        try {
                            socket.send(sendPacket);
                        } catch (IOException e) {
                            Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
                        }
                        Logger.getGlobal().info(">>>Sent packet |" + new String(sendData) + "| to: " + sendPacket.getAddress().getHostAddress());
                    } else if (message.equals(NetworkProtocol.DISCONNECT)) {
                    }
                }
                Logger.getGlobal().info("Pool handle stopped.");
            }
        });
    }

    public void stop() {
        Thread.currentThread().interrupt();
        socket.close();
    }
}
