package com.example.mypc.counterapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
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
import com.example.mypc.counterapp.Activities.Fragments.AddNewFriend;
import com.example.mypc.counterapp.Activities.Fragments.Friends;
import com.example.mypc.counterapp.Activities.Fragments.FriendsFragment;
import com.example.mypc.counterapp.Activities.Fragments.JoinedFriends;
import com.example.mypc.counterapp.Activities.Fragments.PrivateFragment;
import com.example.mypc.counterapp.Activities.Fragments.PublicFragment;
import com.example.mypc.counterapp.Controllers.FetchPublicChantController;
import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Model.PublicList;
import com.example.mypc.counterapp.Network.ConnectionReceiver;
import com.example.mypc.counterapp.Network.TestApplication;
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
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ConnectionReceiver.ConnectionReceiverListener {

    private TabLayout tabLayout;
    DrawerLayout drawerLayout;
    public static boolean check = true;
    public static boolean checked = false;
    //RecyclerView chantRecyclerview;
    Toolbar toolbar;
    SessionsManager sessionsManager;
    public boolean isConnected;
    TextView user_name;
    TextViewBold toolbar_text;
    ImageView toolabr_image;
    String user_email, name, url;
    CircleImageView imageView_profile;
    ArrayList<PublicList> chantsArrayList;
    HomeAdapter homeAdapter;
    private ViewPager viewPager;
    ImageButton floatingActionButton;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    public static MaterialDialog mProgress;
    GoogleApiClient googleApiClient;

    public static String chantId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_home);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
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
        checkConnection();
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
        requestforPhoneContacts();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(AddChantBtn);

    }

    public void set_profile(CircleImageView imageView, TextView textView) {
        Log.e("logindude", name + "\n" + url + "\n" + user_email);
        Glide.with(getApplicationContext()).load(url).asBitmap().placeholder(R.drawable.ic_logo)
                .into(imageView);
        textView.setText(name);
    }

 /*   public void display_data() {
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
    }*/


    View.OnClickListener AddChantBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), AddChantActivity.class));
        }
    };

    public void tryagain() {
        TextViewRegular yes, no, title;
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_logout_dialog);
        yes = dialog.findViewById(R.id.yes);
        no = dialog.findViewById(R.id.no);
        no.setVisibility(View.GONE);
        yes.setText("Try Again");
        title = dialog.findViewById(R.id.dialog_text);
        title.setText("Failed to fetch chants");
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (isConnected) {
                    displayProgressDialog("Loading...");
                    FetchPublicChantController.getinstance().publicchant_data(user_email);
                } else {
                    tryagain();
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


    public void logoutDialog()
    {
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
                if (isConnected) {

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

    @Override
    public void onNetworkConnectionChanged(boolean connect) {
        isConnected = connect;
        if (!isConnected) {
            Toast.makeText(this, "check internet Connection...", Toast.LENGTH_SHORT).show();
            //  Snackbar.make(findViewById(R.id.relative_login), "check internet Connection", Snackbar.LENGTH_SHORT).show();
        } else {
            // Snackbar.make(findViewById(R.id.relative_login), " internet Connection on", Snackbar.LENGTH_INDEFINITE).show();
            //  Log.e("network status", " On");
        }
    }

    private void checkConnection() {
        isConnected = ConnectionReceiver.isConnected();
        if (!isConnected) {
            Toast.makeText(this, "check internet Connection..", Toast.LENGTH_SHORT).show();
            Log.e("oncreate network status", " off");
        } else {
            Log.e("oncreate network status", " on");
        }
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
                        String chant_created = chantsArrayList.get(getAdapterPosition()).getCreated_by();
                        String chant_creted_email = chantsArrayList.get(getAdapterPosition()).created_email;
                        chantId = chant_id;
                        Log.e("pos", " " + chant_name + chant_dec + chant_id);

                        if (chant_privacy.equals("Public")) {
                            Intent intent = new Intent(getApplicationContext(), ChantJoin.class);
                            intent.putExtra("chantname", chant_name);
                            intent.putExtra("chant_dec", chant_dec);
                            intent.putExtra("chant_privacy", chant_privacy);
                            intent.putExtra("chant_id", chant_id);
                            intent.putExtra("chant_created", chant_creted_email);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), ChantsActivity.class);
                            intent.putExtra("chantname", chant_name);
                            intent.putExtra("chant_dec", chant_dec);
                            intent.putExtra("chant_privacy", chant_privacy);
                            intent.putExtra("chant_id", chant_id);
                            intent.putExtra("chant_created", chant_creted_email);
                            intent.putExtra("chant_created_email", chant_creted_email);
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
        if (isConnected) {
            displayProgressDialog("Loading...");
            FetchPublicChantController.getinstance().publicchant_data(user_email);
        } else {
            tryagain();

        }

        super.onStart();
    }

    @Override
    protected void onResume() {
        TestApplication.getInstance().setConnectionListener(this);
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void getuserData()
    {
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

        if (mProgress.isShowing()) {
            mProgress.dismiss();
        }
        mProgress.show();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginActivity.MessageEvent event)
    {
       /* Log.e("filesevent", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("refreshchant"))
        {
            hideProgressDialog();
            //display_data();
        } else if (resultData.equals("error")) {
            tryagain();
            //    displayProgressDialog("Loading Chants...");

        }*/
    }

    public void requestforPhoneContacts() {
        Permissions.check(HomeActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, "We need Email id's of contacts for inviting the chant..", new Permissions.Options()
                        .setRationaleDialogTitle("Info"),
                new PermissionHandler() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        Toast.makeText(getApplicationContext(), "Permissions are necessary,Please grant the permission", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                        startActivity(intent);
                        Log.e("denied Permissions", Arrays.toString(deniedPermissions.toArray()));

                    }

                    @Override
                    public boolean onBlocked(Context context, ArrayList<String> blockedList) {
                        Toast.makeText(context, "read contacts blocked:\n" + Arrays.toString(blockedList.toArray()),
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public void onJustBlocked(Context context, ArrayList<String> justBlockedList, ArrayList<String> deniedPermissions) {
                        Toast.makeText(context, "Camera+Storage just blocked:\n" + Arrays.toString(deniedPermissions.toArray()),
                                Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FriendsFragment(), "Friends");
        adapter.addFragment(new PublicFragment(), "Public");
        adapter.addFragment(new PrivateFragment(), "Private");
        viewPager.setAdapter(adapter);
    }


}
