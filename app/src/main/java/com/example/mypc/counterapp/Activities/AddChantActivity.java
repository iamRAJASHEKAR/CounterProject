package com.example.mypc.counterapp.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mypc.counterapp.Activities.Fragments.ChantsModel;
import com.example.mypc.counterapp.Fonts.EditTextRegular;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.R;

import java.util.ArrayList;

public class AddChantActivity extends AppCompatActivity
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chant);
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

        radioGroup = findViewById(R.id.radioGroup);
        radioPublic = findViewById(R.id.rdbPublic);
        radioFriends = findViewById(R.id.rdbFriends);

        saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(SaveBtnClick);

        setData();

        addchantRecyclerview = findViewById(R.id.addchant_recyclerview);
        addchantRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        addChantAdapter = new AddChantAdapter();
        addchantRecyclerview.setAdapter(addChantAdapter);

        radioPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
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
                if(radioFriends.isChecked())
                addchantRecyclerview.setVisibility(View.VISIBLE);
            }
        });
    }



    View.OnClickListener SaveBtnClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
               validations();
               finish();

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
    }

    public void setData()
    {
        for(int i = 0; i<6;i++)
        {
            chantsModelArrayList.add(new ChantsModel("Vedas","contactvedas@gmail.com"));

        }
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
