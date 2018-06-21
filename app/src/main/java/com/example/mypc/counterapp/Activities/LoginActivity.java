package com.example.mypc.counterapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.R;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    ButtonBold fbLogin, googleLogin;
    private static final int RC_SIGN_IN = 234;
    SharedPreferences.Editor editor;
    SessionsManager sessionsManager;
    //Tag for the logs optional
    ProgressDialog mProgress;
    public static final String MY_PREFS_NAME = "login";

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


        init();
        sessionsManager = new SessionsManager(getApplicationContext());
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        if (sessionsManager.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), ReligionActivity.class);
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

                signIn();

            }
        });

        fb_login();
        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConn()) {
                    // mProgress.show();
                    if (v == fbLogin) {
                        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                        LoginManager.getInstance().registerCallback(callbackManager, callback);
                    }
                } else {
                    //  mProgress.dismiss();
                    Toast.makeText(LoginActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    // new AlertShowingDialog(LoginActivity.this, "No Internet connection");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "hello" + account.getDisplayName() + account.getPhotoUrl(), Toast.LENGTH_SHORT).show();

                sessionsManager.setLogin(true);
                editor.putString("name", account.getDisplayName());
                editor.putString("photo", String.valueOf(account.getPhotoUrl()));
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), ReligionActivity.class);
                startActivity(intent);
                finish();
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }

    }

    private void signIn() {
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

        mProgress = new ProgressDialog(LoginActivity.this);
        mProgress.setMessage("Loading...");
        mProgress.setProgress(Color.BLACK);
        mProgress.setCancelable(true);
        mProgress.setCanceledOnTouchOutside(false);
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


                                    sessionsManager.setLogin(true);
                                    editor.putString("name", name);
                                    editor.putString("photo", imageURLString);
                                    editor.commit();


                                    //sharedPreferencesEditor.commit();

                                    LoginManager.getInstance().logOut();

                                    mProgress.dismiss();
                                    startActivity(new Intent(getApplicationContext(), ReligionActivity.class));

                                    finish();
                                } else {
                                    Log.e("emailnull", "call");
                                    LoginManager.getInstance().logOut();
                                    mProgress.dismiss();

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
            mProgress.dismiss();

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
        public void onClick(View view)
        {
            Log.e("login", "Login with google");
            startActivity(new Intent(LoginActivity.this, ReligionActivity.class));
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
      /*  finish();
        super.onBackPressed();
   */
        moveTaskToBack(true);
    }
}
