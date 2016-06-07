package com.example.android.streamoid.model;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class MusicTrack {
    private String fileArtist;
    private String filePath;
    private String fileName;
    private String fileSize;
    private int fileDuration;
    private int frequency;
}
