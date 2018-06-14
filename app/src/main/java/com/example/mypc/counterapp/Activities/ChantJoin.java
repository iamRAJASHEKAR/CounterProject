package com.example.mypc.counterapp.Activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypc.counterapp.R;

public class ChantJoin extends AppCompatActivity {

    TextView toolbar_head;
    ImageView tool_icon;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chant_join);
        toolbar_head = findViewById(R.id.toolabr_title);
        tool_icon = findViewById(R.id.toolabar_icon);
        toolbar_head.setText("Public Chants");
        tool_icon.setBackground(getResources().getDrawable(R.drawable.ic_delete));

    }
}
