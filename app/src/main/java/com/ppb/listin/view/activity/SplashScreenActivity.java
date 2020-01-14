package com.ppb.listin.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ppb.listin.R;
import com.ppb.listin.api.response.LoginResponse;
import com.ppb.listin.preference.AppPreference;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final LoginResponse.User user = AppPreference.getUser(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user == null) {
                    finish();
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                } else {
                    finish();
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                }
            }
        }, 3000);
    }
}
