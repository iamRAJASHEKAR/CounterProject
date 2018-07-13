package com.example.mypc.counterapp.ServerObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Activities.LoginActivity;
import com.example.mypc.counterapp.Model.Religion;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CounterController {
    public static CounterController counterObj;
    MaterialDialog progress_;
    Context context;
    public ArrayList<Religion> religionArrayList;
    ProgressDialog pDialog;

    public static CounterController getInstance() {
        if (counterObj == null) {
            counterObj = new CounterController();
            counterObj.religionArrayList = new ArrayList<>();
        }
        return counterObj;
    }

    public void fillcontext(Context context) {
        this.context = context;

    }


    ////////////Fetching the religions Apis

    public void fetchReligions()
    {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<CounterServerObject> fetchReligions = api.fetchReligions();

        fetchReligions.enqueue(new Callback<CounterServerObject>() {
            @Override
            public void onResponse(Call<CounterServerObject> call, Response<CounterServerObject> response) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                if (response.body() != null) {

                    if (response.body().getResponse().equals("3")) {
                        Log.e("fetchreligionStatuscode", " " + response.body().getResponse());
                        religionArrayList = response.body().getReligions();
                        Log.e("religions", " " + religionArrayList.size());
                        EventBus.getDefault().post(new LoginActivity.MessageEvent("conttrollerreligion"));

                    }
                }
            }

            @Override
            public void onFailure(Call<CounterServerObject> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                EventBus.getDefault().post(new LoginActivity.MessageEvent("error"));
                Log.e("religion", "Failes");
            }
        });
    }

}
