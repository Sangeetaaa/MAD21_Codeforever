package com.scitech.codegram;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


public class Splash extends AppCompatActivity {

    ImageView imageView;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        imageView = findViewById(R.id.logo);

        Intent intent;
        intent = new Intent(Splash.this, login_page.class);

        imageView.animate().alpha(1).setDuration(1000);

        new Handler().postDelayed(() -> {
            startActivity(intent);
            finish();
        },1500);
    }

}