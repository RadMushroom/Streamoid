package com.example.android.streamoid.model;

import java.net.InetAddress;

import lombok.Data;

@Data
public class QueueItem {
    private MusicTrack musicTrack;
    private InetAddress address;
    private int port;
}
