package com.example.mypc.counterapp.Activities.Fragments;


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
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Activities.AddChantActivity;
import com.example.mypc.counterapp.Activities.HomeActivity;
import com.example.mypc.counterapp.Activities.LoginActivity;
import com.example.mypc.counterapp.Controllers.Chantfriendscontroller;
import com.example.mypc.counterapp.Counter.CounterActivity;
import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Model.FriendsList;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.InviteServerobject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Friends extends Fragment {
    RecyclerView recyclerView;
    ArrayList<FriendsList> userarray;
    ChantsAdpater homeAdapter;
    ArrayList<FriendsList> contactsArraylist;
    ArrayList<FriendsList> allFriendsArraylist;
    MaterialDialog mProgress;
    ArrayList<FriendsList> inviteFriends;
    ArrayList<FriendsList> active;
    ArrayList<FriendsList> intersection;
    String chantid;
    String friendName, friendemail;


    public Friends() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        userarray = new ArrayList<>();
        inviteFriends = new ArrayList<>();
        loadContactsOnSeparateThread();
        recyclerView = view.findViewById(R.id.recycler);
        //display_inactiive_friends();

        return view;
    }

    public void display_progress(String msg) {
        mProgress = new MaterialDialog.Builder(getActivity()).content(msg).canceledOnTouchOutside(false).progress(true, 0).show();

    }

    public void hide_progress() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }

    }

    ////////click on submit_count btn

    private class ChantsAdpater extends RecyclerView.Adapter<ViewHolder> {


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list, null);
            final ViewHolder viewHolder = new ViewHolder(view, getActivity());
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.name.setText(allFriendsArraylist.get(position).getName());
            holder.user.setText(allFriendsArraylist.get(position).getEmail());
            holder.invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.invite.setBackgroundResource(R.color.colorOrange);
                    friendName = allFriendsArraylist.get(position).getName();
                    friendemail = allFriendsArraylist.get(position).getEmail();
                    inviteFriend();
                }
            });
        }

        @Override
        public int getItemCount() {
            if (allFriendsArraylist != null) {
                return allFriendsArraylist.size();
            } else {
                return 0;
            }
        }

    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextViewRegular name, user;
        ButtonBold invite;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            user = itemView.findViewById(R.id.user);
            invite = itemView.findViewById(R.id.invitation);

        }
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();

    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginActivity.MessageEvent event) {
        Log.e("active", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("active_friends")) {
            display_inactiive_friends();
            //checker();
        }
    }

    public void checker() {
        userarray = new ArrayList<>();
        allFriendsArraylist = new ArrayList<>();
//a
        userarray = Chantfriendscontroller.getintance().active_friends;
//b
        allFriendsArraylist.addAll(contactsArraylist);

        //un
        active = new ArrayList<>(userarray);

        active.addAll(allFriendsArraylist);

        intersection = new ArrayList<>(userarray);
        intersection.retainAll(allFriendsArraylist);


        ArrayList<FriendsList> sumetric = new ArrayList<>(active);
        sumetric.removeAll(intersection);
        allFriendsArraylist.clear();
        Log.e("hjhshjsv", String.valueOf(active.size() + "\n" + allFriendsArraylist.size()));

        allFriendsArraylist = sumetric;
        Log.e("lasterdata", String.valueOf(active.size() + "\n" + allFriendsArraylist.size()));

        set_data();
        /*       Log.e("hjhshjsv", String.valueOf(active.size() + "\n" + allFriendsArraylist.size()));

        HashSet hs = new HashSet();
        hs.addAll(allFriendsArraylist);
        allFriendsArraylist.clear();

        Log.e("checkclear", String.valueOf(allFriendsArraylist.size()));


        allFriendsArraylist.addAll(hs);


        Log.e("hashmap", String.valueOf(allFriendsArraylist.size()));
        set_data();*/
    }

    private void display_inactiive_friends() {
        active = new ArrayList<>();
        active = Chantfriendscontroller.getintance().active_friends;
        userarray = new ArrayList<>();
        allFriendsArraylist = new ArrayList<>();
        userarray = Chantfriendscontroller.getintance().inactive_friends;
        Log.e("jshdbvjsvb", String.valueOf(userarray.size()));
        if (userarray.size() > 0 && contactsArraylist != null) {
            Log.e("hellwith", "takeaction");
            mergeContactInactivearrays();
        } else if (active != null && contactsArraylist != null) {
            Log.e("hellwith", "takeaction");
            allFriendsArraylist.addAll(contactsArraylist);


            HashSet hs = new HashSet();
            hs.addAll(allFriendsArraylist);
            allFriendsArraylist.clear();
            Log.e("checkclear", String.valueOf(allFriendsArraylist.size()));
            allFriendsArraylist.addAll(hs);

            /*    for (FriendsList friendsList : active) {
                if (!allFriendsArraylist.contains(friendsList)) {
                    allFriendsArraylist.add(friendsList);
                }
            }
        */
            set_data();
        } else {
            if (contactsArraylist != null && contactsArraylist.size() > 0) {
                Log.e("kokokokoko", String.valueOf(contactsArraylist.size()));
                contact_onlyphone();
            }
        }

    } //////////merging contacts array list and inactive arraylist

    public void contact_onlyphone() {
        allFriendsArraylist.addAll(contactsArraylist);
        Log.e("onlyserverdata", String.valueOf(allFriendsArraylist.size()));


        HashSet hs = new HashSet();
        hs.addAll(allFriendsArraylist);
        allFriendsArraylist.clear();
        Log.e("checkclear", String.valueOf(allFriendsArraylist.size()));
        allFriendsArraylist.addAll(hs);


/*

        for (FriendsList friendsList : userarray) {
            if (allFriendsArraylist.contains(friendsList)) {
            } else {
                Log.e("elsepart", "execute");
                allFriendsArraylist.add(friendsList);
            }
        }
*/
        Log.e("onlyphone", String.valueOf(allFriendsArraylist.size()));
        set_data();
    }

    public void mergeContactInactivearrays() {
        Log.e("userdataaray", " " + userarray.size() + " " + contactsArraylist.size());
        allFriendsArraylist = new ArrayList<>();
        allFriendsArraylist.addAll(contactsArraylist);
        allFriendsArraylist.addAll(userarray);

        HashSet hs = new HashSet();
        hs.addAll(allFriendsArraylist);
        allFriendsArraylist.clear();
        Log.e("checkclear", String.valueOf(allFriendsArraylist.size()));
        allFriendsArraylist.addAll(hs);

/*

        for (FriendsList friendsList : userarray) {
            if (!allFriendsArraylist.contains(friendsList)) {
                allFriendsArraylist.add(friendsList);
            }
        }
*/
        Log.e("allfriends", " " + allFriendsArraylist.size());
        set_data();
    }


    public void set_data() {
        if (allFriendsArraylist.size() > 0) {


            homeAdapter = new ChantsAdpater();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(homeAdapter);
            homeAdapter.notifyDataSetChanged();
        }
        Log.e("usersize", " " + userarray.size());

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

        Log.e("checkcontacts", "start");

        contactsArraylist = new ArrayList<>();
        Context context = getActivity();
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
                    Log.e("checkcontacts", "startgoing");
                    if (!email.isEmpty() && !name.isEmpty()) {
                        FriendsList contactModel = new FriendsList();
                        contactModel.setEmail(email);
                        contactModel.setName(name);
                        contactsArraylist.add(contactModel);
                        Log.e("contactsArrary", "call" + contactsArraylist.size());
                    }
                }
                cur1.close();
            }

        }


    }


    ///////////inviteFriendApi
    public void inviteFriend() {
        SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        chantid = prefs.getString("chant_id", "No name defined");

        display_progress("Inviting...");
        InviteServerobject inviteServerobject = new InviteServerobject();
        inviteServerobject.chantID = chantid;
        inviteServerobject.name = friendName;
        inviteServerobject.email = friendemail;
        Log.e("chantinvitation", chantid + friendName + friendemail);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<InviteServerobject> inviteObj = api.inviteFriend(inviteServerobject);
        inviteObj.enqueue(new Callback<InviteServerobject>() {
            @Override
            public void onResponse(Call<InviteServerobject> call, Response<InviteServerobject> response) {
                String invitestatus;
                hide_progress();
                if (response.body() != null) {
                    invitestatus = response.body().response;
                    Log.e("inviteStatus", " " + invitestatus);
                    if (invitestatus.equals("3")) {
                        Toast toast = Toast.makeText(getActivity(), "Friend Invited", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        Chantfriendscontroller.getintance().fetch_chantFriends(chantid);

                        //       startActivity(new Intent(getActivity(), HomeActivity.class));

                    } else {
                        Toast toast = Toast.makeText(getActivity(), "Friend Invitation Failed", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }
                }
            }

            @Override
            public void onFailure(Call<InviteServerobject> call, Throwable t) {
                hide_progress();
                Toast toast = Toast.makeText(getActivity(), "Friend Invitation Failed", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                Log.e("inviteStatus", "failed");
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
