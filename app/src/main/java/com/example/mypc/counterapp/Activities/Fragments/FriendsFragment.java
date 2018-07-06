package com.example.mypc.counterapp.Activities.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mypc.counterapp.Activities.ChantJoin;
import com.example.mypc.counterapp.Activities.ChantsActivity;
import com.example.mypc.counterapp.Activities.LoginActivity;
import com.example.mypc.counterapp.Controllers.FetchPublicChantController;
import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Model.PublicList;
import com.example.mypc.counterapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {
    RecyclerView recyclerView;

    public static String chantId;
    FriendAdapter adapter;
    ArrayList<PublicList> chantsArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friendnew, container, false);
        recyclerView = view.findViewById(R.id.friends_recycler);
        setdata();
        return view;
    }

    public void setdata() {
        chantsArray = new ArrayList<>();
        chantsArray = FetchPublicChantController.getinstance().friendslist;
        Log.e("friendsfragment", String.valueOf(chantsArray.size()));
        if (chantsArray.size() > 0) {
            adapter = new FriendAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }


    }

    public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
        @NonNull
        @Override
        public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acitvity_home_list, null);
            final ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
            holder.chantTitle.setText(chantsArray.get(position).getChant_name());
            holder.chantText.setText(chantsArray.get(position).getChant_description());
            holder.textVisibility.setText(chantsArray.get(position).getPrivacy());
            holder.textCreated.setText(chantsArray.get(position).getCreated_by());

        }

        @Override
        public int getItemCount() {
            return chantsArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextViewBold chantTitle;
            TextViewRegular chantText;
            TextViewRegular textVisibility, textCreated;
            ImageView rightarrow;

            public ViewHolder(View itemView) {
                super(itemView);
                chantTitle = itemView.findViewById(R.id.chant_title);
                chantText = itemView.findViewById(R.id.chant_text);
                textVisibility = itemView.findViewById(R.id.text_visiible);
                textCreated = itemView.findViewById(R.id.text_created);
                rightarrow = itemView.findViewById(R.id.right_arrow);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chant_name = chantsArray.get(getAdapterPosition()).getChant_name();
                        String chant_dec = chantsArray.get(getAdapterPosition()).getChant_description();
                        String chant_privacy = chantsArray.get(getAdapterPosition()).getPrivacy();
                        String chant_id = chantsArray.get(getAdapterPosition()).getChant_id();
                        String chant_creted_email = chantsArray.get(getAdapterPosition()).getCreated_email();
                        chantId = chant_id;
                        Log.e("pos", " " + chant_name + chant_dec + chant_id + chant_creted_email + chant_privacy);

                        Intent intent = new Intent(getActivity(), ChantsActivity.class);
                        intent.putExtra("chantname", chant_name);
                        intent.putExtra("chant_dec", chant_dec);
                        intent.putExtra("chant_privacy", chant_privacy);
                        intent.putExtra("chant_id", chant_id);
                        intent.putExtra("chant_created", chant_creted_email);
                        intent.putExtra("chant_created_email", chant_creted_email);
                        startActivity(intent);
                    }
                });
            }

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
        Log.e("busfrifrag", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("refreshchant")) {
            chantsArray = new ArrayList<>();
            chantsArray = FetchPublicChantController.getinstance().friendslist;
            Log.e("friendsfragment", String.valueOf(chantsArray.size()));
            if (chantsArray.size() > 0) {
                adapter = new FriendAdapter();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        } else if (resultData.equals("error")) {
        }
    }

}
