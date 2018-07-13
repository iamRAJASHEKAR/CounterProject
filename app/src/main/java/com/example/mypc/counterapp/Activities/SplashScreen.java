package com.example.mypc.counterapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.example.mypc.counterapp.Database.ChantData;
import com.example.mypc.counterapp.Database.DatabaseManager;
import com.example.mypc.counterapp.Database.Userdata;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.sessions.SessionsManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SplashScreen extends AppCompatActivity {

    SessionsManager sessionsManager;
    Handler handler;
    Userdata userdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.DECOR_CAPTION_SHADE_DARK);
        setContentView(R.layout.activity_splash_screen);
        sessionsManager = new SessionsManager(this);
        checksession();
        DatabaseManager.getInstance().fillContext(getApplicationContext());
        sample();
    }


    public void sample() {


        long unixdata = System.currentTimeMillis() / 1000L;
        String timestamp = String.valueOf(unixdata);


        long unixdat = System.currentTimeMillis();


       /* Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm a");
        String date = df.format(c.getTime());
      */  /*    ArrayList<Userdata> userdatadd = new ArrayList<>();

        userdata = new Userdata();
        userdata.setFirstname("raju");
        userdata.setLastname("shekar");
        userdata.setEmail("jhsdvbs");
        userdata.setUsername("jhbvj");
        DatabaseManager.getInstance().add_user(userdata);
        userdatadd = DatabaseManager.getInstance().getalluser();
    */
        Log.e("userdat", timestamp + "\n" + unixdat);
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
