package com.example.android.streamoid.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.example.android.streamoid.R;
import com.example.android.streamoid.activities.BaseActivity;
import com.example.android.streamoid.adapter.listeners.OnItemClickListener;
import com.example.android.streamoid.adapter.view_holder.BaseViewHolder;
import com.example.android.streamoid.adapter.view_holder.TrackViewHolder;
import com.example.android.streamoid.model.MusicTrack;
import com.example.android.streamoid.model.QueueItem;

import java.util.List;

/**
 * Created by RadMushroom on 27.04.2016.
 */
public class TrackAdapter extends BaseRecyclerViewAdapter<QueueItem> {

    private OnItemClickListener onItemClickListener;

    public TrackAdapter(BaseActivity baseActivity, List<QueueItem> data) {
        super(baseActivity, data);
        if (baseActivity instanceof OnItemClickListener) {
            onItemClickListener = (OnItemClickListener) baseActivity;
        }
    }

    @Override
    public BaseViewHolder<QueueItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.track_layout, parent, false);
        return new TrackViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<QueueItem> holder, int position) {
        QueueItem queueItem = data.get(position);
        holder.bind(queueItem);
    }

    public void updateItem(QueueItem queueItem, boolean isActive) {
        for (QueueItem item : data) {
            if (queueItem.equals(item)) {
                item.setActive(isActive);
            } else {
                item.setActive(false);
            }
        }
        notifyDataSetChanged();
    }
}
