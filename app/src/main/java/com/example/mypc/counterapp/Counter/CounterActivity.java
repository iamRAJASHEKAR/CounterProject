package com.example.mypc.counterapp.Counter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.mypc.counterapp.R;

public class CounterActivity extends AppCompatActivity {
    TextView textView, text_megacount;
    int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        textView = findViewById(R.id.text_screenmode);
        text_megacount = findViewById(R.id.text_mega_count);


        final String mega_count = "<u>MEGA COUNT</u>";
        text_megacount.setText(Html.fromHtml(mega_count));
        final String htmlString = "<u>ON-SCREEN MODE</u>";

        final String oddscreen = "<u>OFF-SCREEN MODE</u>";

        textView.setText(Html.fromHtml(htmlString));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 0) {

                    textView.setText(Html.fromHtml(oddscreen));
                    textView.setTextColor(getResources().getColor(R.color.colorGray));
                    state = 1;
                } else if (state == 1) {

                    textView.setText(Html.fromHtml(htmlString));

                    textView.setTextColor(getResources().getColor(R.color.colorOrange));
                    state = 0;
                }

            }
        });
    }
}
