package com.example.mypc.counterapp.Counter;

import android.app.Dialog;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Activities.HomeActivity;
import com.example.mypc.counterapp.Controllers.FetchPublicChantController;
import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Network.ConnectionReceiver;
import com.example.mypc.counterapp.Network.TestApplication;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.ChantCount;
import com.example.mypc.counterapp.ServerObject.MegaCount;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CounterActivity extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {
    TextView text_screenmodes, text_megacount;
    int state = 0;
    ImageButton minus_button, plus_button;
    int japam_count;
    int my_contribution;
    TextView chantname, chantdesc;
    ButtonBold submit_button;
    TickerView ticker_mega, ticker_local, ticker_mycontribution;
    Toolbar countertoolbar;
    String chant_countid, chant_created, chant_name, chant_desc;
    ImageView toolbar_icon;
    TextViewBold conterText;
    MaterialDialog mProgress;
    public boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        //check internet connection
        checkConnection();
        countertoolbar = findViewById(R.id.toolBar);
        setSupportActionBar(countertoolbar);
        chantname = findViewById(R.id.ChantName);
        chantdesc = findViewById(R.id.chantDescription);
        toolbar_icon = findViewById(R.id.toolabar_icon);
        conterText = findViewById(R.id.toolabr_title);
        toolbar_icon.setImageResource(R.drawable.ic_back_arrow);
        conterText.setText("Counter");

        chantdesc.setMovementMethod(new ScrollingMovementMethod());
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
        ticker_mega.setText("- - - -");
        final String mega_count = "<u>MEGA COUNT</u>";
        text_megacount.setText(Html.fromHtml(mega_count));

        text_screenmodes.setText(Html.fromHtml(htmlString));
        screen_modes();
        counter();
        getintentdata();
        chant_count();
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
                if (japam_count > 0) {
                    int contribute = japam_count;
                    int countribution_count = my_contribution + contribute;
                    my_contribution = countribution_count;
                    ticker_mycontribution.setText(String.valueOf(my_contribution));
                    japam_count = 0;
                    ticker_local.setText(String.valueOf(japam_count));
                    submit_count();

                } else {
                    Toast.makeText(CounterActivity.this, "Invalid Count", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void minus() {

        if (japam_count > 0) {

            japam_count = japam_count - 1;
            String check_forzero = String.valueOf(japam_count);
            ticker_local.setText(String.valueOf(japam_count));
            if (check_forzero.endsWith("0") || condi() == true) {
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
        if (check_forzero.endsWith("0") || condi() == true) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 1000 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
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

    public void submit_count() {
        if (isConnected) {
            displayProgressDialog("Updating...");
            String count = String.valueOf(my_contribution);
            MegaCount megaCount = new MegaCount();
            megaCount.mycount = count;
            megaCount.chant_id = chant_countid;
            megaCount.email = chant_created;
            Log.e("apidata", count + chant_countid + chant_created);
            Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
            ServerApiInterface apiInterface = retrofit.create(ServerApiInterface.class);
            Call<MegaCount> logout_call = apiInterface.mega_count(megaCount);
            logout_call.enqueue(new Callback<MegaCount>() {
                @Override
                public void onResponse(Call<MegaCount> call, Response<MegaCount> response) {
                    hide_login_ProgressDialog();
                    if (response.body() != null) {
                        if (response.body().getResponse().equals("3")) {
                            Log.e("counterdata", response.body().getMegacount());
                            ticker_mega.setText(response.body().getMegacount());
                            Toast.makeText(CounterActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CounterActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.e("responseoncounter", response.body().getResponse() + "\n" + response.body().getMegacount()
                                + "\n" + response.body().getEmail()
                                + "\n" + response.body().getMessage());
                    } else {
                        //  Toast.makeText(HomeActivity.this, "Failed to logout", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MegaCount> call, Throwable t) {
                    hide_login_ProgressDialog();
                    Log.e("logoot_failure", t.getMessage());
                }
            });
        } else {

            try_submit();
        }
    }

    public void getintentdata() {
        chant_countid = getIntent().getExtras().getString("chant_id");
        chant_created = getIntent().getExtras().getString("chant_created");
        chant_name = getIntent().getExtras().getString("chantname");
        chant_desc = getIntent().getExtras().getString("chant_decrp");
        chantname.setText(chant_name);
        chantdesc.setText(chant_desc);

        Log.e("counterdata", chant_countid + chant_created + my_contribution + chant_name + chant_desc);
    }

    public void chant_count() {
        if (isConnected) {
            displayProgressDialog("Getting Mega Count..");
            ChantCount chantCount = new ChantCount();
            chantCount.chantid = chant_countid;
            Log.e("chanid", chant_countid);
            Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
            ServerApiInterface apiInterface = retrofit.create(ServerApiInterface.class);
            Call<ChantCount> chant_call = apiInterface.chant_count(chantCount);
            chant_call.enqueue(new Callback<ChantCount>() {
                @Override
                public void onResponse(Call<ChantCount> call, Response<ChantCount> response) {
                    hide_login_ProgressDialog();
                    if (response.body() != null) {
                        if (response.body().getResponse().equals("3")) {
                            Log.e("chantapicount", response.body().getMegacount());
                            ticker_mega.setText(response.body().getMegacount());
                        } else {
                            Toast.makeText(CounterActivity.this, "Fetching Failed", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        //  Toast.makeText(HomeActivity.this, "Failed to logout", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ChantCount> call, Throwable t) {
                    hide_login_ProgressDialog();

                    Log.e("logoot_failure", t.getMessage());
                }
            });

        } else {
            tryagain();
        }
    }

    public void displayProgressDialog(String msg) {
        mProgress = new MaterialDialog.Builder(CounterActivity.this).content(msg).canceledOnTouchOutside(false).progress(true, 0).show();

    }

    private void hide_login_ProgressDialog() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }

    }

    public boolean condi() {

        int a = japam_count;
        int b = 108;
        if (a % b == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean connect) {


        isConnected = connect;
        if (!isConnected) {
            Toast.makeText(this, "check internet Connection", Toast.LENGTH_SHORT).show();
            //  Snackbar.make(findViewById(R.id.relative_login), "check internet Connection", Snackbar.LENGTH_SHORT).show();
        } else {
            // Snackbar.make(findViewById(R.id.relative_login), " internet Connection on", Snackbar.LENGTH_INDEFINITE).show();
            //  Log.e("network status", " On");
        }
    }

    private void checkConnection() {
        isConnected = ConnectionReceiver.isConnected();
        if (!isConnected) {
            Toast.makeText(this, "check internet Connection", Toast.LENGTH_SHORT).show();
            Log.e("oncreate network status", " off");
        } else {
            Log.e("oncreate network status", " on");
        }
    }

    @Override
    protected void onResume() {
        TestApplication.getInstance().setConnectionListener(this);
        super.onResume();
    }

    public void tryagain() {
        TextViewRegular yes, no, title;
        final Dialog dialog = new Dialog(CounterActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_logout_dialog);
        yes = dialog.findViewById(R.id.yes);
        no = dialog.findViewById(R.id.no);
        no.setVisibility(View.GONE);
        yes.setText("Try Again");
        title = dialog.findViewById(R.id.dialog_text);
        title.setText("Failed to Get Mega Count ");
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (isConnected) {
                    chant_count();
                } else {
                    tryagain();
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("call", "dialog no");
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void try_submit() {
        TextViewRegular yes, no, title;
        final Dialog dialog = new Dialog(CounterActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_logout_dialog);
        yes = dialog.findViewById(R.id.yes);
        no = dialog.findViewById(R.id.no);
        no.setVisibility(View.GONE);
        yes.setText("Try Again");
        title = dialog.findViewById(R.id.dialog_text);
        title.setText("Failed to update Count ");
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (isConnected) {
                    submit_count();
                } else {
                    try_submit();
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("call", "dialog no");
                dialog.dismiss();
            }
        });

        dialog.show();


    }

}
