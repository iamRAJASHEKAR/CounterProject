package com.example.mypc.counterapp.ServerObject;

import android.util.Log;

import com.example.mypc.counterapp.Model.Religion;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CounterController {
    public static CounterController counterObj;

    public ArrayList<Religion> religionArrayList;

    public static CounterController getInstance()
    {
        if (counterObj == null) {
            counterObj = new CounterController();
            counterObj.religionArrayList = new ArrayList<>();
        }
        return counterObj;
    }


    ////////////Fetching the religions Apis

    public void fetchReligions() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<CounterServerObject> fetchReligions = api.fetchReligions();
        fetchReligions.enqueue(new Callback<CounterServerObject>() {
            @Override
            public void onResponse(Call<CounterServerObject> call, Response<CounterServerObject> response) {
                if (response.body() != null) {
                    Log.e("fetchreligionStatuscode", " " + response.body().getResponse());
                    religionArrayList = response.body().getReligions();
                    Log.e("religions", " " + religionArrayList.size());

                }
            }

            @Override
            public void onFailure(Call<CounterServerObject> call, Throwable t) {
                Log.e("religion", "Failes");
            }
        });
    }

}
