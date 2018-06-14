package com.example.mypc.counterapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.R;

public class LoginActivity extends AppCompatActivity {

    ButtonBold fbLogin,googleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }



    public void init()
    {
        fbLogin = findViewById(R.id.btn_fb);
        fbLogin.setOnClickListener(ClickFbLogin);

        googleLogin = findViewById(R.id.btn_google);
        googleLogin.setOnClickListener(ClickOnGoogle);

    }



   //Click on facebook button
    View.OnClickListener ClickFbLogin = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Log.e("login","Login with facebook");
            startActivity(new Intent(LoginActivity.this,ReligionActivity.class));
        }
    };

//no need to worry about this dude

    //Click on google button
    View.OnClickListener ClickOnGoogle = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Log.e("login","Login with google");
            startActivity(new Intent(LoginActivity.this,ReligionActivity.class));
        }
    };

}
