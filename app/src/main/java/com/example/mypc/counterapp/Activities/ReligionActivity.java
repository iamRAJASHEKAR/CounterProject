package com.example.mypc.counterapp.Activities;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Model.Religion;
import com.example.mypc.counterapp.R;

import java.util.ArrayList;

public class ReligionActivity extends AppCompatActivity
{

   Button noReligionFound;
   RecyclerView recyclerViewReligion;
   ArrayList<Religion> religionarraylist;
   String[] religions = {"Hindu","Muslim","Christian"};
   ReligionAdapter religionAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_religion);
        init();
    }

    public void init()
    {
        religionarraylist = new ArrayList<>();
        setdata();
        recyclerViewReligion = findViewById(R.id.recyclerview_religion);
        noReligionFound = findViewById(R.id.btn_no_religion);
        noReligionFound.setOnClickListener(NoReligionFound);
        recyclerViewReligion.setLayoutManager(new LinearLayoutManager(this));
        religionAdapter = new ReligionAdapter();
        recyclerViewReligion.setAdapter(religionAdapter);

    }

    public void setdata()
    {
        for(int i = 0;i<religions.length;i++)
        {
            religionarraylist.add(new Religion(religions[i]));
        }
    }



    View.OnClickListener NoReligionFound = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {

                 showReligiondialog();
        }
    };


    ////Dialog for entering the religion
  public void showReligiondialog()
  {
      final AlertDialog.Builder  dialog =  new AlertDialog.Builder(ReligionActivity.this);
      LayoutInflater inflater = this.getLayoutInflater();
      View dilogview =  inflater.inflate(R.layout.activity_religion_dialog,null);
      dialog.setView(dilogview);
      final EditText religion_edit = dilogview.findViewById(R.id.edit_enter_religion);

      dialog.setTitle("Add Religion");
      dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i)
          {
                 String addreligionText = religion_edit.getText().toString();
                 Log.e("addReligion"," "+addreligionText);
                 religionarraylist.add(new Religion(addreligionText));
          }
      });

      dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {

          }
      });

      AlertDialog aler = dialog.create();
      aler.show();

  }


  class ReligionAdapter extends RecyclerView.Adapter<ReligionAdapter.ViewHolder>
  {



      @Override
      public ReligionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_religion_list, null);
          final ViewHolder viewHolder = new ViewHolder(v, getApplicationContext());
          return viewHolder;
      }

      @Override
      public void onBindViewHolder(@NonNull ReligionAdapter.ViewHolder holder, int position)
      {
         holder.btn_religion.setText(religionarraylist.get(position).getReligionName());

      }

      @Override
      public int getItemCount()
      {
          return religionarraylist.size();
      }

      public class ViewHolder extends RecyclerView.ViewHolder
      {
         ButtonBold btn_religion;

          public ViewHolder(View itemView, Context applicationContext)
          {
              super(itemView);
              btn_religion =itemView.findViewById(R.id.btnReligio);
              btn_religion.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view)
                  {
                      startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                  }
              });

          }
      }
  }

}
