package com.example.mypc.counterapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Fonts.EditTextRegular;
import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.R;

public class HelpUsActivity extends AppCompatActivity
{

    Toolbar toolbar;
    ImageView toolbaricon;
    TextViewBold toolbar_text;
    EditTextRegular editName,editEmail,editPhone,editHelpMsg;
    ButtonBold btnSend;
    String name,email,phoneno,helpmsg;
    ScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpus);
        init();

    }

    public void init()
    {
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbaricon = findViewById(R.id.toolabar_icon);
        toolbaricon.setImageResource(R.drawable.ic_back_arrow);

        toolbaricon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar_text =findViewById(R.id.toolabr_title);
        toolbar_text.setText(R.string.help_text);

        scrollView = findViewById(R.id.scrollView);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        editHelpMsg =findViewById(R.id.edit_help);
        editHelpMsg.setOnTouchListener(touchListener);

        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(BtnSendClick);

    }

    View.OnTouchListener touchListener = new View.OnTouchListener(){
        public boolean onTouch(final View v, final MotionEvent motionEvent){
            if(v.getId() == R.id.edit_help){
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
            }
            return false;
        }
    };

    View.OnClickListener BtnSendClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            validate();
        }
    };


    /////////editText Validations
    public void validate()
    {
        name = editName.getText().toString();
        email = editEmail.getText().toString();
        phoneno = editPhone.getText().toString();
        helpmsg = editHelpMsg.getText().toString();

        if(name.isEmpty())
        {
            editName.setError("Name Can'not be empty");
        }
        else if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editEmail.setError("Enter Valid Email");
        }

        else if(phoneno.isEmpty() )
        {
             editPhone.setError("Enter Valid Phone number");
        }

        else if(helpmsg.isEmpty())
        {
            editHelpMsg.setError("Enter the message");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
