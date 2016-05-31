package com.example.android.streamoid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.streamoid.Callback;
import com.example.android.streamoid.R;
import com.example.android.streamoid.StreamoidApp;
import com.example.android.streamoid.adapter.TrackAdapter;
import com.example.android.streamoid.model.MusicTrack;
import com.example.android.streamoid.udp_connection.BroadcastListener;
import com.example.android.streamoid.udp_connection.Server;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements Callback {
    private Server server;
    @Bind(R.id.toolbar)
    protected Toolbar tb;
    @Bind(R.id.lvMain)
    protected RecyclerView tracksRecyclerView;
    public static TrackAdapter trackAdapter;
    private BroadcastListener broadcastListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StreamoidApp.getAppComponent().inject(this);
        setSupportActionBar(tb);
        server = new Server(9000,this);
        server.run();
        trackAdapter = new TrackAdapter(this,new ArrayList<>(Collections.<MusicTrack>emptyList()));
        tracksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tracksRecyclerView.setAdapter(trackAdapter);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (server != null) server.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                break;
            case R.id.aboutProgram:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(String.format("Hello, %s", appPreferences.getDeviceName()));
    }

    @Override
    public void updateAdapter(final MusicTrack musicTrack) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                trackAdapter.addItem(musicTrack);
            }
        });
    }
}
