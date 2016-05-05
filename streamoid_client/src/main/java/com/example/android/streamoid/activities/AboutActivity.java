package com.example.android.streamoid.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.android.streamoid.R;

import mehdi.sakout.aboutpage.AboutPage;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.logo_prefina)
                .addGroup("Connect with us")
                .addEmail("vergilb5@gmail.com")
                .addWebsite("http://radmushroom.github.io/")
                .addFacebook("radmushroom")
                .addTwitter("radmushroom")
                .addInstagram("radmushroom")
                .addGitHub("radmushroom")
                .create();

        setContentView(aboutPage);
    }

}
