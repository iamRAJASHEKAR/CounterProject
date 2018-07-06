package com.example.mypc.counterapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypc.counterapp.Counter.CounterActivity;
import com.example.mypc.counterapp.Fonts.ButtonRegular;
import com.example.mypc.counterapp.R;

public class ChantJoin extends AppCompatActivity {

    TextView toolbar_head, chantname, chantdesc;
    ImageView tool_icon;
    ButtonRegular btnJoin;
    String chant_countid, chant_created, chant_name, chant_desc;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chant_join);

        toolbar_head = findViewById(R.id.toolabr_title);
        tool_icon = findViewById(R.id.toolabar_icon);
        chantname = findViewById(R.id.ChantName);
        chantdesc = findViewById(R.id.chantDescription);
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
        getintentdata();
    }

    View.OnClickListener ClickBtnJoin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            submiting();
        }
    };

    public void getintentdata() {
        chant_countid = getIntent().getExtras().getString("chant_id");
        chant_created = getIntent().getExtras().getString("chant_created");
        chant_name = getIntent().getExtras().getString("chantname");
        chant_desc = getIntent().getExtras().getString("chant_dec");
        chantname.setText(chant_name);
        chantdesc.setText(chant_desc);
        Log.e("joinerrdata", chant_countid + chant_created + chant_name + chant_desc);
    }


    public void submiting() {

        Intent intent = new Intent(getApplicationContext(), CounterActivity.class);
        intent.putExtra("chant_id", chant_countid);
        intent.putExtra("chant_created", chant_created);
        intent.putExtra("chantname", chant_name);
        intent.putExtra("chant_decrp", chant_desc);
        Log.e("chgejhbj", chant_countid + chant_created + chant_name + chant_desc);
        startActivity(intent);
    }
}
