package com.example.mypc.counterapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mypc.counterapp.Activities.Fragments.AddNewFriend;
import com.example.mypc.counterapp.Activities.Fragments.ChantsModel;
import com.example.mypc.counterapp.Activities.Fragments.Friends;
import com.example.mypc.counterapp.Activities.Fragments.JoinedFriends;
import com.example.mypc.counterapp.Controllers.Chantfriendscontroller;
import com.example.mypc.counterapp.Counter.CounterActivity;
import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Fonts.ButtonRegular;
import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChantsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView chantname, chantdescr;
    String chant_name, chant_decr, chant_id, chant_privacy, chant_created;
    RadioGroup radioGroup;
    RecyclerView recyclerView_peoples;
    RadioButton radioButton_friends, radioButton_public;
    RelativeLayout relativeLayout_tab, relativeLayout_public, relative_peopled_Joined, relativeName, relative_count_public;
    ButtonRegular btn_submit;
    Toolbar toolbar;
    Friends_Holder holder;
    ArrayList<ChantsModel> people_joined;
    TextViewBold toolbar_text;
    ImageView toolbar_icon;
    ImageView editImage, deleteImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chants);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        people_joined = new ArrayList<>();
        //recyclerView_peoples = findViewById(R.id.recycler_people_joined);
        toolbar_text = findViewById(R.id.toolabr_title);
        toolbar_icon = findViewById(R.id.toolabar_icon);
        chantname = findViewById(R.id.the_mantra);
        btn_submit = findViewById(R.id.button_submit);
        chantdescr = findViewById(R.id.the_mantra_lines);
        toolbar_icon.setImageResource(R.drawable.ic_back_arrow);
        toolbar_text.setText("Chants");

        toolbar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        radioGroup = findViewById(R.id.radioprofile);
        radioButton_friends = findViewById(R.id.radioFriends);
        radioButton_public = findViewById(R.id.radiopublic);
        radioButton_public = findViewById(R.id.radiopublic);
        relativeLayout_tab = findViewById(R.id.relative_appbar);
        // relativeLayout_public = findViewById(R.id.relative_public);
        //  relative_peopled_Joined = findViewById(R.id.realtive_people_joined);
        //  relative_count_public = findViewById(R.id.rl_count_public);
        //  relativeName = findViewById(R.id.liner_name);
        editImage = findViewById(R.id.image_edit);
        deleteImage = findViewById(R.id.image_delete);

        //  button = findViewById(R.id.count_public);
       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CounterActivity.class));
            }
        });*/
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        getIntentData();

        if (radioButton_friends.isChecked()) {
            relativeLayout_tab.setVisibility(View.VISIBLE);/*
            relativeLayout_public.setVisibility(View.GONE);
            relative_count_public.setVisibility(View.GONE);*/

        }

        /*radioButton_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton_friends.isChecked()) {

                    relativeLayout_tab.setVisibility(View.VISIBLE);
                    relativeLayout_public.setVisibility(View.GONE);
                    relative_count_public.setVisibility(View.GONE);
                }
            }
        });*/
/*

        radioButton_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton_public.isChecked()) {
                    relativeLayout_tab.setVisibility(View.GONE);
                    relativeLayout_public.setVisibility(View.VISIBLE);
                    recyclerView_peoples.setVisibility(View.VISIBLE);
                    relative_peopled_Joined.setVisibility(View.VISIBLE);
                    relativeName.setVisibility(View.VISIBLE);
                    relative_count_public.setVisibility(View.VISIBLE);
                }
            }
        });
*/

        holder = new Friends_Holder();/*
        recyclerView_peoples.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView_peoples.setAdapter(holder);*/
        // int pos = radioGroup.getCheckedRadioButtonId();
        Chantfriendscontroller.getintance().fillcontext(getApplicationContext());

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submiting();
            }
        });

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new JoinedFriends(), "Friends Joined");
        adapter.addFragment(new Friends(), "All Friends");
        adapter.addFragment(new AddNewFriend(), "Add New Friends");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private class Friends_Holder extends RecyclerView.Adapter<Friends_Holder.ViewHolder> {


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chant_list, null);
            final ViewHolder viewHolder = new ViewHolder(view, getApplicationContext());
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.name.setText(people_joined.get(position).getName());
            holder.user.setText(people_joined.get(position).getUser());

        }

        @Override
        public int getItemCount() {
            return people_joined.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, user;

            public ViewHolder(View itemView, Context context) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                user = itemView.findViewById(R.id.user);

            }
        }
    }


    public void getIntentData() {
        chant_name = getIntent().getExtras().getString("chant_name");
        chant_decr = getIntent().getExtras().getString("chant_dec");
        chant_privacy = getIntent().getExtras().getString("chant_privacy");
        chant_id = getIntent().getExtras().getString("chant_id");
        chant_created = getIntent().getExtras().getString("chant_created");
        chantname.setText(chant_name);
        chantdescr.setText(chant_decr);
        if (chant_privacy.equals("Friend")) {
            radioButton_friends.setChecked(true);
            Chantfriendscontroller.getintance().fetch_chantFriends(chant_id);
        }
        Log.e("vhvfdbsjb", chant_name + chant_decr + chant_privacy + chant_id);
    }

    public void submiting() {

        Intent intent = new Intent(getApplicationContext(), CounterActivity.class);
        intent.putExtra("chant_id", chant_id);
        intent.putExtra("chant_created", chant_created);
        startActivity(intent);
    }


}
