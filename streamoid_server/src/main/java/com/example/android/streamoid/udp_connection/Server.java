package com.example.android.streamoid.udp_connection;

import com.example.android.streamoid.Callback;

import java.util.logging.Logger;

/**
 * Created by RadMushroom on 16.04.2016.
 */
public class Server {
    protected int serverPort = 9000;
    protected boolean isStopped = false;
    private BroadcastListener broadcastMessageThread;

    public Server(int port, Callback callback) {
        this.serverPort = port;
        broadcastMessageThread = new BroadcastListener(9001,callback);
    }

    public void run() {
        Logger.getGlobal().info("Server started...");
        new Thread(broadcastMessageThread).start();
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        this.broadcastMessageThread.stop();
    }

}
