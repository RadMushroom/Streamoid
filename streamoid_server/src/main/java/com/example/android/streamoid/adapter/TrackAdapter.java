package com.example.android.streamoid.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.example.android.streamoid.R;
import com.example.android.streamoid.activities.BaseActivity;
import com.example.android.streamoid.adapter.listeners.OnItemClickListener;
import com.example.android.streamoid.adapter.view_holder.BaseViewHolder;
import com.example.android.streamoid.adapter.view_holder.TrackViewHolder;
import com.example.android.streamoid.model.MusicTrack;

import java.util.List;

/**
 * Created by RadMushroom on 27.04.2016.
 */
public class TrackAdapter extends BaseRecyclerViewAdapter<MusicTrack> {

    private OnItemClickListener onItemClickListener;

    public TrackAdapter(BaseActivity baseActivity, List<MusicTrack> data) {
        super(baseActivity, data);
        if (baseActivity instanceof OnItemClickListener) {
            onItemClickListener = (OnItemClickListener) baseActivity;
        }
    }

    @Override
    public BaseViewHolder<MusicTrack> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.track_layout, parent, false);
        return new TrackViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<MusicTrack> holder, int position) {
        MusicTrack musicTrack = data.get(position);
        holder.bind(musicTrack);
    }
}
