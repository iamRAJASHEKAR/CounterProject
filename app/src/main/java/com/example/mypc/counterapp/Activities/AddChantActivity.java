package com.example.mypc.counterapp.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.mypc.counterapp.R;

public class AddChantActivity extends AppCompatActivity
{

   android.support.v7.widget.Toolbar addChantToolbar;
   ImageView toolbarIconback;
   TextView toolText;
   Button saveBtn;
   EditText editChantname,editchantText;
   RadioGroup radioGroup;
   RadioButton radioPublic,radioFriends;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chant);
        init();
    }

    public void init()
    {
        addChantToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(addChantToolbar);
        toolbarIconback = findViewById(R.id.toolabar_icon);
        toolbarIconback.setImageResource(R.mipmap.ic_launcher);
        toolText = findViewById(R.id.toolabr_title);
        toolText.setText(R.string.txt_add_chant);
        editChantname = findViewById(R.id.edit_chantName);
        editchantText = findViewById(R.id.edit_chant);
        radioGroup = findViewById(R.id.radioGroup);
        radioPublic = findViewById(R.id.rdbPublic);
        radioFriends = findViewById(R.id.rdbFriends);
        saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(SaveBtnClick);
    }

    View.OnClickListener SaveBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {


        }
    };

    /////////Edit Text Validations
    public void validations()
    {
        if(editChantname.length()==0)
        {

        }
    }
}
