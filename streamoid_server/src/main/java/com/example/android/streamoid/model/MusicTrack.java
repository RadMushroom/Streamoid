package com.example.android.streamoid.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by RadMushroom on 20.04.2016.
 */

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class MusicTrack {
    private String fileArtist;
    private String filePath;
    private String fileName;
    private String fileSize;
    private String fileDuration;
}
