package com.example.mypc.counterapp.Activities.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mypc.counterapp.Activities.LoginActivity;
import com.example.mypc.counterapp.Controllers.Chantfriendscontroller;
import com.example.mypc.counterapp.Model.FriendsList;
import com.example.mypc.counterapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinedFriends extends Fragment {

    RecyclerView recyclerView;
    ArrayList<FriendsList> userarray;
    ChantsAdpater homeAdapter;

    public JoinedFriends() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_joined_friends, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        userarray = new ArrayList<>();
        display_actiive_friends();
        return view;

    }


    public void display_actiive_friends() {
        userarray = Chantfriendscontroller.getintance().active_friends;
        Log.e("joinfriends", String.valueOf(userarray.size()));
        if (userarray.size() > 0) {
            homeAdapter = new ChantsAdpater();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(homeAdapter);
        }
    }

    private class ChantsAdpater extends RecyclerView.Adapter<ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chant_list, null);
            final ViewHolder viewHolder = new ViewHolder(view, getActivity());
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.name.setText(userarray.get(position).getName());
            holder.user.setText(userarray.get(position).getEmail());
        }

        @Override
        public int getItemCount() {
            return userarray.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, user;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            user = itemView.findViewById(R.id.user);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

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
            display_actiive_friends();

        }
    }
}
