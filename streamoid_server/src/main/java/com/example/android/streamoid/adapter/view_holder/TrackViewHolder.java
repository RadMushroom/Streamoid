package com.example.android.streamoid.adapter.view_holder;

import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.streamoid.R;
import com.example.android.streamoid.adapter.listeners.OnItemClickListener;
import com.example.android.streamoid.model.MusicTrack;

import butterknife.Bind;

/**
 * Created by RadMushroom on 27.04.2016.
 */
public class TrackViewHolder extends BaseViewHolder<MusicTrack> {

    @Bind(R.id.picNote)
    protected ImageView picNote;
    @Bind(R.id.trackDuration)
    protected TextView trackDuration;
    @Bind(R.id.fileName)
    protected TextView fileName;
    @Bind(R.id.fileSize)
    protected TextView fileSize;
    @Bind(R.id.trackArtist)
    protected TextView trackArtist;

    public TrackViewHolder(View view, final OnItemClickListener onItemClickListener
    ) {
        super(view);
        if (onItemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
        DrawableCompat.setTint(DrawableCompat.wrap(picNote.getDrawable()),
                ContextCompat.getColor(view.getContext(), R.color.accent));
    }

    @Override
    public void bind(MusicTrack musicTrack) {
        trackDuration.setText(musicTrack.getFileDuration());
        fileName.setText(musicTrack.getFileName());
        fileSize.setText(musicTrack.getFileSize());
        trackArtist.setText(musicTrack.getFileArtist());
    }
}
