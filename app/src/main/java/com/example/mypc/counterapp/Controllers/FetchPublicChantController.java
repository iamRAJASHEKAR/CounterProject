package com.example.mypc.counterapp.Controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Activities.HomeActivity;
import com.example.mypc.counterapp.Activities.LoginActivity;
import com.example.mypc.counterapp.Model.PublicList;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.FetchingFriendsChants;
import com.example.mypc.counterapp.ServerObject.FetchingPublicChant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchPublicChantController {
    public static FetchPublicChantController obj;
    public ArrayList<PublicList> publicList;
    public ArrayList<PublicList> nepublicList;
    public ArrayList<PublicList> friendslist;
    MaterialDialog mProgress;
    public Context context;
    ProgressDialog pDialog;

    public static FetchPublicChantController getinstance() {
        if (obj == null) {
            obj = new FetchPublicChantController();
            obj.publicList = new ArrayList<>();
            obj.nepublicList = new ArrayList<>();
            obj.friendslist = new ArrayList<>();
        }
        return obj;
    }

    public void fillContext(Context context1) {
        this.context = context1;
    }

    public void publicchant_data(final String user_email, final String religion) {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        pDialog.show();

        FetchingPublicChant logoutServerObjects = new FetchingPublicChant();

        logoutServerObjects.email = user_email;
        logoutServerObjects.religion = religion;
        Log.e("fetchcontroller", user_email);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();

        ServerApiInterface apiInterface = retrofit.create(ServerApiInterface.class);
        Call<FetchingPublicChant> logout_call = apiInterface.fetch_public(logoutServerObjects);
        logout_call.enqueue(new Callback<FetchingPublicChant>() {
            @Override
            public void onResponse(Call<FetchingPublicChant> call, Response<FetchingPublicChant> response) {
                if (response.body() != null) {


                    nepublicList.clear();
                    nepublicList = response.body().getPublicList();
                    publicList = response.body().getPublicList();
                    friendschant_data(user_email, religion);
                    Log.e("publicresponse", response.body().getResponse());
                    Log.e("arraypublicList", String.valueOf(nepublicList.size()));
                    for (int i = 0; i < response.body().getPublicList().size(); i++) {
                        Log.e("publicprofil" +
                                "le", String.valueOf(
                                response.body().getPublicList().get(i).chant_description) + "\n" +
                                response.body().getPublicList().get(i).chant_id + "\n" +
                                response.body().getPublicList().get(i).chant_name + "\n" +
                                response.body().getPublicList().get(i).created_by + "\n" +
                                response.body().getPublicList().get(i).created_email + "\n" +
                                response.body().getPublicList().get(i).privacy + "\n");
                    }
                } else {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    EventBus.getDefault().post(new LoginActivity.MessageEvent("error"));
                }

            }

            @Override
            public void onFailure(Call<FetchingPublicChant> call, Throwable t) {
                EventBus.getDefault().post(new LoginActivity.MessageEvent("error"));

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                //    Log.e("logoot_failure", t.getMessage());
            }
        });
    }

    public void friendschant_data(final String user_email, final String religion) {
        FetchingFriendsChants logoutServerObjects = new FetchingFriendsChants();
        logoutServerObjects.email = user_email;
        logoutServerObjects.religion = religion;
        Log.e("fetchcontroller", user_email);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface apiInterface = retrofit.create(ServerApiInterface.class);
        Call<FetchingFriendsChants> logout_call = apiInterface.fetch_friends(logoutServerObjects);
        logout_call.enqueue(new Callback<FetchingFriendsChants>() {
            @Override
            public void onResponse(Call<FetchingFriendsChants> call, Response<FetchingFriendsChants> response) {

                if (response.body() != null) {
                    //Log.e("arrayfriends", String.valueOf(publicList.size()));
                    ArrayList<PublicList> publicaray = new ArrayList<>();
                    friendslist.clear();
                    friendslist = response.body().getPublicList();
                    EventBus.getDefault().post(new LoginActivity.MessageEvent("refreshchant"));

                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    //publicList.addAll(publicaray);
                    Log.e("friends", response.body().getResponse());
                    Log.e("arrayfriends", String.valueOf(friendslist.size()));
                    for (int i = 0; i < response.body().getPublicList().size(); i++) {
                        Log.e("friendARRAYprofile", String.valueOf
                                (response.body().getPublicList().get(i).chant_description) + "\n" +
                                response.body().getPublicList().get(i).chant_id + "\n" +
                                response.body().getPublicList().get(i).chant_name + "\n" +
                                response.body().getPublicList().get(i).created_by + "\n" +
                                response.body().getPublicList().get(i).created_email + "\n" +
                                response.body().getPublicList().get(i).privacy + "\n");
                    }
                } else {
                    EventBus.getDefault().post(new LoginActivity.MessageEvent("error"));

                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<FetchingFriendsChants> call, Throwable t) {
                EventBus.getDefault().post(new LoginActivity.MessageEvent("error"));
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }
}
