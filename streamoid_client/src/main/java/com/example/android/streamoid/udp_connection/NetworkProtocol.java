package com.example.android.streamoid.udp_connection;

/**
 * Created by RadMushroom on 16.04.2016.
 */
public class NetworkProtocol {
    public static final int CLIENT_KEY = 0;
    public static final int SERVER_KEY = 1;
    public static final int CLIENT_INFO = 2;
    public static final int SERVER_INFO = 3;
    public static final int SYNC_REQUEST = 4;
    public static final int SYNC_RESPONSE = 5;
    public static final int DATA_TO_RECEIVE = 6;
    public static final int RECEIVE_CONFIRM = 7;
    public static final String DISCONNECT = "DISCONNECT";
    public static final String DISCOVER = "DISCOVER";
    public static final String RESPONSE = "RESPONSE";
}
