package com.example.mypc.counterapp.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Model.Chants;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.sessions.SessionsManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    DrawerLayout drawerLayout;
    public static boolean check = true;
    public static boolean checked = false;
    RecyclerView chantRecyclerview;
    Toolbar toolbar;
    SessionsManager sessionsManager;
    TextView user_name;
    TextViewBold toolbar_text;
    ImageView toolabr_image;
    CircleImageView imageView_profile;
    ArrayList<Chants> chantsArrayList;
    HomeAdapter homeAdapter;
    com.github.clans.fab.FloatingActionButton floatingActionButton;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_home);
        sessionsManager = new SessionsManager(this);
        navigationView = findViewById(R.id.navigation_view);
        imageView_profile = navigationView.getHeaderView(0).findViewById(R.id.image_profile);
        user_name = navigationView.getHeaderView(0).findViewById(R.id.text_email);
        //imageView_profile.setImageResource(R.drawable.ic_star);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolabr_image = findViewById(R.id.toolabar_icon);
        toolabr_image.setImageResource(R.drawable.ic_sidemenu);
        toolbar_text = findViewById(R.id.toolabr_title);
        toolbar_text.setText(R.string.avialble_chants);

        drawerLayout = findViewById(R.id.drawerLayout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                // item.setCheckable(true);
                Log.e("item", " " + item.getTitle());
                switch (item.getItemId()) {
                    case R.id.rate_us:
                        Log.e("rateus", "rateus");
                        break;
                    case R.id.share:
                        Log.e("share", "share");
                        break;

                    case R.id.help:
                        Log.e("help", "help");
                        startActivity(new Intent(getApplicationContext(), HelpUsActivity.class));
                        break;

                    case R.id.logout:
                        Log.e("logout", "logout");
                        logoutDialog();
                        break;

                }
                return true;
            }
        });

        //click on navigationview
        toolabr_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, HomeActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        init();

        set_profile(imageView_profile, user_name);


    }


    public void set_profile(CircleImageView imageView, TextView textView) {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);

        String name = prefs.getString("name", "No name defined");
        String url = prefs.getString("photo", "No name defined");

        Log.e("logindude", name + "\n" + url);

        Glide.with(getApplicationContext()).load(url).asBitmap().into(imageView);
        textView.setText(name);
    }

    public void init() {

        chantsArrayList = new ArrayList<>();
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(AddChantBtn);

        setData();
        chantRecyclerview = findViewById(R.id.recyclerview_chant);
        homeAdapter = new HomeAdapter();
        chantRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        chantRecyclerview.setAdapter(homeAdapter);
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

   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.e("itemid"," "+item.getItemId());
        switch (item.getItemId())
        {
            case R.id.rate_us:
                Log.e("rateus","rateus");
                break;
            case R.id.share:
                Log.e("share","share");
                break;

            case R.id.help:
                Log.e("help","help");
                break;

            case R.id.logout:
                Log.e("logout","logout");
                break;

        }

        return true;
    }
*/

    ////////Logout alert Dailog

    public void logoutDialog() {

        TextViewRegular yes, no;
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_logout_dialog);
        yes = dialog.findViewById(R.id.yes);
        no = dialog.findViewById(R.id.no);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.e("call", "dialog yes");

                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                if (sessionsManager.isLoggedIn()) {
                                    sessionsManager.setLogin(false);
                                }
                                //      Toast.makeText(HomeActivity.this, "Logged out", Toast.LENGTH_SHORT).show();

                            }
                        });
                drawerLayout.closeDrawers();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("call", "dialog no");
                dialog.dismiss();
                drawerLayout.closeDrawers();
            }
        });

        dialog.show();


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


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
            return chantsArrayList.size();
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
                        Log.e("pos", " " + getLayoutPosition());
                        startActivity(new Intent(getApplicationContext(), ChantsActivity.class));
                    }
                });

            }


        }
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
