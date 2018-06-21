package com.example.mypc.counterapp.Activities.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mypc.counterapp.Counter.CounterActivity;
import com.example.mypc.counterapp.Fonts.ButtonBold;
import com.example.mypc.counterapp.Fonts.ButtonRegular;
import com.example.mypc.counterapp.Fonts.EditTextRegular;
import com.example.mypc.counterapp.R;

public class AddNewFriend extends Fragment {

    EditTextRegular addFriendName,addFriendEmail;
    ButtonRegular save;
    View view;
    ButtonBold startCount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_add_new_friend, container, false);

       init();
        return view;
    }


    public void init()
    {
        addFriendName = view.findViewById(R.id.edit_name);
        addFriendEmail = view.findViewById(R.id.edit_email);

        save = view.findViewById(R.id.btn_save);
        save.setOnClickListener(ClickOnsaveBtn);

        startCount = view.findViewById(R.id.btn_start_counting);
        startCount.setOnClickListener(ClickOnButton);
    }


    ////click on save button
    View.OnClickListener ClickOnsaveBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            Validations();
        }
    };


    //////////click on counterbutton
    View.OnClickListener ClickOnButton = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
             startActivity(new Intent(getActivity(), CounterActivity.class));
        }
    };




   ////////////EditText validations
    public void Validations()
    {
        if (addFriendName.getText().toString().isEmpty())
        {
            addFriendName.setError("Name Can'not be empty");
        }
        else if (addFriendEmail.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(addFriendEmail.getText().toString()).matches())
        {
            addFriendEmail.setError("Enter Valid Email");
        }
    }


}
