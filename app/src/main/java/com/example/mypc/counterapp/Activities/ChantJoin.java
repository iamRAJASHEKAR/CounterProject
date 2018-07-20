package com.example.mypc.counterapp.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Counter.CounterActivity;
import com.example.mypc.counterapp.Database.DatabaseManager;
import com.example.mypc.counterapp.Database.Userdata;
import com.example.mypc.counterapp.Fonts.ButtonRegular;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.DeleteChantServerObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChantJoin extends AppCompatActivity {

    TextView toolbar_head, chantname, chantdesc;
    ImageView tool_icon;
    RelativeLayout relative_toolbar;
    MaterialDialog mprogress;
    ImageView public_edit, public_delete;
    ButtonRegular btnJoin;
    ArrayList<Userdata> userdataArrayList;
    int position;
    String chant_countid, privacy, user_Email, userid, created_user, chant_created, chant_name, chant_desc;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chant_join);

        userdataArrayList = new ArrayList<>();
        public_edit = findViewById(R.id.public_edit);
        public_delete = findViewById(R.id.public_delete);
        toolbar_head = findViewById(R.id.toolabr_title);
        tool_icon = findViewById(R.id.toolabar_icon);
        relative_toolbar = findViewById(R.id.relative_back);
        chantname = findViewById(R.id.ChantName);
        chantdesc = findViewById(R.id.chantDescription);
        chantdesc.setMovementMethod(new ScrollingMovementMethod());
        toolbar_head.setText("Public Chants");
        tool_icon.setBackground(getResources().getDrawable(R.drawable.ic_back_arrow));

        relative_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnJoin = findViewById(R.id.submitButton);
        btnJoin.setOnClickListener(ClickBtnJoin);
        getintentdata();
        operations();
    }

    View.OnClickListener ClickBtnJoin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            submiting();
        }
    };

    public void getintentdata() {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        user_Email = prefs.getString("email", "No name defined");

        chant_countid = getIntent().getExtras().getString("chant_id");
        privacy = getIntent().getExtras().getString("chant_privacy");
        position = getIntent().getExtras().getInt("position");
        chant_created = getIntent().getExtras().getString("chant_created_email");
        created_user = getIntent().getExtras().getString("chant_createmail");
        userid = getIntent().getExtras().getString("userid");
        chant_name = getIntent().getExtras().getString("chantname");
        chant_desc = getIntent().getExtras().getString("chant_dec");
        chantname.setText(chant_name);
        chantdesc.setText(chant_desc);

        if (chant_countid.equals("private")) {
            toolbar_head.setText("private Chants");

        }
        Log.e("joinerrdata", chant_countid + chant_created + chant_name + chant_desc);
    }


    public void submiting() {

        Intent intent = new Intent(getApplicationContext(), CounterActivity.class);
        intent.putExtra("chant_id", chant_countid);
        intent.putExtra("chant_created_email", chant_created);
        intent.putExtra("chantname", chant_name);
        intent.putExtra("chant_decrp", chant_desc);
        Log.e("chgejhbj", chant_countid + chant_created + chant_name + chant_desc);
        startActivity(intent);
        finish();
    }


    public void operations() {
        public_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("editdelete", user_Email + created_user);

                if (user_Email.equals(created_user)) {
                    deleteChantDialog();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "chant created by others you can't delete", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


        public_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_Email.equals(created_user)) {
                    Intent intent = new Intent(getApplicationContext(), EditChantActivity.class);
                    intent.putExtra("chant_name", chant_name);
                    intent.putExtra("chant_dec", chant_desc);
                    intent.putExtra("chant_id", chant_countid);
                    intent.putExtra("userid", userid);
                    intent.putExtra("privacy", "private");
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "chant created by others you can't Edit", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

        });
    }


    public void deleteChantDialog() {

        TextViewRegular yes, no, text_dialog;
        final Dialog dialog = new Dialog(ChantJoin.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_logout_dialog);
        yes = dialog.findViewById(R.id.yes);
        no = dialog.findViewById(R.id.no);
        text_dialog = dialog.findViewById(R.id.dialog_text);
        text_dialog.setText("Are you sure you want to delete ?");

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("call", "dialog yes");
                if (privacy.equals("private")) {
                    userdataArrayList = DatabaseManager.getInstance().getalluser();
                    DatabaseManager.getInstance().delete_user(userdataArrayList.get(position));
                    finish();
                } else {

                    deleteChant();
                }

                if (dialog.isShowing()) {
                    dialog.dismiss();
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

    /////server api for delete
    public void deleteChant() {
        display_Progress("Deleting...");
        DeleteChantServerObject deleteObj = new DeleteChantServerObject();
        deleteObj.chantId = chant_countid;
        deleteObj.email = user_Email;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<DeleteChantServerObject> deletechant = api.deleteChant(deleteObj);
        deletechant.enqueue(new Callback<DeleteChantServerObject>() {
            @Override
            public void onResponse(Call<DeleteChantServerObject> call, Response<DeleteChantServerObject> response) {
                hide_ProgressDialog();
                String deleteStatus;
                if (response.body() != null) {
                    deleteStatus = response.body().response;
                    Log.e("deleteStatus", " " + deleteStatus);
                    if (deleteStatus.equals("3")) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteChantServerObject> call, Throwable t) {
                hide_ProgressDialog();

                Toast.makeText(ChantJoin.this, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void display_Progress(String msg) {
        mprogress = new MaterialDialog.Builder(ChantJoin.this).content(msg).canceledOnTouchOutside(false).progress(true, 0).show();
    }

    private void hide_ProgressDialog() {
        if (mprogress != null && mprogress.isShowing()) {
            mprogress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
