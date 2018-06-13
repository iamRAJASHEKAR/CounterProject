package com.example.mypc.counterapp.Activities.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mypc.counterapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Friends extends Fragment {
    RecyclerView recyclerView;
    ArrayList<ChantsModel> userarray;
    ChantsAdpater homeAdapter;


    public Friends() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        userarray = new ArrayList<>();
        setData();
        homeAdapter = new ChantsAdpater();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(homeAdapter);


        return view;
    }

    public void setData() {
        for (int i = 0; i < 10; i++) {
            userarray.add(new ChantsModel("Vedas", "vedas@gmail.com"));
        }
    }

    private class ChantsAdpater extends RecyclerView.Adapter<ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list, null);
            final ViewHolder viewHolder = new ViewHolder(view, getActivity());
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.name.setText(userarray.get(position).getName());
            holder.user.setText(userarray.get(position).getUser());
        }

        @Override
        public int getItemCount()
        {
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
}
