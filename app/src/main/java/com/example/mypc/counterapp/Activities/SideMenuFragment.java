package com.example.mypc.counterapp.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Model.Religion;
import com.example.mypc.counterapp.R;

import java.util.zip.Inflater;

public class SideMenuFragment extends Fragment
{

  RelativeLayout rlRateOurApp,rlShareApp,rlHelpUs,rlLogout;
  FragmentDrawerListner fragmentDrawerListner;
  public ActionBarDrawerToggle actionBarDrawerToggle;
 public DrawerLayout drawerLayout;



   View view;

    public SideMenuFragment()
    {

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_sidemenu,container,false);
        init();
        return view;
    }


    public void init()
    {

        ///widgets intialization
         rlRateOurApp = view.findViewById(R.id.rl_rateour_app);
         rlShareApp = view.findViewById(R.id.rl_share);
         rlHelpUs = view.findViewById(R.id.rl_helpus);
         rlLogout = view.findViewById(R.id.rl_logout);


       ///click on helpus
        rlHelpUs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(),HelpUsActivity.class));
                drawerLayout.closeDrawers();
            }
        });

        ////click on logout

        rlLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                logoutDialog();
            }
        });
    }



   ////////Logout alert Dailog

    public void logoutDialog()
    {

        TextViewRegular yes,no;
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_logout_dialog);
        yes = dialog.findViewById(R.id.yes);
        no = dialog.findViewById(R.id.no);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("call","dialog yes");
                startActivity(new Intent(getActivity(),LoginActivity.class));
                drawerLayout.closeDrawers();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("call","dialog no");
                dialog.dismiss();
                drawerLayout.closeDrawers();
            }
        });

        dialog.show();




    }



    public interface FragmentDrawerListner
    {

    }

    public void setFragmentDrawerListner(FragmentDrawerListner fragmentDrawerListner)
    {
        this.fragmentDrawerListner = fragmentDrawerListner;

    }


    public void setup(int fragment1, DrawerLayout drawer, final Toolbar toolbar)
    {

        view = getActivity().findViewById(fragment1);
        drawerLayout = drawer;
      actionBarDrawerToggle   = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.open, R.string.close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

   /* @Override
    public void onResume() {
        super.onResume();
        drawerLayout.closeDrawer(GravityCompat.START);
    }*/
}
