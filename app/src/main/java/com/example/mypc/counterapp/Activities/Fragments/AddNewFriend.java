package com.example.mypc.counterapp.Activities.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Activities.AddChantActivity;
import com.example.mypc.counterapp.Counter.CounterActivity;
import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Fonts.ButtonRegular;
import com.example.mypc.counterapp.Fonts.EditTextRegular;
import com.example.mypc.counterapp.Network.ConnectionReceiver;
import com.example.mypc.counterapp.Network.TestApplication;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.AddFriendServerObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddNewFriend extends Fragment implements ConnectionReceiver.ConnectionReceiverListener {

    EditTextRegular addFriendName, addFriendEmail;
    ButtonRegular save;
    View view;
    public boolean isConnected;
    String frndname, friendEmail;
    MaterialDialog mProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_new_friend, container, false);
        checkConnection();
        init();
        return view;
    }


    public void init() {
        addFriendName = view.findViewById(R.id.edit_name);
        addFriendEmail = view.findViewById(R.id.edit_email);

        save = view.findViewById(R.id.btn_save);
        save.setOnClickListener(ClickOnsaveBtn);

    }


    ////click on save button
    View.OnClickListener ClickOnsaveBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isConnected) {
                Validations();
            } else {
                Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();

            }
        }
    };


    ////////////EditText validations
    public void Validations() {

        frndname = addFriendName.getText().toString().trim();
        friendEmail = addFriendEmail.getText().toString().trim();

        if (addFriendName.getText().toString().isEmpty()) {
            addFriendName.setError("Name Can'not be empty");
        } else if (addFriendEmail.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(addFriendEmail.getText().toString()).matches()) {
            addFriendEmail.setError("Enter Valid Email");
        } else {
            addFriendToChant();
        }


    }

    public void addFriendToChant() {

        displayProgressDialog();

        AddFriendServerObject addFriendServerObject = new AddFriendServerObject();
        addFriendServerObject.chantId = "chant_2853";
        addFriendServerObject.name = frndname;
        addFriendServerObject.email = friendEmail;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<AddFriendServerObject> addFriendServerObjectCall = api.addFriend(addFriendServerObject);
        addFriendServerObjectCall.enqueue(new Callback<AddFriendServerObject>() {
            @Override
            public void onResponse(Call<AddFriendServerObject> call, Response<AddFriendServerObject> response) {
                String addFriendResponse;
                if (response.body() != null) {
                    Log.e("addFriendResponse", " " + response.body().response);
                    addFriendResponse = response.body().response;
                    if (addFriendResponse.equals("3")) {
                        hideProgressDialog();
                    }

                }
            }

            @Override
            public void onFailure(Call<AddFriendServerObject> call, Throwable t) {
                Log.e("addFriendFailure", "Failed");
                hideProgressDialog();
            }
        });

    }


    @Override
    public void onNetworkConnectionChanged(boolean connect) {
        isConnected = connect;

        if (!isConnected) {
            Toast.makeText(getActivity(), "check internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), " Connected to internet ", Toast.LENGTH_SHORT).show();

        }
    }

    private void checkConnection() {
        isConnected = ConnectionReceiver.isConnected();
        if (!isConnected) {
            Toast.makeText(getActivity(), "check internet Connection", Toast.LENGTH_SHORT).show();
            Log.e("oncreate network status", " off");
        } else {
            Log.e("oncreate network status", " on");
        }
    }

    @Override
    public void onResume() {
        TestApplication.getInstance().setConnectionListener(this);
        super.onResume();
        // hideProgressDialog();
    }


    public void displayProgressDialog() {
        mProgress = new MaterialDialog.Builder(getActivity()).content("Loading").canceledOnTouchOutside(false).progress(true, 0).show();

    }

    private void hideProgressDialog() {

        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }
}
