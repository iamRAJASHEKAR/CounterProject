package com.example.mypc.counterapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypc.counterapp.Activities.Fragments.ChantsModel;
import com.example.mypc.counterapp.Fonts.EditTextRegular;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Network.ConnectionReceiver;
import com.example.mypc.counterapp.Network.TestApplication;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.AddChantServerObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddChantActivity extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener
{

   android.support.v7.widget.Toolbar addChantToolbar;
   ImageView toolbarIconback;
   TextView toolText;
   Button saveBtn;
   EditTextRegular editChantname,editchantText;
   RadioGroup radioGroup;
   RadioButton radioPublic,radioFriends;
   RecyclerView addchantRecyclerview;
   AddChantAdapter addChantAdapter;
   ArrayList<ChantsModel> chantsModelArrayList;
   RelativeLayout nameEmailLayout;
   String radioButtonText,chantname,chantDescription,createdBy,createdEmail,timestamp;
    public boolean isConnected;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chant);
        checkConnection();
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);

        createdBy = prefs.getString("name", "No name defined");

        long unixTime = System.currentTimeMillis() / 1000L;
        timestamp = String.valueOf(unixTime);
        Log.e("name timestamp"," "+createdBy+" "+timestamp);

        chantsModelArrayList = new ArrayList<>();
        init();
    }

    public void init()
    {
        addChantToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(addChantToolbar);

        toolbarIconback = findViewById(R.id.toolabar_icon);
        toolbarIconback.setImageResource(R.drawable.ic_back_arrow);
        toolbarIconback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolText = findViewById(R.id.toolabr_title);
        toolText.setText(R.string.txt_add_chant);

        editChantname = findViewById(R.id.edit_chantName);
        editchantText = findViewById(R.id.edit_chant);
        editchantText.setOnTouchListener(touchListener);

        radioGroup = findViewById(R.id.radioGroup);
        radioPublic = findViewById(R.id.rdbPublic);
        radioFriends = findViewById(R.id.rdbFriends);

        saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(SaveBtnClick);

        nameEmailLayout = findViewById(R.id.rl_name_email);

        setData();

        addchantRecyclerview = findViewById(R.id.addchant_recyclerview);
        addchantRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        addChantAdapter = new AddChantAdapter();
        addchantRecyclerview.setAdapter(addChantAdapter);

        radioPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               radioButtonText = "Public";
                if(radioPublic.isChecked())
                {
                    addchantRecyclerview.setVisibility(View.INVISIBLE);
                }

            }
        });

        radioFriends.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               radioButtonText = "Friend";
                if(radioFriends.isChecked())
                addchantRecyclerview.setVisibility(View.VISIBLE);
                nameEmailLayout.setVisibility(View.VISIBLE);
            }
        });
    }



    View.OnClickListener SaveBtnClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
              chantname = editChantname.getText().toString();
              chantDescription = editchantText.getText().toString();
              if(isConnected)
              {
                 validations();
              }
              else
              {
                  Toast.makeText(AddChantActivity.this, "No Internet", Toast.LENGTH_SHORT).show();

              }


        }
    };

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        public boolean onTouch(final View v, final MotionEvent motionEvent) {
            if (v.getId() == R.id.edit_help) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
            }
            return false;
        }
    };


    /////////Edit Text Validations
    public void validations()
    {
        if(editChantname.length()==0)
        {
            editChantname.setError("Enter chant name");
        }

        else if(editchantText.length() == 0)
        {
            editchantText.setError("Enter the chant");
        }

        else
        {
            addChants();
        }



    }

    public void setData()
    {
        for(int i = 0; i<6;i++)
        {
            chantsModelArrayList.add(new ChantsModel("Vedas","contactvedas@gmail.com"));

        }
    }

    ///////Adding the chants and send to server
    public void addChants()
    {
       AddChantServerObject addChantObj = new AddChantServerObject();
       addChantObj.chantName = chantname.trim();
       addChantObj.chantDescription = chantDescription.trim();
       addChantObj.createdBy = createdBy.trim();
       addChantObj.privacy = radioButtonText.trim();
       addChantObj.timestamp = timestamp.trim();
       addChantObj.created_email = "vedas@gmail.com".trim();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<AddChantServerObject> addChants = api.addChants(addChantObj);
        addChants.enqueue(new Callback<AddChantServerObject>() {
            @Override
            public void onResponse(Call<AddChantServerObject> call, Response<AddChantServerObject> response)
            {
                   if(response.body()!=null)
                   {
                       Log.e("addchantStatuscode"," "+response.body().response);
                   }
            }

            @Override
            public void onFailure(Call<AddChantServerObject> call, Throwable t) {

            }
        });

    }

    @Override
    public void onNetworkConnectionChanged(boolean connect)
    {
        isConnected = connect;

        if (!isConnected)
        {
            Toast.makeText(this, "check internet Connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, " Connected to internet ", Toast.LENGTH_SHORT).show();

        }
    }

    private void checkConnection()
    {
        isConnected = ConnectionReceiver.isConnected();
        if (!isConnected) {
            Toast.makeText(this, "check internet Connection", Toast.LENGTH_SHORT).show();
            Log.e("oncreate network status", " off");
        } else {
            Log.e("oncreate network status", " on");
        }
    }

    @Override
    protected void onResume()
    {
        TestApplication.getInstance().setConnectionListener(this);
        super.onResume();
    }



    class AddChantAdapter extends RecyclerView.Adapter<AddChantAdapter.ViewHolder>
    {

        @NonNull
        @Override
        public AddChantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_add_chant_friendslist, null);
            final AddChantActivity.AddChantAdapter.ViewHolder viewHolder = new AddChantActivity.AddChantAdapter.ViewHolder(v, getApplicationContext());
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AddChantAdapter.ViewHolder holder, int position)
        {
            holder.name.setText(chantsModelArrayList.get(position).getName());
            holder.email.setText(chantsModelArrayList.get(position).getUser());

        }

        @Override
        public int getItemCount() {
            return chantsModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
           TextViewRegular name,email;

            public ViewHolder(View itemView, Context applicationContext)
            {
                super(itemView);
                name = itemView.findViewById(R.id.text_friend_name);
                email = itemView.findViewById(R.id.text_friend_mail);
            }
        }
    }
}
