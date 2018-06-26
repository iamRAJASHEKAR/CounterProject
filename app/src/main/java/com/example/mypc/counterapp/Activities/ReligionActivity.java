package com.example.mypc.counterapp.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Model.Religion;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.AddRelegionObject;
import com.example.mypc.counterapp.ServerObject.CounterServerObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReligionActivity extends AppCompatActivity {

    MaterialDialog mProgress;
    Button noReligionFound;
    RecyclerView recyclerViewReligion;
    ArrayList<Religion> religionarraylist;
    String[] religions = {"Hindu", "Muslim", "Christian"};
    ReligionAdapter religionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_religion);
        init();
    }

    public void init() {
        religionarraylist = new ArrayList<>();
        recyclerViewReligion = findViewById(R.id.recyclerview_religion);
        noReligionFound = findViewById(R.id.btn_no_religion);
        noReligionFound.setOnClickListener(NoReligionFound);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewReligion.setLayoutManager(layoutManager);
        //  recyclerViewReligion.setLayoutManager(new LinearLayoutManager(this));
        religionAdapter = new ReligionAdapter();
        recyclerViewReligion.setAdapter(religionAdapter);

    }

  /*  public void setdata() {
        for (int i = 0; i < religions.length; i++) {
            religionarraylist.add(new Religion(religions[i]));
        }
    }*/


    View.OnClickListener NoReligionFound = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showReligiondialog();
        }
    };


    ////Dialog for entering the religion
    public void showReligiondialog() {
        TextViewRegular save, cancel;
        final EditText relegion_data;
        final Dialog dialog = new Dialog(ReligionActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_religion_dialog);
        save = dialog.findViewById(R.id.save);
        cancel = dialog.findViewById(R.id.cancel);
        relegion_data = dialog.findViewById(R.id.dialog_text);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String relegion = relegion_data.getText().toString().trim();
                if (isNetworkAvailable() == true) {

                    add_relegion(relegion);
                    Log.e("dialogueyes", "dialog yes" + relegion);
                } else {
                    Toast toast = Toast.makeText(ReligionActivity.this, " Check Internet Connection", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                dialog.dismiss();
                // startActivity(new Intent(getApplicationContext(),LoginActivity.class));


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("dialoguecancel", "dialog no");
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void hideProgressDialog() {

        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }

    }

    public void displayProgressDialog() {
        mProgress = new MaterialDialog.Builder(ReligionActivity.this).content("Loading").canceledOnTouchOutside(false).progress(true, 0).show();

    }

    class ReligionAdapter extends RecyclerView.Adapter<ReligionAdapter.ViewHolder> {


        @Override
        public ReligionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_religion_list, null);
            final ViewHolder viewHolder = new ViewHolder(v, getApplicationContext());
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ReligionAdapter.ViewHolder holder, int position) {
            holder.btn_religion.setText(religionarraylist.get(position).getReligion_name());

        }

        @Override
        public int getItemCount() {
            return religionarraylist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ButtonBold btn_religion;

            public ViewHolder(View itemView, Context applicationContext) {
                super(itemView);
                btn_religion = itemView.findViewById(R.id.btnReligio);
                btn_religion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        //     finish();
                    }
                });

            }
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void add_relegion(String relegion) {

        displayProgressDialog();

        AddRelegionObject relegion_object = new AddRelegionObject();

        relegion_object.relegion = relegion;

        Log.e("helpObj", " " + relegion);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).
                addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<AddRelegionObject> helpus = api.add_relegion(relegion_object);
        helpus.enqueue(new Callback<AddRelegionObject>() {
            @Override
            public void onResponse(Call<AddRelegionObject> call, Response<AddRelegionObject> response) {
                if (response.body() != null)
                    Log.e("responsecalling", " " + response.body().getResponse());
                hideProgressDialog();
                String status_code = response.body().getResponse();
                {
                    if (status_code.equals("3")) {
                        Toast toast = Toast.makeText(ReligionActivity.this, "Relegion added successfully", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else if (status_code.equals("2")) {
                        Toast toast = Toast.makeText(ReligionActivity.this, "Relegion exist ", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {
                        Toast toast = Toast.makeText(ReligionActivity.this, " Failed to add Relegion ", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    Log.e("checking", "onrespo");
                }
            }

            @Override
            public void onFailure(Call<AddRelegionObject> call, Throwable t) {
                hideProgressDialog();

                Toast toast = Toast.makeText(ReligionActivity.this, " Failed to add Relegion ", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Log.e("helpFailure", "Failed");
            }
        });

    }
/*
    public class AddRelegion extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(ReligionActivity.this, "Sending data", Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            add_relegion();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(ReligionActivity.this, "Sucesss", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }
    }*/

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;

        } else {
            return false;
        }
    }
}
