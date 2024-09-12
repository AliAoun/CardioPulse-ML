package com.aountech.heartdiseasesprediction.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.aountech.heartdiseasesprediction.R;

public class SplashActivity extends AppCompatActivity {
    ImageView logo , splash_bg;
    LottieAnimationView lottie_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo =findViewById(R.id.splash_iv_logo);
        splash_bg =findViewById(R.id.splash_iv_bg);
        lottie_splash =findViewById(R.id.lottie_splash);

        Thread thread =new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent= new Intent(SplashActivity.this,SignInActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        thread.start();
    }
}