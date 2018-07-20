package com.example.mypc.counterapp.Activities.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypc.counterapp.Activities.ChantJoin;
import com.example.mypc.counterapp.Activities.LoginActivity;
import com.example.mypc.counterapp.Database.DatabaseManager;
import com.example.mypc.counterapp.Database.Userdata;
import com.example.mypc.counterapp.Fonts.TextViewBold;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class PrivateFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Userdata> privateArray;
    PrivateAdapter privateAdapter;
    TextView text_nodata;
    String createdBy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_private, container, false);
        recyclerView = view.findViewById(R.id.recycler_private);
        text_nodata = view.findViewById(R.id.text_nodata);
        SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        createdBy = prefs.getString("name", "No name defined");
        set_data();
        return view;
    }

    public void set_data() {
        privateArray = new ArrayList<>();
        privateArray = DatabaseManager.getInstance().getalluser();
        Collections.reverse(privateArray);
        if (privateArray.size() > 0)
        {
            text_nodata.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            privateAdapter = new PrivateAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(privateAdapter);
            privateAdapter.notifyDataSetChanged();
        } else
            {
        }
    }

    @Override
    public void onResume() {
        set_data();
        super.onResume();
    }

    public class PrivateAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acitvity_home_list, null);
            final ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.chantTitle.setText(privateArray.get(position).getFirstname());
            holder.chantText.setText(privateArray.get(position).getLastname());
            holder.textCreated.setText(createdBy);
            holder.textVisibility.setText("Private");
            holder.timestamp.setText("Created at " + trying(privateArray.get(position).getUsername()));
        }

        @Override
        public int getItemCount() {
            return privateArray.size();
        }
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
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

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if (privateArray.size() > 0) {

                        int position = getAdapterPosition();
                        Log.e("adpetdjn", String.valueOf(position));
                        String chant_name = privateArray.get(getAdapterPosition()).getFirstname();
                        String chant_dec = privateArray.get(getAdapterPosition()).getLastname();
                        String idofaray = String.valueOf(privateArray.get(getAdapterPosition()).getId());
                        String chant_privacy = "private";
                        String chant_id = "private";
                        String chant_created = privateArray.get(getAdapterPosition()).getEmail();
                        String chant_creted_email = privateArray.get(getAdapterPosition()).getEmail();

                        Intent intent = new Intent(getActivity(), ChantJoin.class);
                        intent.putExtra("chantname", chant_name);
                        intent.putExtra("chant_dec", chant_dec);
                        intent.putExtra("position", position);
                        intent.putExtra("userid", idofaray);
                        intent.putExtra("chant_privacy", chant_privacy);
                        intent.putExtra("chant_id", chant_id);
                        intent.putExtra("chant_created", chant_creted_email);
                        intent.putExtra("chant_createmail", chant_created);
                        startActivity(intent);
                        Log.e("pos", "" + idofaray);
                    } else
                        {
                            recyclerView.setVisibility(View.GONE);
                      //  Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                        privateAdapter.notifyDataSetChanged();
                        text_nodata.setVisibility(View.VISIBLE);
                    }
                    /*
                    String chant_name = chantsArray.get(getAdapterPosition()).getChant_name();
                    String chant_dec = chantsArray.get(getAdapterPosition()).getChant_description();
                    String chant_privacy = chantsArray.get(getAdapterPosition()).getPrivacy();
                    String chant_id = chantsArray.get(getAdapterPosition()).getChant_id();
                    String chant_creted_email = chantsArray.get(getAdapterPosition()).getCreated_email();
                    chantId = chant_id;
                    Log.e("pos", " " + chant_name + chant_dec + chant_id + chant_creted_email + chant_privacy);

*/
                }
            });
        }

    }

    public String trying(String time) {
        String startTime = null;
        long sunixSeconds = Long.valueOf(time);
        Date date = new java.util.Date(sunixSeconds);
        SimpleDateFormat datesString = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        startTime = datesString.format(date);
        Log.e("startTime", startTime);
        return startTime;
    }
}
