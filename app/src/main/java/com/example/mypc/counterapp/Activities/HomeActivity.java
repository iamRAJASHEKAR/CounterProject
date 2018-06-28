package com.example.mypc.counterapp.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.mypc.counterapp.Controllers.FetchPublicChantController;
import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Model.PublicList;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.LogoutServerObjects;
import com.example.mypc.counterapp.sessions.SessionsManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    String user_email, name, url;
    CircleImageView imageView_profile;
    ArrayList<PublicList> chantsArrayList;
    HomeAdapter homeAdapter;
    ImageButton floatingActionButton;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    MaterialDialog mProgress;
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
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Counter Application");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Counter application used for counting the chants");
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));
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
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        getuserData();
        set_profile(imageView_profile, user_name);
        // publicchant_data();

    }

    public void set_profile(CircleImageView imageView, TextView textView) {
        Log.e("logindude", name + "\n" + url + "\n" + user_email);
        Glide.with(getApplicationContext()).load(url).asBitmap().placeholder(R.drawable.ic_logo)
                .into(imageView);
        textView.setText(name);
    }

    public void display_data() {
        chantsArrayList = new ArrayList<>();
        chantsArrayList = FetchPublicChantController.getinstance().publicList;
        Log.e("kddbvjbs", String.valueOf(chantsArrayList.size()));
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(AddChantBtn);
        chantRecyclerview = findViewById(R.id.recyclerview_chant);
        homeAdapter = new HomeAdapter();
        chantRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        chantRecyclerview.setAdapter(homeAdapter);
        homeAdapter.notifyDataSetChanged();
    }


    View.OnClickListener AddChantBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), AddChantActivity.class));
        }
    };

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
            public void onClick(View view) {
                Log.e("call", "dialog yes");

                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {

                                if (sessionsManager.isLoggedIn()) {
                                    sessionsManager.setLogin(false);

                                }
                                logout();
                            }
                        });
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
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
            holder.chantTitle.setText(chantsArrayList.get(position).getChant_name());
            holder.chantText.setText(chantsArrayList.get(position).getChant_description());

            holder.textCreated.setText(chantsArrayList.get(position).created_by);
            holder.textVisibility.setText(chantsArrayList.get(position).privacy);
        }

        @Override
        public int getItemCount() {
            return chantsArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextViewBold chantTitle;
            TextViewRegular chantText;
            TextViewRegular textVisibility, textCreated;
            ImageView rightarrow;

            public ViewHolder(View itemView, Context applicationContext) {
                super(itemView);

                chantTitle = itemView.findViewById(R.id.chant_title);
                chantText = itemView.findViewById(R.id.chant_text);
                textVisibility = itemView.findViewById(R.id.text_visiible);
                textCreated = itemView.findViewById(R.id.text_created);
                rightarrow = itemView.findViewById(R.id.right_arrow);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String chant_name = chantsArrayList.get(getAdapterPosition()).chant_name;
                        String chant_dec = chantsArrayList.get(getAdapterPosition()).chant_description;
                        String chant_privacy = chantsArrayList.get(getAdapterPosition()).getPrivacy();
                        String chant_id = chantsArrayList.get(getAdapterPosition()).chant_id;
                        Log.e("pos", " " + chant_name + chant_dec);

                        if (chant_privacy.equals("Public")) {
                            Intent intent = new Intent(getApplicationContext(), ChantJoin.class);
                            intent.putExtra("chant_name", chant_name);
                            intent.putExtra("chant_dec", chant_dec);
                            intent.putExtra("chant_privacy", chant_privacy);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), ChantsActivity.class);
                            intent.putExtra("chant_name", chant_name);
                            intent.putExtra("chant_dec", chant_dec);
                            intent.putExtra("chant_privacy", chant_privacy);
                            intent.putExtra("chant_id", chant_id);
                            startActivity(intent);
                        }
                        //  startActivity(new Intent(getApplicationContext(), ChantsActivity.class));
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


    public void logout() {
        displayProgressDialog("Logging out...");
        @SuppressLint("HardwareIds") String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        LogoutServerObjects logoutServerObjects = new LogoutServerObjects();
        logoutServerObjects.device_id = device_id;
        logoutServerObjects.user_email = user_email;
        Log.e("logout_data", device_id + user_email);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
//hello dude
        ServerApiInterface apiInterface = retrofit.create(ServerApiInterface.class);
        Call<LogoutServerObjects> logout_call = apiInterface.logout_user(logoutServerObjects);
        logout_call.enqueue(new Callback<LogoutServerObjects>() {
            @Override
            public void onResponse(Call<LogoutServerObjects> call, Response<LogoutServerObjects> response) {

                hideProgressDialog();
                if (response.body() != null) {
                    Log.e("logout", response.body().getResponse());
                    if (response.body().getResponse().equals("3")) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to logout", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogoutServerObjects> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(HomeActivity.this, "Failed to logout", Toast.LENGTH_SHORT).show();
                Log.e("logoot_failure", t.getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        FetchPublicChantController.getinstance().publicchant_data(user_email);
        displayProgressDialog("Fetching data...");
        super.onStart();
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void getuserData() {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        name = prefs.getString("name", "No name defined");
        url = prefs.getString("photo", "No name defined");
        user_email = prefs.getString("email", "No name defined");
    }

    private void hideProgressDialog() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    public void displayProgressDialog(String msg) {
        mProgress = new MaterialDialog.Builder(HomeActivity.this).content(msg).canceledOnTouchOutside(false).progress(true, 0).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginActivity.MessageEvent event) {
        Log.e("filesevent", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("refreshchant")) {
            hideProgressDialog();
            display_data();
        }
    }
}
