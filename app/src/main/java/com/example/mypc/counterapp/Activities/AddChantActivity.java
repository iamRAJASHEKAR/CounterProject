package com.example.mypc.counterapp.Activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Activities.Fragments.ChantsModel;
import com.example.mypc.counterapp.Database.DatabaseManager;
import com.example.mypc.counterapp.Database.Userdata;
import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Fonts.EditTextRegular;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Model.Contact;
import com.example.mypc.counterapp.Network.ConnectionReceiver;
import com.example.mypc.counterapp.Network.TestApplication;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.AddChantServerObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddChantActivity extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {

    android.support.v7.widget.Toolbar addChantToolbar;
    ImageView toolbarIconback;
    TextView toolText;
    Button saveBtn;
    boolean privacy;
    Userdata userdata;
    EditTextRegular editChantname, editchantText;
    RadioGroup radioGroup;
    RadioButton radioPublic, radioFriends, rdb_private;
    RecyclerView addchantRecyclerview;
    AddChantAdapter addChantAdapter;
    RelativeLayout relative_back;
    ArrayList<ChantsModel> chantsModelArrayList;
    RelativeLayout nameEmailLayout;
    String radioButtonText, chantname, chantDescription, createdBy, user_name, createdEmail, timestamp, religion;
    public boolean isConnected;
    ArrayList<String> mSelectedFriendsEmail = new ArrayList<String>();
    ArrayList<String> mSelectedFriendsName = new ArrayList<>();
    ArrayList<Contact> friendsArrayList;
    MaterialDialog mProgress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chant);
        checkConnection();
        loadContactsOnSeparateThread();
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        privacy = false;
        createdBy = prefs.getString("name", "No name defined");
        createdEmail = prefs.getString("email", "No name defined");
        user_name = prefs.getString("name", "No name defined");
        long unixdata = System.currentTimeMillis();
        timestamp = String.valueOf(unixdata);
        Log.e("nametimestamp", " " + createdBy + " " + timestamp);
        init();
        getuserData();
    }

    public void init() {

        addChantToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(addChantToolbar);
        relative_back = findViewById(R.id.relative_back);

        toolbarIconback = findViewById(R.id.toolabar_icon);
        toolbarIconback.setImageResource(R.drawable.ic_back_arrow);
        toolbarIconback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolText = findViewById(R.id.toolabr_title);
        toolText.setText(R.string.txt_add_chant);
        editChantname = findViewById(R.id.edit_chantName);
        editchantText = findViewById(R.id.edit_chant);
        editchantText.setOnTouchListener(touchListener);
        radioGroup = findViewById(R.id.radioGroup);
        rdb_private = findViewById(R.id.rdb_private);
        radioPublic = findViewById(R.id.rdbPublic);
        radioFriends = findViewById(R.id.rdbFriends);
        saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(SaveBtnClick);
        nameEmailLayout = findViewById(R.id.rl_name_email);
        addchantRecyclerview = findViewById(R.id.addchant_recyclerview);
        addchantRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        addChantAdapter = new AddChantAdapter();
        addchantRecyclerview.setAdapter(addChantAdapter);

        radioPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonText = "Public";
                privacy = true;
                if (radioPublic.isChecked()) {
                    nameEmailLayout.setVisibility(View.INVISIBLE);
                    addchantRecyclerview.setVisibility(View.INVISIBLE);
                }
            }
        });
        radioFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privacy = true;
                radioButtonText = "Friend";
                if (radioFriends.isChecked())
                    addchantRecyclerview.setVisibility(View.VISIBLE);
                nameEmailLayout.setVisibility(View.VISIBLE);
            }
        });
        rdb_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdb_private.isChecked()) {
                    nameEmailLayout.setVisibility(View.INVISIBLE);
                    addchantRecyclerview.setVisibility(View.INVISIBLE);
                }
            }
        });

        relative_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    View.OnClickListener SaveBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            chantname = editChantname.getText().toString();
            chantDescription = editchantText.getText().toString();
            if (isConnected) {
                validations();
            } else {
                Toast.makeText(AddChantActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        }
    };

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        public boolean onTouch(final View v, final MotionEvent motionEvent) {
            if (v.getId() == R.id.edit_chant) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
            }
            return false;
        }
    };

    /////////Edit Text Validations
    public void validations() {
        if (editChantname.length() == 0) {
            editChantname.setError("Enter chant name");
        } else if (editchantText.length() == 0) {
            editchantText.setError("Enter the chant");
        } else if (rdb_private.isChecked()) {
            add_data();
            Toast toast = Toast.makeText(this, "Private Chant Added Successfully ", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            finish();
        } else if (privacy == false) {
            Toast toast = Toast.makeText(this, "Check chant privacy", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (radioFriends.isChecked() && friendsArrayList.size() < 1) {
            Toast toast = Toast.makeText(this, "Friends are Empty", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else {
            addChants();
        }
    }

    public void add_data() {
        userdata = new Userdata();
        userdata.setFirstname(chantname);
        userdata.setLastname(chantDescription);
        userdata.setEmail(createdEmail);
        userdata.setUsername(timestamp);
        userdata.setId(DatabaseManager.getInstance().users.size() + 1);
        DatabaseManager.getInstance().add_user(userdata);
    }

    public void loadContactsOnSeparateThread() {
        // run on separate thread
        HandlerThread handlerThread = new HandlerThread("fetchContacts");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                ExecutorService taskExecutor = Executors.newFixedThreadPool(1);
                try {
                    Runnable backgroundTask = new Runnable() {
                        @Override
                        public void run() {
                            // ContactsDataController.getInstance().getPhoneDetailsFromDeviceContacts();
                            getPhoneDetailsFromDeviceContacts();
                        }
                    };
                    taskExecutor.submit(backgroundTask);
                    taskExecutor.shutdown();
                    taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {

                }
            }
        });

    }

    ///getting the phone contacts
    public void getPhoneDetailsFromDeviceContacts() {

        chantsModelArrayList = new ArrayList<>();
        friendsArrayList = new ArrayList<>();

        Context context = getApplicationContext();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);

                while (cur1.moveToNext()) {
                    //to get the contact names
                    String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    Log.e("Name:", "" + name);
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    Log.e("Email", "" + email);
                    String image_uri = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                    Integer hasPhone = cur1.getInt(cur1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (!email.isEmpty() && !name.isEmpty()) {
                        ChantsModel contactModel = new ChantsModel();
                        contactModel.setUser(email);
                        contactModel.setName(name);
                        chantsModelArrayList.add(contactModel);
                        Log.e("contactsArrary", "call" + chantsModelArrayList.size());
                    }
                }
                cur1.close();
            }
        }
    }

    ///////Adding the chants and send to server
    public void addChants() {
        displayProgressDialog();
        AddChantServerObject addChantObj = new AddChantServerObject();
        addChantObj.chantName = chantname.trim();
        addChantObj.chantDescription = chantDescription.trim();
        addChantObj.createdBy = createdBy.trim();
        addChantObj.timestamp = timestamp.trim();
        addChantObj.created_email = createdEmail.trim();
        addChantObj.religion = religion.trim();
        if (radioButtonText.equals("Public")) {
            addChantObj.privacy = radioButtonText.trim();
        } else if (radioButtonText.equals("Friend")) {
            addChantObj.privacy = radioButtonText.trim();
            addChantObj.contacts = friendsArrayList;
        }

        Log.e("addchnatffff", " " + addChantObj);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<AddChantServerObject> addChants = api.addChants(addChantObj);
        addChants.enqueue(new Callback<AddChantServerObject>() {
            @Override
            public void onResponse(Call<AddChantServerObject> call, Response<AddChantServerObject> response) {
                if (response.body() != null) {
                    Log.e("addchantStatuscode", " " + response.body().response);
                    String addchantStatuscode = response.body().response;
                    if (addchantStatuscode.equals("3")) {
                        hideProgressDialog();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddChantServerObject> call, Throwable t) {
                Log.e("addchantfailure", " failed");
                hideProgressDialog();
            }
        });

    }

    @Override
    public void onNetworkConnectionChanged(boolean connect) {
        isConnected = connect;

        if (!isConnected) {
            Toast.makeText(this, "check internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, " Connected to internet ", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onResume() {
        TestApplication.getInstance().setConnectionListener(this);
        super.onResume();
    }


    class AddChantAdapter extends RecyclerView.Adapter<AddChantAdapter.ViewHolder> {


        public AddChantAdapter() {

            mSelectedFriendsEmail = new ArrayList<String>();
            mSelectedFriendsName = new ArrayList<>();
        }

        @NonNull
        @Override
        public AddChantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_add_chant_friendslist, null);
            final AddChantActivity.AddChantAdapter.ViewHolder viewHolder = new AddChantActivity.AddChantAdapter.ViewHolder(v, getApplicationContext());
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final AddChantAdapter.ViewHolder holder, final int position) {
            final Contact contact = new Contact();
            holder.name.setText(chantsModelArrayList.get(position).getName());
            holder.email.setText(chantsModelArrayList.get(position).getUser());
            final ChantsModel chantsModel = chantsModelArrayList.get(position);
            holder.btninvite.setBackgroundResource(chantsModel.isSelected() ? R.color.colorOrange : R.color.colorLightGray);
            holder.btninvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chantsModel.setSelected(!chantsModel.isSelected());
                    holder.btninvite.setBackgroundResource(chantsModel.isSelected() ? R.color.colorOrange : R.color.colorLightGray);
                    Log.e("SelectedStatus", " " + chantsModel.isSelected() + " " + holder.getAdapterPosition());
                    if (chantsModel.isSelected()) {
                        contact.setMail(chantsModelArrayList.get(holder.getAdapterPosition()).getUser());
                        contact.setName(chantsModelArrayList.get(holder.getAdapterPosition()).getName());
                        friendsArrayList.add(contact);
                        Log.e("friendsArrayList", " " + " " + friendsArrayList.size());
                    } else {
                        Log.e("arraypos", "call" + position);

                        if (friendsArrayList.contains(contact)) {
                            int index = friendsArrayList.indexOf(contact);
                            Log.e("index", "call" + index);
                            friendsArrayList.remove(index);
                        }
                        Log.e("unselectedFreinds", " " + mSelectedFriendsEmail + " " + mSelectedFriendsEmail.size() + " " + mSelectedFriendsName + " " + mSelectedFriendsName.size());
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return chantsModelArrayList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextViewRegular name, email;
            ButtonBold btninvite;

            public ViewHolder(View itemView, Context applicationContext) {
                super(itemView);
                name = itemView.findViewById(R.id.text_friend_name);
                email = itemView.findViewById(R.id.text_friend_mail);
                btninvite = itemView.findViewById(R.id.btn_invite);
            }
        }
    }

    public void displayProgressDialog() {
        mProgress = new MaterialDialog.Builder(AddChantActivity.this).content("Loading").canceledOnTouchOutside(false).progress(true, 0).show();
    }

    private void hideProgressDialog() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        hideProgressDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        hideProgressDialog();
    }

    public void getuserData() {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        religion = prefs.getString("religion", "No name defined");
        Log.e("addchantreligion", religion);
    }

}
