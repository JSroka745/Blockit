package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class LockScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        setContentView(R.layout.activity_lock_screen);
        ImageView img=findViewById(R.id.lockscreenimg);

        try {
            Drawable dd=getApplicationContext().getPackageManager().getApplicationIcon(sessionId);
            img.setImageDrawable(dd);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}