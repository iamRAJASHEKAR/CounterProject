package com.example.mypc.counterapp.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Network.ConnectionReceiver;
import com.example.mypc.counterapp.Network.TestApplication;
import com.example.mypc.counterapp.PushNotification.Constants;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.CounterController;
import com.example.mypc.counterapp.ServerObject.UserLoginObjects;
import com.example.mypc.counterapp.sessions.SessionsManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ConnectionReceiver.ConnectionReceiverListener {

    String user_email, user_name;

    ImageButton fbLogin, googleLogin;
    private static final int RC_SIGN_IN = 234;
    SharedPreferences.Editor editor;
    public int status_code;
    SessionsManager sessionsManager;
    //Tag for the logs optional
    MaterialDialog mProgress, progress_login;
    public static final String MY_PREFS_NAME = "login";
    public boolean isConnected;
    int PRIVATE_MODE = 0;
    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient googleApiClient;
    //And also a Firebase Auth object
    //FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    SharedPreferences sharedPreferences;
    //SharedPreferences.Editor sharedPreferencesEditor;
    String socialmediaLoginEmail;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fetchData();
        init();
        //login("raju@gmail.com", "Mr.android");
        checkConnection();
        //  mProgressDialog = new MaterialDialog(this);
        sessionsManager = new SessionsManager(getApplicationContext());
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        if (sessionsManager.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, LoginActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    signIn();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Check network", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });

        fb_login();
        fbLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (isConnected)
                {
                    // mProgress.show();
                    displayProgressDialog("Loading");
                    if (v == fbLogin) {
                        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                        LoginManager.getInstance().registerCallback(callbackManager, callback);
                    }
                } else {
                    hideProgressDialog();
                    //  mProgress.dismiss();
                    Toast.makeText(LoginActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    // new AlertShowingDialog(LoginActivity.this, "No Internet connection");

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("notify", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            // showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }


    private void hideProgressDialog() {

        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    private void hide_login_ProgressDialog() {

        if (progress_login != null && progress_login.isShowing()) {

            progress_login.dismiss();
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Log.e("displaying", "display name: " + account.getDisplayName());
            String personName = account.getDisplayName();
            String email = account.getEmail();
            user_email = email;
            user_name = personName;
            //  login(user_email, user_name);
            editor.putString("name", account.getDisplayName());
            editor.putString("email", String.valueOf(account.getEmail()));
            editor.putString("photo", String.valueOf(account.getPhotoUrl()));
            editor.commit();
            storeLogin(2);
            login(user_email, user_name);
            hideProgressDialog();
        } else {
            hideProgressDialog();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }

    }

    private void signIn() {

        displayProgressDialog("Loading");
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void fb_login() {
        //   fbLogin = findViewById(R.id.btn_fb);
        sharedPreferences = getApplicationContext().getSharedPreferences("socialMediaLoginDetails", Context.MODE_PRIVATE);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                Log.e("sdf", "asdfas" + newToken);
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.e("FBStatus", "onSuccess Called");

            System.out.println("onSuccess");

            String accessToken = loginResult.getAccessToken()
                    .getToken();
            Log.i("accessToken", accessToken);
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object,
                                                GraphResponse response) {

                            Log.i("LoginActivity", response.toString());
                            try {
                                String id = object.getString("id");
                                String name = object.getString("name");
                                String email = object.getString("email");
                                //  String gender = object.getString("gender");
                                if (email != null) {
                                    // mProgress.dismiss();
                                    //sharedPreferencesEditor = sharedPreferences.edit();
                                    //sharedPreferencesEditor.putString("name", name);
                                    Log.e("username", "" + name);
                                    //   sharedPreferencesEditor.putString("gender", gender);
                                    String imageURLString = "http://graph.facebook.com/" + id + "/picture?type=large";
                                    //sharedPreferencesEditor.putString("picture", imageURLString);
                                    //sharedPreferencesEditor.putString("Type", "Facebook");
                                    //  Log.e("gender", "" + gender);
                                    Log.e("gender", "" + imageURLString);
                                    // Log.e("emailfacebook", "call" + name + "" + email + "" + gender);
                                    socialmediaLoginEmail = email;


                                    editor.putString("name", name);
                                    editor.putString("email", email);
                                    editor.putString("photo", imageURLString);
                                    editor.commit();
                                    user_email = email;
                                    user_name = name;
                                    // login(user_email, user_name);


                                    //sharedPreferencesEditor.commit();

                                    LoginManager.getInstance().logOut();
                                    hideProgressDialog();

                                    login(user_email, user_name);

                                    storeLogin(1);
                                } else {
                                    Log.e("emailnull", "call");
                                    LoginManager.getInstance().logOut();
                                    hideProgressDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields",
                    "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            hideProgressDialog();
            Log.e("FBStatus", "OnCancel Called");
        }

        @Override
        public void onError(FacebookException e) {
            Log.e("FBStatus", "OnCancel Called" + e);
            mProgress.dismiss();
        }
    };


    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

    public void init() {
        fbLogin = findViewById(R.id.btn_fb);
        fbLogin.setOnClickListener(ClickFbLogin);

        googleLogin = findViewById(R.id.btn_google);
        googleLogin.setOnClickListener(ClickOnGoogle);

    }

    public void display_login_Progress(String msg) {
        progress_login = new MaterialDialog.Builder(LoginActivity.this).content(msg).canceledOnTouchOutside(false).progress(true, 0).show();

    }

    //Click on facebook button
    View.OnClickListener ClickFbLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.e("login", "Login with facebook");
            startActivity(new Intent(LoginActivity.this, ReligionActivity.class));
        }
    };

//no need to worry about this dude

    //Click on google button
    View.OnClickListener ClickOnGoogle = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.e("login", "Login with google");
            startActivity(new Intent(LoginActivity.this, ReligionActivity.class));
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void displayProgressDialog(String msg) {
        mProgress = new MaterialDialog.Builder(LoginActivity.this).content(msg).canceledOnTouchOutside(false).progress(true, 0).show();

    }

    @Override
    protected void onResume() {
        TestApplication.getInstance().setConnectionListener(this);
        super.onResume();
    }

    @Override
    public void onNetworkConnectionChanged(boolean connect) {
        isConnected = connect;
        if (!isConnected) {
            Toast.makeText(this, "check internet Connection", Toast.LENGTH_SHORT).show();
            //  Snackbar.make(findViewById(R.id.relative_login), "check internet Connection", Snackbar.LENGTH_SHORT).show();
        } else {
            // Snackbar.make(findViewById(R.id.relative_login), " internet Connection on", Snackbar.LENGTH_INDEFINITE).show();
            //  Log.e("network status", " On");
        }
    }

    private void checkConnection() {
        isConnected = ConnectionReceiver.isConnected();
        if (!isConnected) {
            Toast.makeText(this, "check internet Connection", Toast.LENGTH_SHORT).show();
            Log.e("oncreate network status", " off");
        } else {
            Log.e("oncreate network status", " on");
        }
    }

    public void login(String email, String name) {
        display_login_Progress("Registering...");
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
        String token = prefs.getString("regId", "No name defined");
        @SuppressLint("HardwareIds") String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        UserLoginObjects userLogin = new UserLoginObjects();
        userLogin.email = email;
        userLogin.name = name;
        userLogin.device_id = device_id;
        userLogin.device_token = token;

        Log.e("tokengenbe", email + "\n" + name + "\n" + device_id + "\n" + token);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<UserLoginObjects> login_user = api.login_user(userLogin);
        login_user.enqueue(new Callback<UserLoginObjects>() {
            @Override
            public void onResponse(Call<UserLoginObjects> call, Response<UserLoginObjects> response) {

                hide_login_ProgressDialog();
                if (response.body() != null) {
                    status_code = Integer.parseInt(response.body().getResponse());
                    Log.e("lloginuser", " " + status_code);
                    if (status_code == 3) {
                        sessionsManager.setLogin(true);
                        Intent intent = new Intent(getApplicationContext(), ReligionActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserLoginObjects> call, Throwable t) {
                Log.e("fhfabhfbd", t.getMessage());

                hide_login_ProgressDialog();
                Toast.makeText(LoginActivity.this, "Failed to register try again", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void storeLogin(int token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("logindata", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("loginstack", token);
        editor.commit();

    }


    public class Login_user extends AsyncTask<Void, Void, Void> {
        String email, name;

        public Login_user(final String email, final String name) {
            this.email = email;
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            display_login_Progress("Registering...");
            //    displayProgressDialog("Registering please wait ");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            login(email, name);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            hide_login_ProgressDialog();
            action();
            super.onPostExecute(aVoid);
        }
    }

    public void action() {
        Log.e("slowversion", " " + status_code);

    }

    ///fetchdata
    public void fetchData()
    {
        CounterController.getInstance().fetchReligions();
    }
}
