package com.example.android.streamoid.activities;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.android.streamoid.AppPreferences;

import net.orange_box.storebox.StoreBox;

import butterknife.ButterKnife;

/**
 * Created by RadMushroom on 20.04.2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        appPreferences = StoreBox.create(this,AppPreferences.class);
    }

    public abstract int getContentView();

    protected void toast(@StringRes int resId) {
        toast(getString(resId));
    }

    protected void toast(@StringRes int resId, int duration) {
        toast(getString(resId), duration);
    }

    protected void toast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    protected void toast(String message, int duration) {
        if (!TextUtils.isEmpty(message) && duration > 0) {
            Toast.makeText(this, message, duration).show();
        }
    }
}
