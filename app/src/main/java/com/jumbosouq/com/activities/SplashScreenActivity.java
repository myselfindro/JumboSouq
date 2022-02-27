package com.jumbosouq.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.jumbosouq.com.MainActivity;
import com.jumbosouq.com.R;
import com.jumbosouq.com.session.SessionManager;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splasht);
        sessionManager = new SessionManager(getApplicationContext());


        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {

                if (sessionManager.isLoggedIn()) {

                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    Intent intent = new Intent(SplashScreenActivity.this, GuestLoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, secondsDelayed * 3000);


    }


}
