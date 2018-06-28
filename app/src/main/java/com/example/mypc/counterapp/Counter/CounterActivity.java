package com.example.mypc.counterapp.Counter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypc.counterapp.Activities.HomeActivity;
import com.example.mypc.counterapp.Activities.LoginActivity;
import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.LogoutServerObjects;
import com.example.mypc.counterapp.ServerObject.MegaCount;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CounterActivity extends AppCompatActivity {
    TextView text_screenmodes, text_megacount;
    int state = 0;
    ImageButton minus_button, plus_button;
    int japam_count;
    int my_contribution;
    ButtonBold submit_button;
    TickerView ticker_mega, ticker_local, ticker_mycontribution;
    Toolbar countertoolbar;
    String chant_id, user_email, chant_created;
    ImageView toolbar_icon;
    TextViewBold conterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        //check internet connection
        countertoolbar = findViewById(R.id.toolBar);
        setSupportActionBar(countertoolbar);

        toolbar_icon = findViewById(R.id.toolabar_icon);
        conterText = findViewById(R.id.toolabr_title);
        toolbar_icon.setImageResource(R.drawable.ic_back_arrow);
        conterText.setText("Counter");

        toolbar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        text_screenmodes = findViewById(R.id.text_screenmode);
        text_megacount = findViewById(R.id.text_mega_count);
        ticker_mega = findViewById(R.id.tickerview);
        ticker_local = findViewById(R.id.ticker_localCount);
        ticker_mycontribution = findViewById(R.id.ticker_contribution);
        minus_button = findViewById(R.id.minusimage);
        plus_button = findViewById(R.id.plusimage);
        submit_button = findViewById(R.id.submitButton);
        final String htmlString = "<u>ON-SCREEN MODE</u>";
        ticker_mega.setCharacterLists(TickerUtils.provideAlphabeticalList());
        ticker_local.setCharacterLists(TickerUtils.provideAlphabeticalList());
        ticker_mycontribution.setCharacterLists(TickerUtils.provideAlphabeticalList());
        ticker_mega.setText("0000");
        final String mega_count = "<u>MEGA COUNT</u>";
        text_megacount.setText(Html.fromHtml(mega_count));

        text_screenmodes.setText(Html.fromHtml(htmlString));
        screen_modes();
        counter();
    }

    public void screen_modes() {

        text_screenmodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String htmlString = "<u>ON-SCREEN MODE</u>";

                final String oddscreen = "<u>OFF-SCREEN MODE</u>";

                if (state == 0) {
                    text_screenmodes.setText(Html.fromHtml(oddscreen));
                    text_screenmodes.setTextColor(getResources().getColor(R.color.colorGray));
                    state = 1;
                } else if (state == 1) {

                    text_screenmodes.setText(Html.fromHtml(htmlString));

                    text_screenmodes.setTextColor(getResources().getColor(R.color.colorOrange));
                    state = 0;
                }

            }
        });
    }

    public void counter() {

        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 0) {
                    plus();
                }
            }
        });
        minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 0) {

                    if (japam_count > 0) {
                        minus();
                    } else {
                        Toast toast = Toast.makeText(CounterActivity.this, "Invalid Count ", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                }
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int contribute = japam_count;
                int countribution_count = my_contribution + contribute;
                my_contribution = countribution_count;
                ticker_mycontribution.setText(String.valueOf(my_contribution));
                japam_count = 0;
                ticker_local.setText(String.valueOf(japam_count));
                ticker_mega.setText(String.valueOf(my_contribution));

                getintentdata();
            }
        });
    }

    public void minus() {

        if (japam_count > 0) {

            japam_count = japam_count - 1;
            String check_forzero = String.valueOf(japam_count);
            ticker_local.setText(String.valueOf(japam_count));
            if (check_forzero.contains("0")) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 1000 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    vibrator.vibrate(100);
                }
            }
        } else {

            Toast toast = Toast.makeText(CounterActivity.this, "Invalid Count ", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void plus() {
        japam_count = japam_count + 1;
        String check_forzero = String.valueOf(japam_count);
        ticker_local.setText(String.valueOf(japam_count));
        if (check_forzero.contains("0")) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 1000 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(100);
            }

        }

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (state == 1) {
                        plus();
                    }
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
                    if (state == 1) {
                        minus();
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public void onBackPressed() {
        //   finish();
        super.onBackPressed();
    }

    public void count() {
        String count = String.valueOf(my_contribution);
        MegaCount megaCount = new MegaCount();
        megaCount.mycount = count;
        megaCount.chant_id = chant_id;
        megaCount.email = chant_created;

        Log.e("apidata", count + chant_id + chant_created);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface apiInterface = retrofit.create(ServerApiInterface.class);
        Call<MegaCount> logout_call = apiInterface.mega_count(megaCount);
        logout_call.enqueue(new Callback<MegaCount>() {
            @Override
            public void onResponse(Call<MegaCount> call, Response<MegaCount> response) {
                if (response.body() != null) {
                    Log.e("responseoncounter", response.body().getResponse() + "\n" + response.body().getMegacount()
                            + "\n" + response.body().getEmail()
                            + "\n" + response.body().getMessage());
                } else {
                    //  Toast.makeText(HomeActivity.this, "Failed to logout", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MegaCount> call, Throwable t) {
                Log.e("logoot_failure", t.getMessage());
            }
        });

    }

    public void getintentdata() {
        chant_id = getIntent().getExtras().getString("chant_id");
        chant_created = getIntent().getExtras().getString("chant_created");
        Log.e("counterdata", chant_id + chant_created + my_contribution);
        count();
    }
}
