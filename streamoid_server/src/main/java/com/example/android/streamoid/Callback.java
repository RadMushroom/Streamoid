package com.example.android.streamoid;

import com.example.android.streamoid.model.MusicTrack;

/**
 * Created by RadMushroom on 15.05.2016.
 */
public interface Callback {
    void updateAdapter(MusicTrack musicTrack);
    void removeItem(MusicTrack musicTrack);
}
