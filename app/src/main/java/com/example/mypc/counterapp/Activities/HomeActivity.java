package com.example.mypc.counterapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Model.Chants;
import com.example.mypc.counterapp.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements SideMenuFragment.FragmentDrawerListner {
    SideMenuFragment sideMenuFragment;
    DrawerLayout drawerLayout;

    public static boolean check = true;
    public static boolean checked = false;
    RecyclerView chantRecyclerview;
    Toolbar toolbar;
    TextViewBold toolbar_text;
    ImageView toolabr_image;
    ArrayList<Chants> chantsArrayList;
    HomeAdapter homeAdapter;
    com.github.clans.fab.FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_home);
        init();
        intializeDrawer();

    }


    public void init() {
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolabr_image = findViewById(R.id.toolabar_icon);
        toolabr_image.setImageResource(R.drawable.ic_sidemenu);
        toolbar_text = findViewById(R.id.toolabr_title);
        toolbar_text.setText(R.string.avialble_chants);
        chantsArrayList = new ArrayList<>();
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(AddChantBtn);

        setData();
        chantRecyclerview = findViewById(R.id.recyclerview_chant);
        homeAdapter = new HomeAdapter();
        chantRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        chantRecyclerview.setAdapter(homeAdapter);
    }


    public void intializeDrawer() {
        drawerLayout = findViewById(R.id.drawerLayout);
        sideMenuFragment = (SideMenuFragment) getSupportFragmentManager().findFragmentById(R.id.fragment1);
        sideMenuFragment.setup(R.id.fragment1, drawerLayout, toolbar);
        sideMenuFragment.setFragmentDrawerListner(this);
        toolabr_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void setData() {
        for (int i = 0; i < 10; i++) {
            chantsArrayList.add(new Chants("The Gayathri Mantra", " Oṃ bhūr bhuvaḥ svaḥ tát savitúr váreṇyaṃ bhárgo devásya dhīmahi dhíyo yó naḥ pracodáyāt"));
        }
    }

    View.OnClickListener AddChantBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), AddChantActivity.class));
        }
    };


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

        @NonNull
        @Override
        public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.acitvity_home_list, null);
            final HomeActivity.HomeAdapter.ViewHolder viewHolder = new HomeActivity.HomeAdapter.ViewHolder(v, getApplicationContext());
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
            holder.chantTitle.setText(chantsArrayList.get(position).getChantTitle());
            holder.chantText.setText(chantsArrayList.get(position).getChantText());

        }

        @Override
        public int getItemCount() {
            return    chantsArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextViewBold chantTitle;
            TextViewRegular chantText;

            public ViewHolder(View itemView, Context applicationContext) {
                super(itemView);

                chantTitle = itemView.findViewById(R.id.chant_title);
                chantText = itemView.findViewById(R.id.chant_text);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), ChantsActivity.class));
                    }
                });

            }
        }
    }
}
