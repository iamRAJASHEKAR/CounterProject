package com.example.mypc.counterapp.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mypc.counterapp.Activities.Fragments.AddNewFriend;
import com.example.mypc.counterapp.Activities.Fragments.Friends;
import com.example.mypc.counterapp.Activities.Fragments.JoinedFriends;
import com.example.mypc.counterapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChantsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    RadioGroup radioGroup;
    RadioButton radioButton, radioButton_public;
    RelativeLayout relativeLayout;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chants);
        radioGroup = findViewById(R.id.radioprofile);
        radioButton = findViewById(R.id.radioFriends);
        radioButton_public = findViewById(R.id.radiopublic);
        relativeLayout = findViewById(R.id.relative_appbar);
        button = findViewById(R.id.count_public);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton.isChecked()) {
                    button.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    //    Toast.makeText(ChantsActivity.this, "hello", Toast.LENGTH_SHORT).show();

                }
            }
        });

        radioButton_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton_public.isChecked()) {

                    button.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    //    Toast.makeText(ChantsActivity.this, "hello", Toast.LENGTH_SHORT).show();

                }
            }
        });

        // int pos = radioGroup.getCheckedRadioButtonId();

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

}
