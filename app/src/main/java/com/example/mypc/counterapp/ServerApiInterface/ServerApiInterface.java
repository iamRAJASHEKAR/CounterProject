package com.example.mypc.counterapp.ServerApiInterface;

import com.example.mypc.counterapp.ServerObject.CounterServerObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServerApiInterface
{
    public static String Base_Url = "http://107.175.83.105/CounterApp/webapi/counter";

    @FormUrlEncoded
    @POST("helpus")
    Call<CounterServerObject> helpUs(@Body JSONObject object);


}
