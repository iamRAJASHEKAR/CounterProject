package com.example.mypc.counterapp.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.mypc.counterapp.R;

import java.util.zip.Inflater;

public class SideMenuFragment extends Fragment
{

  LinearLayout rlRateOurApp,rlShareApp,rlHelpUs,rlLogout;
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

         rlRateOurApp = view.findViewById(R.id.rl_rateour_app);
         rlShareApp = view.findViewById(R.id.rl_share);
        rlHelpUs = view.findViewById(R.id.rl_helpus);
        rlLogout = view.findViewById(R.id.rl_logout);


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




}
