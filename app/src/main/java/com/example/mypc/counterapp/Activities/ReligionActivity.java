package com.example.mypc.counterapp.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Model.Religion;
import com.example.mypc.counterapp.R;

import java.util.ArrayList;

public class ReligionActivity extends AppCompatActivity {

    Button noReligionFound;
    RecyclerView recyclerViewReligion;
    ArrayList<Religion> religionarraylist;
    String[] religions = {"Hindu", "Muslim", "Christian"};
    ReligionAdapter religionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_religion);
        init();
    }

    public void init() {
        religionarraylist = new ArrayList<>();
        setdata();
        recyclerViewReligion = findViewById(R.id.recyclerview_religion);
        noReligionFound = findViewById(R.id.btn_no_religion);
        noReligionFound.setOnClickListener(NoReligionFound);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewReligion.setLayoutManager(layoutManager);
        //  recyclerViewReligion.setLayoutManager(new LinearLayoutManager(this));
        religionAdapter = new ReligionAdapter();
        recyclerViewReligion.setAdapter(religionAdapter);

    }

    public void setdata() {
        for (int i = 0; i < religions.length; i++) {
            religionarraylist.add(new Religion(religions[i]));
        }
    }


    View.OnClickListener NoReligionFound = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            showReligiondialog();
        }
    };


    ////Dialog for entering the religion
    public void showReligiondialog() {
        TextViewRegular save, cancel;
        final Dialog dialog = new Dialog(ReligionActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_religion_dialog);
        save = dialog.findViewById(R.id.save);
        cancel = dialog.findViewById(R.id.cancel);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("call", "dialog yes");
                // startActivity(new Intent(getApplicationContext(),LoginActivity.class));


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("call", "dialog no");
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    class ReligionAdapter extends RecyclerView.Adapter<ReligionAdapter.ViewHolder> {


        @Override
        public ReligionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_religion_list, null);
            final ViewHolder viewHolder = new ViewHolder(v, getApplicationContext());
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ReligionAdapter.ViewHolder holder, int position) {
            holder.btn_religion.setText(religionarraylist.get(position).getReligionName());

        }

        @Override
        public int getItemCount() {
            return religionarraylist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ButtonBold btn_religion;

            public ViewHolder(View itemView, Context applicationContext) {
                super(itemView);
                btn_religion = itemView.findViewById(R.id.btnReligio);
                btn_religion.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                   //     finish();
                    }
                });

            }
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}
