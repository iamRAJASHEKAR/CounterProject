package com.example.mypc.counterapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypc.counterapp.Counter.CounterActivity;
import com.example.mypc.counterapp.Fonts.ButtonRegular;
import com.example.mypc.counterapp.R;

public class ChantJoin extends AppCompatActivity {

    TextView toolbar_head, chant_name, chant_desc;
    ImageView tool_icon;
    ButtonRegular btnJoin;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chant_join);

        toolbar_head = findViewById(R.id.toolabr_title);
        tool_icon = findViewById(R.id.toolabar_icon);
        chant_name = findViewById(R.id.ChantName);
        chant_desc = findViewById(R.id.chantDescription);
        toolbar_head.setText("Public Chants");
        tool_icon.setBackground(getResources().getDrawable(R.drawable.ic_back_arrow));

        tool_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnJoin = findViewById(R.id.submitButton);
        btnJoin.setOnClickListener(ClickBtnJoin);
        String chantname = getIntent().getExtras().getString("chant_name");
        String chantdecr = getIntent().getExtras().getString("chant_dec");
        String chantprivacy = getIntent().getExtras().getString("chant_privacy");
        chant_name.setText(chantname);
        chant_desc.setText(chantdecr);
    }

    View.OnClickListener ClickBtnJoin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), CounterActivity.class));
        }
    };
}
