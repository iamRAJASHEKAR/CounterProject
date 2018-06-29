package com.example.mypc.counterapp.Activities.Fragments;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
    String chantid;
    String friendName,friendemail;


    public Friends() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        chantid = HomeActivity.chantId;
        Log.e("chantID"," "+chantid);
        inviteFriends = new ArrayList<>();
        displayProgressDialog();
        loadContactsOnSeparateThread();
        recyclerView = view.findViewById(R.id.recycler);
        userarray = new ArrayList<>();
        display_inactiive_friends();
        return view;

    }

    ////////click on count btn

    private class ChantsAdpater extends RecyclerView.Adapter<ViewHolder> {


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list, null);
            final ViewHolder viewHolder = new ViewHolder(view, getActivity());
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
        {
            holder.name.setText(allFriendsArraylist.get(position).getName());
            holder.user.setText(allFriendsArraylist.get(position).getEmail());
             holder.invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                   holder.invite.setBackgroundResource(R.color.colorOrange);
                   friendName = allFriendsArraylist.get(position).getName();
                   friendemail = allFriendsArraylist.get(position).getEmail();
                   inviteFriend();
                }
            });

        }

        @Override
        public int getItemCount() {
            if(allFriendsArraylist!=null)
            {
                return allFriendsArraylist.size();
            }
            else
            {
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
        Log.e("activeevent", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("active_friends")) {
            display_inactiive_friends();

        }

    }

    private void display_inactiive_friends() {
        userarray = Chantfriendscontroller.getintance().inactive_friends;
        if(userarray !=null && contactsArraylist!=null)
        {
            mergeContactInactivearrays();
            if(allFriendsArraylist!=null)
            {
                hideProgressDialog();
            }
            else
                {
                    Toast toast = Toast.makeText(getActivity(),"Unable to fetch the all friends please try again",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
        }
        if (userarray.size() > 0) {
            homeAdapter = new ChantsAdpater();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(homeAdapter);
            homeAdapter.notifyDataSetChanged();
        }
        Log.e("usersize"," "+userarray.size());

    }

    public void loadContactsOnSeparateThread()
    {
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
    public void getPhoneDetailsFromDeviceContacts()
    {

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

            //call api
            // contactApiExecution();
        }


    }


    //////////merging contacts array list and inactive arraylist
    public void mergeContactInactivearrays()
    {


        Log.e("user"," "+userarray.size()+" "+contactsArraylist.size());
       allFriendsArraylist = new ArrayList<>();
       allFriendsArraylist.addAll(contactsArraylist);
       for(FriendsList friendsList:userarray)
       {
           if(!allFriendsArraylist.contains(friendsList))
           {
               allFriendsArraylist.add(friendsList);
           }
       }

        Log.e("allfriends"," "+allFriendsArraylist.size());

    }


    public void displayProgressDialog() {
        mProgress = new MaterialDialog.Builder(getActivity()).content("Loading").canceledOnTouchOutside(false).progress(true, 0).show();

    }

    private void hideProgressDialog() {

        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

  ///////////inviteFriendApi
    public void inviteFriend()
    {
       displayProgressDialog();
        InviteServerobject inviteServerobject = new InviteServerobject();
       inviteServerobject.chantID = chantid;
       inviteServerobject.name = friendName;
       inviteServerobject.email = friendemail;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<InviteServerobject> inviteObj = api.inviteFriend(inviteServerobject);
        inviteObj.enqueue(new Callback<InviteServerobject>() {
            @Override
            public void onResponse(Call<InviteServerobject> call, Response<InviteServerobject> response)
            {
               String invitestatus;
               if(response.body()!=null)
               {
                   invitestatus  =  response.body().response;
                   Log.e("inviteStatus"," "+invitestatus);
                   if(invitestatus.equals("3"))
                   {
                       startActivity(new Intent(getActivity(),HomeActivity.class));
                       hideProgressDialog();
                   }
               }
            }

            @Override
            public void onFailure(Call<InviteServerobject> call, Throwable t)
            {
                Log.e("inviteStatus","failed");
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }
}
