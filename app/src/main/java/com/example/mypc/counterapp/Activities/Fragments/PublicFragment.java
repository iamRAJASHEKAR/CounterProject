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

/**
 * A simple {@link Fragment} subclass.
 */
public class PublicFragment extends Fragment {

    ArrayList<PublicList> publicListsArray;
    PublicAdapter adapter;
    RecyclerView recyclerView;

    public PublicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_public, container, false);
        recyclerView = view.findViewById(R.id.public_recycler);
        publicListsArray = new ArrayList<>();
        //   setdata();
        return view;
    }

    public void setdata() {
        publicListsArray = FetchPublicChantController.getinstance().nepublicList;
        Log.e("publicfragment", String.valueOf(publicListsArray.size()));
        if (publicListsArray.size() > 0) {
            adapter = new PublicAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }


    public class PublicAdapter extends RecyclerView.Adapter<PublicAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acitvity_home_list, null);
            final ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.chantTitle.setText(publicListsArray.get(position).getChant_name());
            holder.chantText.setText(publicListsArray.get(position).getChant_description());
            holder.textVisibility.setText(publicListsArray.get(position).getPrivacy());
            holder.textCreated.setText(publicListsArray.get(position).getCreated_by());

        }

        @Override
        public int getItemCount() {
            return publicListsArray.size();
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

                        String chant_name = publicListsArray.get(getAdapterPosition()).chant_name;
                        String chant_dec = publicListsArray.get(getAdapterPosition()).chant_description;
                        String chant_privacy = publicListsArray.get(getAdapterPosition()).getPrivacy();
                        String chant_id = publicListsArray.get(getAdapterPosition()).chant_id;
                        String chant_created = publicListsArray.get(getAdapterPosition()).getCreated_by();
                        String chant_creted_email = publicListsArray.get(getAdapterPosition()).created_email;
                        // chantId = chant_id;
                        Log.e("pos", " " + chant_name + chant_dec + chant_id);

                        Intent intent = new Intent(getActivity(), ChantJoin.class);
                        intent.putExtra("chantname", chant_name);
                        intent.putExtra("chant_dec", chant_dec);
                        intent.putExtra("chant_privacy", chant_privacy);
                        intent.putExtra("chant_id", chant_id);
                        intent.putExtra("chant_created", chant_creted_email);
                        startActivity(intent);
                    }
                });
            }

        }
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginActivity.MessageEvent event) {

        Log.e("fileseventfrag", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("refreshchant")) {
            setdata();
        } else if (resultData.equals("error")) {

        }
    }
}
