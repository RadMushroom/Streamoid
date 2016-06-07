package com.example.android.streamoid;

import com.example.android.streamoid.model.MusicTrack;
import com.example.android.streamoid.model.QueueItem;

/**
 * Created by RadMushroom on 15.05.2016.
 */
public interface Callback {
    void addItem(QueueItem queueItem);
    void updateItem(QueueItem queueItem, boolean isActive);
}
