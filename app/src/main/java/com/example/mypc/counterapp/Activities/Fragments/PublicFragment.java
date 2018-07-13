package com.example.mypc.counterapp.Activities.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.text.DateFormat.getDateTimeInstance;

/**
 * A simple {@link Fragment} subclass.
 */
public class PublicFragment extends Fragment {

    ArrayList<PublicList> publicListsArray;
    PublicAdapter adapter;
    TextView text_nodata;
    RecyclerView recyclerView;

    public PublicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_public, container, false);
        text_nodata = view.findViewById(R.id.text_nodata);
        recyclerView = view.findViewById(R.id.public_recycler);
        publicListsArray = new ArrayList<>();
        setdata();
        return view;
    }

    public void setdata() {
        publicListsArray = FetchPublicChantController.getinstance().nepublicList;
        Log.e("publicfragment", String.valueOf(publicListsArray.size()));
        if (publicListsArray.size() > 0) {
            text_nodata.setVisibility(View.GONE);
            adapter = new PublicAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else {
            text_nodata.setVisibility(View.VISIBLE);
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
            holder.timestamp.setText("Created :" + trying(publicListsArray.get(position).getTime_stamp()));
            Log.e("timestamp", publicListsArray.get(position).getTime_stamp());
        }

        @Override
        public int getItemCount()
        {
            return publicListsArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextViewBold chantTitle;
            TextViewRegular chantText, timestamp;
            TextViewRegular textVisibility, textCreated;
            ImageView rightarrow;

            public ViewHolder(View itemView) {
                super(itemView);
                chantTitle = itemView.findViewById(R.id.chant_title);
                timestamp = itemView.findViewById(R.id.timestamp);
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
                        String chant_id = publicListsArray.get(getAdapterPosition()).getChant_id();
                        String chant_created = publicListsArray.get(getAdapterPosition()).getCreated_email();
                        String chant_creted_email = publicListsArray.get(getAdapterPosition()).created_email;
                        // chantId = chant_id;
                        Log.e("pos", " " + chant_name + chant_dec + chant_id);

                        Intent intent = new Intent(getActivity(), ChantJoin.class);
                        intent.putExtra("chantname", chant_name);
                        intent.putExtra("chant_dec", chant_dec);
                        intent.putExtra("chant_privacy", chant_privacy);
                        intent.putExtra("chant_id", chant_id);
                        intent.putExtra("chant_created", chant_creted_email);
                        intent.putExtra("chant_createmail", chant_created);
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


    public String trying(String time) {
        String startTime = null;
        long sunixSeconds = Long.valueOf(time);
        Date date = new java.util.Date(sunixSeconds);
        SimpleDateFormat datesString = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm aa");
        startTime = datesString.format(date);
        Log.e("startTime", startTime);
        return startTime;
    }


    /* public static String getTimeDate(String timeStamp) {
     *//* Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        *//**//* debug: is it local time? *//**//*
        Log.d("Time zone: ", tz.getDisplayName());

        *//**//* date formatter in local timezone *//**//*
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        sdf.setTimeZone(tz);

        *//**//* print your timestamp and double check it's the date you expect *//**//*

        long timestamp = Long.parseLong(timeStamp);
        String localTime = sdf.format(new Date(timestamp * 1000)); // I assume your timestamp is in seconds and you're converting to milliseconds?
        Log.d("Time: ", localTime);
        Log.e("unixconvert", localTime);
        return localTime;*//*
    }
*/

}
