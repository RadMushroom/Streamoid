package com.example.android.streamoid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

import com.example.android.streamoid.R;
import com.example.android.streamoid.Utils;

import butterknife.Bind;
import butterknife.OnClick;

public class SplashScreenActivity extends BaseActivity {

    @Bind(R.id.refreshButton)
    ImageButton refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void onStart() {
        super.onStart();
        int secondsDelayed = 1;
        if (Utils.checkWiFiConnection(this)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }
            }, secondsDelayed * 1000);
        } else {
            refreshButton.setVisibility(View.VISIBLE);
            toast("Please connect to Wi-Fi network!");
        }
    }

    @OnClick(R.id.refreshButton)
    protected void onRefreshClick() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
