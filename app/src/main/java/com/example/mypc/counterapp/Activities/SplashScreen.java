package com.example.mypc.counterapp.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.sessions.SessionsManager;

public class SplashScreen extends AppCompatActivity {

    SessionsManager sessionsManager;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.DECOR_CAPTION_SHADE_DARK);
        setContentView(R.layout.activity_splash_screen);
        sessionsManager = new SessionsManager(this);
        checksession();
    }

    public void checksession() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionsManager.isLoggedIn()) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3 * 1000);
    }

}
