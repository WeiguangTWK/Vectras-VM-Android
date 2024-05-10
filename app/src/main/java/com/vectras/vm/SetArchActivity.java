package com.vectras.vm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.vectras.qemu.MainSettingsManager;

public class SetArchActivity extends AppCompatActivity implements View.OnClickListener {

    SetArchActivity activity;
    private static Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        setContentView(R.layout.activity_set_arch);
        activity = this;
        Button archi386 = findViewById(R.id.archi386);
        Button archx86_64 = findViewById(R.id.archx86_64);
        Button archarm64 = findViewById(R.id.archarm64);
        Button archppc = findViewById(R.id.archppc);
        Button web = findViewById(R.id.webBtn);
        archi386.setOnClickListener(this);
        archx86_64.setOnClickListener(this);
        archarm64.setOnClickListener(this);
        archppc.setOnClickListener(this);
        web.setOnClickListener(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.archi386) {
            MainSettingsManager.setArch(this, "I386");

            finish();
            startActivity(new Intent(this, SplashActivity.class));
        } else if (id == R.id.archx86_64) {
            MainSettingsManager.setArch(this, "X86_64");

            finish();
            startActivity(new Intent(this, SplashActivity.class));
        } else if (id == R.id.archarm64) {
            MainSettingsManager.setArch(this, "ARM64");

            finish();
            startActivity(new Intent(this, SplashActivity.class));
        } else if (id == R.id.archppc) {
            MainSettingsManager.setArch(this, "PPC");

            finish();
            startActivity(new Intent(this, SplashActivity.class));
        } else if (id == R.id.webBtn) {
            String qe = "https://www.qemu.org/";
            Intent q = new Intent(Intent.ACTION_VIEW);
            q.setData(Uri.parse(qe));
            startActivity(q);
        }
    }
}