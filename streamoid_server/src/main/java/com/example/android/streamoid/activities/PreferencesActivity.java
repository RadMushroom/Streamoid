package com.example.android.streamoid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.android.streamoid.R;

import butterknife.Bind;

public class PreferencesActivity extends BaseActivity {
    @Bind(R.id.etDeviceName)
    protected EditText deviceName;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    private static final int PICKFILE_RESULT_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        deviceName.setText(appPreferences.getDeviceName());
    }

    @Override
    public int getContentView() {
        return R.layout.preferences_layout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String name = deviceName.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            appPreferences.setDeviceName(name);
        } else {
            appPreferences.setDeviceName(getString(R.string.default_device_name));
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        switch (requestCode) {

        }
    }
}