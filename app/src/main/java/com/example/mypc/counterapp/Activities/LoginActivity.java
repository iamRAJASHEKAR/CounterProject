package com.example.mypc.counterapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.sessions.SessionsManager;
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

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{

    ButtonBold fbLogin, googleLogin;
    private static final int RC_SIGN_IN = 234;
    SharedPreferences.Editor editor;
    SessionsManager sessionsManager;
    //Tag for the logs optional
    public static final String MY_PREFS_NAME = "login";

    int PRIVATE_MODE = 0;
    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient googleApiClient;
    //And also a Firebase Auth object
    //FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        sessionsManager = new SessionsManager(getApplicationContext());
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        if (sessionsManager.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);

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

        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Toast.makeText(LoginActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                //authenticating with firebasel
                // firebaseAuthWithGoogle(account);
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
    View.OnClickListener ClickOnGoogle = new View.OnClickListener()
    {
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

}
