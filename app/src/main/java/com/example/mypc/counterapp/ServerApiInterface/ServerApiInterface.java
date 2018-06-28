package com.example.mypc.counterapp.ServerApiInterface;

import com.example.mypc.counterapp.ServerObject.AddChantServerObject;
import com.example.mypc.counterapp.ServerObject.AddRelegionObject;
import com.example.mypc.counterapp.ServerObject.ChantFriendList;
import com.example.mypc.counterapp.ServerObject.CounterServerObject;
import com.example.mypc.counterapp.ServerObject.FetchingFriendsChants;
import com.example.mypc.counterapp.ServerObject.FetchingPublicChant;
import com.example.mypc.counterapp.ServerObject.HelpusServerObject;
import com.example.mypc.counterapp.ServerObject.LogoutServerObjects;
import com.example.mypc.counterapp.ServerObject.UserLoginObjects;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServerApiInterface {
    public static String Base_Url = "http://107.175.83.105/CounterApp/webapi/counter/";

    //@helpus
    @POST("helpus")
    Call<HelpusServerObject> helpUs(@Body HelpusServerObject helpusObject);

    //Fetching the religions API's
    @GET("fetchreligions")
    Call<CounterServerObject> fetchReligions();

    //checking
    //adding the chants
    @POST("addchant")
    Call<AddChantServerObject> addChants(@Body AddChantServerObject AddChantObj);

    @POST("addreligion")
    Call<AddRelegionObject> add_relegion(@Body AddRelegionObject add_relegion);

    @POST("login")
    Call<UserLoginObjects> login_user(@Body UserLoginObjects login_user);

    @POST("logout")
    Call<LogoutServerObjects> logout_user(@Body LogoutServerObjects logout_user);

    @POST("public ")
    Call<FetchingPublicChant> fetch_public(@Body FetchingPublicChant fetch_public);

    @POST("friend")
    Call<FetchingFriendsChants> fetch_friends(@Body FetchingFriendsChants fetch_friends);

//adding hh 
    @POST("friends")
    Call<ChantFriendList> fetchchant_friends(@Body ChantFriendList chantFriendList);
}
