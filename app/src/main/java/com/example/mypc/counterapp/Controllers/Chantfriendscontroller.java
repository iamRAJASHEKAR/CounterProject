package com.example.mypc.counterapp.Controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.example.mypc.counterapp.Activities.LoginActivity;
import com.example.mypc.counterapp.Model.FriendsList;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.ChantFriendList;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Chantfriendscontroller {
    public static Chantfriendscontroller obj;
    public ArrayList<FriendsList> chants_Friends;
    public ArrayList<FriendsList> active_friends;
    public ArrayList<FriendsList> inactive_friends;
    Context context;
    ProgressDialog pDialog;

    public static Chantfriendscontroller getintance() {
        if (obj == null) {
            obj = new Chantfriendscontroller();
            obj.chants_Friends = new ArrayList<>();
            obj.active_friends = new ArrayList<>();
            obj.inactive_friends = new ArrayList<>();
        }
        return obj;
    }


    public void fillcontext(Context context) {
        this.context = context;

    }

    public void fetch_chantFriends(final String chant_id) {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        pDialog.show();
        ChantFriendList friendList = new ChantFriendList();
        friendList.chant_id = chant_id;
        Log.e("chantidlog", chant_id);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface apiInterface = retrofit.create(ServerApiInterface.class);
        final Call<ChantFriendList> chant_friend = apiInterface.fetchchant_friends(friendList);
        chant_friend.enqueue(new Callback<ChantFriendList>() {
            @Override
            public void onResponse(Call<ChantFriendList> call, Response<ChantFriendList> response) {
                /*    Log.e("json", Gson.class);*/
                Log.e("chantresponse", response.body().getResponse());
                if (response.body() != null) {
                    Log.e("chantsfriends", response.body().getResponse());
                    chants_Friends.clear();
                    active_friends.clear();
                    inactive_friends.clear();
                    chants_Friends = response.body().getFriendsList();
                    Log.e("chantssizer", String.valueOf(response.body().getFriendsList().size()));

                    for (FriendsList friends_active : chants_Friends) {
                        if (friends_active.isactive.equals("1")) {
                            active_friends.add(friends_active);
                            Log.e("friendsactive", String.valueOf(active_friends.size()));
                        } else {
                            inactive_friends.add(friends_active);
                            Log.e("inactivefriends", String.valueOf(inactive_friends.size()));
                        }
                    }
                    EventBus.getDefault().post(new LoginActivity.MessageEvent("active_friends"));
                    Log.e("chantarray", String.valueOf(chants_Friends.size()));
                    for (int i = 0; i < response.body().getFriendsList().size(); i++) {
                        String active = response.body().getFriendsList().get(i).isactive;
                        String mail = response.body().getFriendsList().get(i).email;
                        Log.e("dataarray", mail + "\n" + active);
                    }

                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                } else {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChantFriendList> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Log.e("logoot_failure", t.getMessage());
            }
        });

    }

}

            /*    if (response.body() != null) {
                    Log.e("chantsfriends", response.body().getResponse());
                    chants_Friends = response.body().getChantsFriends();
                    for (FriendsList friends_active : chants_Friends) {
                        if (friends_active.isactive.equals("1")) {
                            EventBus.getDefault().post(new LoginActivity.MessageEvent("active_friends"));
                            active_friends.add(friends_active);
                            Log.e("friendsactive", String.valueOf(active_friends.size()));
                        } else {
                            inactive_friends.add(friends_active);
                            Log.e("inactivefriends", String.valueOf(inactive_friends.size()));
                        }
                    }


                    Log.e("chantarray", String.valueOf(chants_Friends.size()));
                    for (int i = 0; i < response.body().getChantsFriends().size(); i++) {
                        String active = response.body().getChantsFriends().get(i).isactive;
                        String mail = response.body().getChantsFriends().get(i).email;
                        Log.e("chantarraydata", mail + "\n" + active);
                    }
                } else {
                }
            */