package com.example.mypc.counterapp.ServerApiInterface;

import com.example.mypc.counterapp.ServerObject.AddChantServerObject;
import com.example.mypc.counterapp.ServerObject.CounterServerObject;
import com.example.mypc.counterapp.ServerObject.HelpusServerObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServerApiInterface
{
    public static String Base_Url = "http://107.175.83.105/CounterApp/webapi/counter/";

    //@helpus
    @POST("helpus")
    Call<HelpusServerObject> helpUs(@Body HelpusServerObject helpusObject);

    //Fetching the religions API's
    @GET("fetchreligions")
    Call<CounterServerObject> fetchReligions();

    //adding the chants
    @POST("addchant")
    Call<AddChantServerObject> addChants(@Body AddChantServerObject AddChantObj );



}
