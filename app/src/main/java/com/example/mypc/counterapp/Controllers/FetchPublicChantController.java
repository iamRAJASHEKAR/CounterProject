package com.example.mypc.counterapp.Controllers;

import android.content.Context;
import android.util.Log;

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

    public Context context;

    public static FetchPublicChantController getinstance() {
        if (obj == null) {
            obj = new FetchPublicChantController();
            obj.publicList = new ArrayList<>();
        }
        return obj;
    }

    public void fillContext(Context context1) {
        context = context1;
    }

    public void publicchant_data(final String user_email)
    {

        FetchingPublicChant logoutServerObjects = new FetchingPublicChant();

        logoutServerObjects.email = user_email;
        Log.e("fetchcontroller", user_email);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();

        ServerApiInterface apiInterface = retrofit.create(ServerApiInterface.class);
        Call<FetchingPublicChant> logout_call = apiInterface.fetch_public(logoutServerObjects);
        logout_call.enqueue(new Callback<FetchingPublicChant>() {
            @Override
            public void onResponse(Call<FetchingPublicChant> call, Response<FetchingPublicChant> response) {

                if (response.body() != null) {
                    publicList = response.body().getPublicList();
                    friendschant_data(user_email);
                    Log.e("logout", response.body().getResponse());
                    Log.e("array", String.valueOf(publicList.size()));
                    for (int i = 0; i < response.body().getPublicList().size(); i++) {
                        Log.e("publicprofile", String.valueOf(
                                response.body().getPublicList().get(i).chant_description) + "\n" +
                                response.body().getPublicList().get(i).chant_id + "\n" +
                                response.body().getPublicList().get(i).chant_name + "\n" +
                                response.body().getPublicList().get(i).created_by + "\n" +
                                response.body().getPublicList().get(i).created_email + "\n" +
                                response.body().getPublicList().get(i).privacy + "\n");
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<FetchingPublicChant> call, Throwable t) {
                Log.e("logoot_failure", t.getMessage());
            }
        });
    }

    public void friendschant_data(final String user_email) {

        FetchingFriendsChants logoutServerObjects = new FetchingFriendsChants();

        logoutServerObjects.email = user_email;
        Log.e("fetchcontroller", user_email);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();

        ServerApiInterface apiInterface = retrofit.create(ServerApiInterface.class);
        Call<FetchingFriendsChants> logout_call = apiInterface.fetch_friends(logoutServerObjects);
        logout_call.enqueue(new Callback<FetchingFriendsChants>() {
            @Override
            public void onResponse(Call<FetchingFriendsChants> call, Response<FetchingFriendsChants> response) {

                if (response.body() != null) {
                    EventBus.getDefault().post(new LoginActivity.MessageEvent("refreshchant"));
                    Log.e("arrayfriends", String.valueOf(publicList.size()));
                    ArrayList<PublicList> publicaray = new ArrayList<>();

                    publicaray = response.body().getPublicList();
                    publicList.addAll(publicaray);
                    Log.e("friends", response.body().getResponse());
                    Log.e("arrayfriends", String.valueOf(publicList.size()));
                    for (int i = 0; i < response.body().getPublicList().size(); i++)
                    {
                        Log.e("friendARRAYprofile", String.valueOf
                                (response.body().getPublicList().get(i).chant_description) + "\n" +
                                response.body().getPublicList().get(i).chant_id + "\n" +
                                response.body().getPublicList().get(i).chant_name + "\n" +
                                response.body().getPublicList().get(i).created_by + "\n" +
                                response.body().getPublicList().get(i).created_email + "\n" +
                                response.body().getPublicList().get(i).privacy + "\n");
                    }
                } else
                    {
                }
            }

            @Override
            public void onFailure(Call<FetchingFriendsChants> call, Throwable t) {
                Log.e("logoot_failure", t.getMessage());
            }
        });
    }

}
