package com.example.mypc.counterapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Fonts.EditTextRegular;
import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.Network.ConnectionReceiver;
import com.example.mypc.counterapp.Network.TestApplication;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.HelpusServerObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HelpUsActivity extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {

    Toolbar toolbar;
    ImageView toolbaricon;
    TextViewBold toolbar_text;
    RelativeLayout relative_back;
    EditTextRegular editName, editEmail, editPhone, editHelpMsg, edit_subject;
    ButtonBold btnSend;
    String name, user_name, user_email, email, phoneno, helpmsg, subject;
    ScrollView scrollView;
    public boolean isConnected;
    MaterialDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpus);
        checkConnection();
        init();
        user_data();

    }

    public void init() {
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbaricon = findViewById(R.id.toolabar_icon);
        toolbaricon.setImageResource(R.drawable.ic_back_arrow);
        toolbaricon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar_text = findViewById(R.id.toolabr_title);
        toolbar_text.setText(R.string.help_text);
        scrollView = findViewById(R.id.scrollView);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_surname);
        editPhone = findViewById(R.id.edit_firstname);
        editHelpMsg = findViewById(R.id.edit_help);
        editHelpMsg.setOnTouchListener(touchListener);
        edit_subject = findViewById(R.id.edit_subject);
        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(BtnSendClick);
        relative_back = findViewById(R.id.relative_back);
        relative_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        public boolean onTouch(final View v, final MotionEvent motionEvent) {
            if (v.getId() == R.id.edit_help) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
            }
            return false;
        }
    };

    View.OnClickListener BtnSendClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isConnected) {
                validate();
            } else {
                Toast.makeText(HelpUsActivity.this, "No Internet", Toast.LENGTH_SHORT).show();

            }
        }
    };


    /////////editText Validations
    public void validate() {
        name = editName.getText().toString();
        email = editEmail.getText().toString();
        phoneno = editPhone.getText().toString();
        helpmsg = editHelpMsg.getText().toString();
        subject = edit_subject.getText().toString();

        if (name.isEmpty()) {
            editName.setError("Name Can'not be empty");
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Enter Valid Email");
        } else if (phoneno.isEmpty() || phoneno.length() < 10) {
            editPhone.setError("Enter Valid Phone number");
        } else if (helpmsg.isEmpty()) {
            editHelpMsg.setError("Enter the message");
        } else if (subject.isEmpty()) {
            edit_subject.setError("Enter Subject");
        } else {
            sendHelpMessage();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean connect) {
        isConnected = connect;
        if (!isConnected) {
            Toast.makeText(this, "check internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, " Connected to internet ", Toast.LENGTH_SHORT).show();
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

    public void displayProgressDialog()
    {
        mProgress = new MaterialDialog.Builder(HelpUsActivity.this).content("Loading").canceledOnTouchOutside(false).progress(true, 0).show();
    }

    private void hideProgressDialog()
    {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }

    }


    ///////HelpUs server implementation

    public void sendHelpMessage() {
        displayProgressDialog();
        HelpusServerObject helpObj = new HelpusServerObject();
        helpObj.name = name.trim();
        helpObj.email = email.trim();
        helpObj.phone = phoneno.trim();
        helpObj.subject = subject.trim();
        helpObj.message = helpmsg.trim();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).
                addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<HelpusServerObject> helpus = api.helpUs(helpObj);
        helpus.enqueue(new Callback<HelpusServerObject>() {
            @Override
            public void onResponse(Call<HelpusServerObject> call, Response<HelpusServerObject> response) {
                if (response.body() != null) {

                    Log.e("helpstatusCode", " " + response.body().response);
                    String statuscode = response.body().response;
                    if (statuscode.equals("3"))
                    {
                        hideProgressDialog();
                        Toast toast = Toast.makeText(HelpUsActivity.this, "Message Sent", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        finish();
                    }
                } else
                    {
                    Toast toast = Toast.makeText(HelpUsActivity.this, "Failed Try again", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<HelpusServerObject> call, Throwable t) {
                Log.e("helpFailure", "Failed");
                hideProgressDialog();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    public void user_data() {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        user_email = prefs.getString("email", "No name defined");
        user_name = prefs.getString("name", "No name defined");
        editName.setText(user_name);
        editEmail.setText(user_email);

    }

}
