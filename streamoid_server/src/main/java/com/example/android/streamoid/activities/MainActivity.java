package com.example.android.streamoid.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.example.android.streamoid.R;
import com.example.android.streamoid.udp_connection.Server;

import butterknife.Bind;

public class MainActivity extends BaseActivity {
    @Bind(R.id.textView)
    TextView textView;
    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        server = new Server(9000);
        server.run();
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

}
