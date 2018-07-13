package com.example.mypc.counterapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Database.DatabaseManager;
import com.example.mypc.counterapp.Database.Userdata;
import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Fonts.EditTextRegular;
import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.Network.ConnectionReceiver;
import com.example.mypc.counterapp.Network.TestApplication;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.AddChantServerObject;
import com.example.mypc.counterapp.ServerObject.EditChantServerObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditChantActivity extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {

    EditTextRegular editChantName, editChantDescription;
    Toolbar toolbar;
    TextViewBold tooltiltle;
    ImageView toolbarIcon;
    ButtonBold save;
    Userdata userdata;
    String email, chantName, timestamp, chantdes, chantID, user_id, privacy;
    public boolean isConnected;
    MaterialDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_editchant);
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        email = prefs.getString("email", "No name defined");
        getIntentData();
        Log.e("editemail", " " + email + user_id);

        checkConnection();
        init();


    }

    public void init() {
        toolbar = findViewById(R.id.toolBar);
        tooltiltle = findViewById(R.id.toolabr_title);
        toolbarIcon = findViewById(R.id.toolabar_icon);

        toolbarIcon.setImageResource(R.drawable.ic_back_arrow);
        tooltiltle.setText("Edit Chant");

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editChantName = findViewById(R.id.edit_chantName);
        editChantDescription = findViewById(R.id.edit_chant);
        editChantDescription.setOnTouchListener(touchListener);

        editChantName.setText(chantName);
        editChantDescription.setText(chantdes);

        save = findViewById(R.id.btn_save);
        save.setOnClickListener(saveBtnClick);
    }


    View.OnTouchListener touchListener = new View.OnTouchListener() {
        public boolean onTouch(final View v, final MotionEvent motionEvent) {
            if (v.getId() == R.id.edit_chant) {
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


    public void getIntentData() {
        long unixTime = System.currentTimeMillis() / 1000L;
        timestamp = String.valueOf(unixTime);
        chantName = getIntent().getExtras().getString("chant_name");
        chantdes = getIntent().getExtras().getString("chant_dec");
        chantID = getIntent().getExtras().getString("chant_id");
        user_id = getIntent().getExtras().getString("userid");
        Log.e("chanteditdata", timestamp + chantName + chantdes + chantID + privacy);
    }


    View.OnClickListener saveBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            chantName = editChantName.getText().toString();
            chantdes = editChantDescription.getText().toString();


            if (isConnected) {
                validations();
            } else {
                Toast.makeText(EditChantActivity.this, "No Internet", Toast.LENGTH_SHORT).show();

            }

        }
    };


    public void validations() {
        if (chantName.length() == 0) {
            editChantName.setError("Enter chant name");
        } else if (chantdes.length() == 0) {
            editChantDescription.setError("Enter the chant");
        } else if (chantID.equals("private")) {
            String chant_name = editChantName.getText().toString().trim();
            String chantdec = editChantDescription.getText().toString().trim();
            DatabaseManager.getInstance().update_user(chant_name, chantdec, timestamp, email, user_id);
            finish();
        } else {
            editChants();
        }
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
        super.onResume();
        TestApplication.getInstance().setConnectionListener(this);
        hideProgressDialog();

    }

    public void editChants() {
        displayProgressDialog();
        EditChantServerObject editChantServerObject = new EditChantServerObject();
        editChantServerObject.ChantName = chantName;
        editChantServerObject.chantdescription = chantdes;
        editChantServerObject.createdEmail = email;
        editChantServerObject.chantID = chantID;

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<EditChantServerObject> addChants = api.editChant(editChantServerObject);
        addChants.enqueue(new Callback<EditChantServerObject>() {
            @Override
            public void onResponse(Call<EditChantServerObject> call, Response<EditChantServerObject> response) {
                if (response.body() != null) {
                    String editstatuscode = response.body().reposnse;
                    Log.e("editchantresponse", " " + editstatuscode);

                    if (editstatuscode.equals("3")) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_SHORT).show();
                        hideProgressDialog();

                    } else if (editstatuscode.equals("0")) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_SHORT).show();
                        hideProgressDialog();

                    }
                }
            }

            @Override
            public void onFailure(Call<EditChantServerObject> call, Throwable t) {
                Log.e("editchantresponse", "Failed");
                hideProgressDialog();
            }
        });


    }

    public void displayProgressDialog() {
        mProgress = new MaterialDialog.Builder(EditChantActivity.this).content("Updating...").canceledOnTouchOutside(false).progress(true, 0).show();

    }

    private void hideProgressDialog() {

        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideProgressDialog();
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
}
