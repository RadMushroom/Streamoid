package com.example.android.streamoid;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements MyCallback {
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

    @Override
    public void updateText(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(str);
            }
        });

    }
}
