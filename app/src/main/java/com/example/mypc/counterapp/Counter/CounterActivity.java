package com.example.mypc.counterapp.Counter;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypc.counterapp.R;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

public class CounterActivity extends AppCompatActivity {
    TextView text_screenmodes, text_megacount;
    int state = 0;
    ImageButton minus_button, plus_button;
    int japam_count;
    int my_contribution;
    Button submit_button;
    TickerView ticker_mega, ticker_local, ticker_mycontribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
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
/*

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (state == 1) {
                plus();
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (state == 1) {
                minus();
            }
        }
        return true;
    }
*/

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (state == 1) {
                        minus();
                    }

                    //TODO
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (state == 1) {
                        plus();
                    }

                    //TODO
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
}
