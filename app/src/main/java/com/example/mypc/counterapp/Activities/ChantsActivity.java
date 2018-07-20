package com.example.mypc.counterapp.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Activities.Fragments.AddNewFriend;
import com.example.mypc.counterapp.Activities.Fragments.ChantsModel;
import com.example.mypc.counterapp.Activities.Fragments.Friends;
import com.example.mypc.counterapp.Activities.Fragments.JoinedFriends;
import com.example.mypc.counterapp.Controllers.Chantfriendscontroller;
import com.example.mypc.counterapp.Counter.CounterActivity;
import com.example.mypc.counterapp.Fonts.ButtonRegular;
import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.DeleteChantServerObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChantsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView chantname, chantdescr;
    String chant_name, chant_decr, chant_id, chant_privacy, chant_created_email, createdEmail, creted_By_Email;
    RadioGroup radioGroup;
    RecyclerView recyclerView_peoples;
    RadioButton radioButton_friends, radioButton_public;
    RelativeLayout relativeLayout_tab, relativeLayout_public, relative_peopled_Joined, relativeName, relative_count_public;
    ButtonRegular btn_submit;
    Toolbar toolbar;
    Friends_Holder holder;
    ArrayList<ChantsModel> people_joined;
    TextViewBold toolbar_text;
    MaterialDialog mprogress;
    ImageView toolbar_icon;
    RelativeLayout relative_toolbar;
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
        chantdescr.setMovementMethod(new ScrollingMovementMethod());
        toolbar_icon.setImageResource(R.drawable.ic_back_arrow);
        toolbar_text.setText("Friends Chants");
        relative_toolbar = findViewById(R.id.relative_back);
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
        editImage.setOnClickListener(EditChantClick);
        deleteImage = findViewById(R.id.image_delete);
        deleteImage.setOnClickListener(DeleteChantClick);

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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submiting();
            }
        });

        toolbar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        relative_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        chant_name = getIntent().getExtras().getString("chantname");
        chant_decr = getIntent().getExtras().getString("chant_dec");
        chant_privacy = getIntent().getExtras().getString("chant_privacy");
        chant_id = getIntent().getExtras().getString("chant_id");
        chant_created_email = getIntent().getExtras().getString("chant_created_email");
        creted_By_Email = getIntent().getExtras().getString("chant_created_email");
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        createdEmail = prefs.getString("email", "No name defined");
        chantname.setText(chant_name);
        chantdescr.setText(chant_decr);
        if (chant_privacy.equals("Friend")) {
            radioButton_friends.setChecked(true);
            Chantfriendscontroller.getintance().fillcontext(ChantsActivity.this);
            Chantfriendscontroller.getintance().fetch_chantFriends(chant_id);
        }
        Log.e("vhvfdbsjb", chant_name + chant_decr + chant_privacy + chant_id + createdEmail);
    }

    public void submiting() {
        Intent intent = new Intent(getApplicationContext(), CounterActivity.class);
        intent.putExtra("chant_id", chant_id);
        intent.putExtra("chant_created_email", chant_created_email);
        intent.putExtra("chantname", chant_name);
        intent.putExtra("chant_decrp", chant_decr);
        Log.e("chgejhbj", chant_id + chant_created_email + chant_name + chant_decr);
        startActivity(intent);
        finish();
    }


    View.OnClickListener DeleteChantClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Log.e("deleteEmails", " " + creted_By_Email + " " + createdEmail);
            if (createdEmail.equals(creted_By_Email)) {
                deleteChantDialog();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "chant created by others you can't delete", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }


        }
    };


    public void deleteChantDialog() {

        TextViewRegular yes, no, text_dialog;
        final Dialog dialog = new Dialog(ChantsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_logout_dialog);
        yes = dialog.findViewById(R.id.yes);
        no = dialog.findViewById(R.id.no);
        text_dialog = dialog.findViewById(R.id.dialog_text);
        text_dialog.setText("Are you sure you want to delete ?");

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("call", "dialog yes");

                deleteChant();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("call", "dialog no");
                dialog.dismiss();

            }
        });

        dialog.show();


    }

    public void display_Progress(String msg) {
        mprogress = new MaterialDialog.Builder(ChantsActivity.this).content(msg).canceledOnTouchOutside(false).progress(true, 0).show();
    }

    private void hide_ProgressDialog() {
        if (mprogress != null && mprogress.isShowing()) {
            mprogress.dismiss();
        }

    }

    /////server api for delete
    public void deleteChant() {

        display_Progress("Deleting...");
        DeleteChantServerObject deleteObj = new DeleteChantServerObject();
        deleteObj.chantId = chant_id;
        deleteObj.email = chant_created_email;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<DeleteChantServerObject> deletechant = api.deleteChant(deleteObj);
        deletechant.enqueue(new Callback<DeleteChantServerObject>() {
            @Override
            public void onResponse(Call<DeleteChantServerObject> call, Response<DeleteChantServerObject> response) {
                String deleteStatus;
                hide_ProgressDialog();
                if (response.body() != null) {

                    deleteStatus = response.body().response;
                    Log.e("deleteStatus", " " + deleteStatus);
                    if (deleteStatus.equals("3")) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteChantServerObject> call, Throwable t) {
                hide_ProgressDialog();
                Toast.makeText(ChantsActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });


    }


    View.OnClickListener EditChantClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), EditChantActivity.class);
            intent.putExtra("chant_name", chant_name);
            intent.putExtra("chant_dec", chant_decr);
            intent.putExtra("chant_id", chant_id);
            startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
