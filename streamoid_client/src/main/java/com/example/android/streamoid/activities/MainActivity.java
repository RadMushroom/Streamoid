package com.example.android.streamoid.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.canelmas.let.AskPermission;
import com.canelmas.let.Let;
import com.example.android.streamoid.R;
import com.example.android.streamoid.StreamoidApp;
import com.example.android.streamoid.Utils;
import com.example.android.streamoid.adapter.TrackAdapter;
import com.example.android.streamoid.adapter.listeners.OnItemClickListener;
import com.example.android.streamoid.model.MusicTrack;
import com.example.android.streamoid.tcp_connection.StreamingManager;
import com.example.android.streamoid.udp_connection.BroadcastListener;
import com.example.android.streamoid.udp_connection.BroadcastSender;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends BaseActivity implements OnItemClickListener {

    private static final int PICKFILE_REQUEST_CODE = 1001;
    private static final int PICKIMAGE_REQUEST_CODE = 1002;
    @Bind(R.id.toolbar)
    protected Toolbar tb;
    @Bind(R.id.lvMain)
    protected RecyclerView tracksRecyclerView;
    @Bind(R.id.userPic)
    ImageView userPic;
    @Inject
    protected StreamingManager streamingManager;
    private MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    private TrackAdapter trackAdapter;
    private BroadcastListener broadcastListener;
    private BroadcastSender broadcastSender;
    private DecimalFormat decimalFormat;
    private DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StreamoidApp.getAppComponent().inject(this);
        setSupportActionBar(tb);
        mp = new MediaPlayer();
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat = new DecimalFormat("0.00", decimalFormatSymbols);
        broadcastListener = new BroadcastListener(8999);
        new Thread(broadcastListener).start();
        broadcastSender = new BroadcastSender(9001);
        new Thread(broadcastSender).start();
        trackAdapter = new TrackAdapter(this, new ArrayList<>(Collections.<MusicTrack>emptyList()));
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipePosition = viewHolder.getAdapterPosition();
                trackAdapter.removeItem(swipePosition);
            }


        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(tracksRecyclerView);
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
        if (broadcastListener != null)
            broadcastListener.stop();
        if (broadcastSender != null)
            broadcastSender.stop();
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
            case R.id.fileChoose:
                Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileIntent.setType("*/*");
                fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(fileIntent, "Select a file"), PICKFILE_REQUEST_CODE);
                } catch (ActivityNotFoundException exception) {
                    toast("Please install file manager");
                }
                break;
            default:
                break;
            case R.id.aboutProgram:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        switch (requestCode) {
            case PICKFILE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String filePath = Utils.getPath(this, data.getData());
                    String fileName = Utils.getFileName(filePath);
                    if (filePath != null) {
                        File file = new File(filePath);
                        String fileSize = decimalFormat.format((float) file.length() / (1024 * 1024)) + " Mb";
                        mmr.setDataSource(this, Uri.parse(filePath));
                        String parsedTrackDuration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        int trackDuration = Integer.valueOf(parsedTrackDuration);
                        String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                        MusicTrack musicTrack = new MusicTrack(artist, filePath, fileName, fileSize, trackDuration);
                        trackAdapter.addItem(musicTrack);
                    } else {
                        toast("Error while reading file");
                    }

                }
                break;
            case PICKIMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String imagePath = Utils.getPath(this, data.getData());
                    appPreferences.setUserpicPath(imagePath);
                    userPic.setImageBitmap(BitmapFactory.decodeFile(appPreferences.getUserpicPath()));
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(String.format("Hello, %s", appPreferences.getDeviceName()));
        userPic.setImageBitmap(BitmapFactory.decodeFile(appPreferences.getUserpicPath()));
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mp.isPlaying()) {
            mp.stop();
            mp.reset();
            mp.release();
        }
    }

    @Override
    public void onItemClick(int position) {
        MusicTrack musicTrack = trackAdapter.getItem(position);
        if (mp.isPlaying()) {
            mp.stop();
            mp.reset();
        } else {
            try {
                mp.setDataSource(musicTrack.getFilePath());
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.userPic)
    protected void chooseUserPicPath() {
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.setType("*/*");
        fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(fileIntent, "Select a file"), PICKIMAGE_REQUEST_CODE);
        } catch (ActivityNotFoundException exception) {
            toast("Please install file manager");
        }
    }


    @OnClick(R.id.fab)
    @AskPermission(WRITE_EXTERNAL_STORAGE)
    protected void startStream() {
        if (trackAdapter != null && !trackAdapter.getData().isEmpty()) {
            streamingManager.sendMetaData(trackAdapter.getData());
        } else {
            toast("Add more tracks to playlist");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Let.handle(this,requestCode,permissions,grantResults);
    }
}
