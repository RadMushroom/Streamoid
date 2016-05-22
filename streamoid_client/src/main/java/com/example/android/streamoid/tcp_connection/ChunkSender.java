package com.example.android.streamoid.tcp_connection;

/**
 * Created by RadMushroom on 18.05.2016.
 */
public interface ChunkSender {
    void sendChunk(int protocolValue, byte[] chunk);
}
