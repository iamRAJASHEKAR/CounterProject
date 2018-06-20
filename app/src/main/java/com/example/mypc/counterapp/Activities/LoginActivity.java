package com.example.mypc.counterapp.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.R;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button fbLogin;
    ButtonBold googleLogin;
    private static final int PERMISSION_REQUEST_CODE = 1;
    ProgressDialog mProgress;
    String name, gendar,emailString, age;
    private static final String EMAIL = "email";

    AccessToken accessToken;
    //ProgressDialog progress;
    private Dialog logindilog, facebookalert, responsealert,failurealert;
    List<String> permissionNeeds = Arrays.asList("user_photos", "email",
            "user_birthday", "public_profile");
    Button fb;
    private CallbackManager callbackManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    String socialmediaLoginEmail;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        fb = (Button) findViewById(R.id.btn_fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConn()) {
                   // mProgress.show();
                    if (v == fb) {
                        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                        LoginManager.getInstance().registerCallback(callbackManager,callback );
                    }
                } else {
                  //  mProgress.dismiss();
                    Toast.makeText(LoginActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                   // new AlertShowingDialog(LoginActivity.this, "No Internet connection");
                }
            }
        });

        }



    public void init()
    {
        googleLogin = findViewById(R.id.btn_google);
        //googleLogin.setOnClickListener(ClickOnGoogle);

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
                                 //String gender = object.getString("gender");
                                if (email != null) {
                                   // mProgress.dismiss();
                                    sharedPreferencesEditor = sharedPreferences.edit();
                                    sharedPreferencesEditor.putString("name", name);
                                    //sharedPreferencesEditor.putString("gender",gender);
                                    Log.e("username", "" + name);
                                    //   sharedPreferencesEditor.putString("gender", gender);
                                    String imageURLString = "http://graph.facebook.com/" + id + "/picture?type=large";
                                    sharedPreferencesEditor.putString("picture", imageURLString);
                                    sharedPreferencesEditor.putString("Type", "Facebook");
                                    //  Log.e("gender", "" + gender);
                                    Log.e("gender", "" + imageURLString);
                                    // Log.e("emailfacebook", "call" + name + "" + email + "" + gender);
                                    socialmediaLoginEmail = email;
                                    sharedPreferencesEditor.commit();
                                    LoginManager.getInstance().logOut();
                                    mProgress.dismiss();
                                    startActivity(new Intent(getApplicationContext(), ReligionActivity.class));

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.e("requestcode", "" + requestCode + " " + resultCode);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        /*if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            Log.e("statuscode", "calldd" + statusCode);

            handleSignInResult(result);
        } else {
        }*/
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    public static void showNetworkAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Network Alert");
        builder.setMessage("Ple" +
                "" +
                "ase check your network connection and try again");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }



    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }


}
