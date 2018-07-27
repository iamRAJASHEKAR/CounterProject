package com.example.mypc.counterapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

   /*     byte[] sha1 = {
                0x65, 0x5D, 0x66, (byte) 0xA1, (byte) 0xC9, 0x31, (byte) 0x85,
                (byte) 0xAB, (byte) 0x92, (byte) 0xC6, (byte) 0xA2,
                0x60, (byte) 0x87, 0x5B, 0x1A, (byte) 0xDA,
                0x45, 0x6E, (byte) 0x97, (byte) 0xEA
        };
   *//*     System.out.println("keyhashGooglePlaySignIn:" + Base64.encodeToString(sha1, Base64.NO_WRAP));
        Log.e("hsjsvsjv", "keyhashGooglePlaySignIn:" + Base64.encodeToString(sha1, Base64.NO_WRAP));
   */

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
