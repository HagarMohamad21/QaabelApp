package com.Qaabel.org.view.fragment.MainActivity.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.view.fragment.Account.ConfimEmailFragment;


public class HomeFragment extends Fragment
{


    public HomeFragment()
    {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2)
    {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (new SharedPref(getContext()).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER).getEmail().equals(null))
        {
//            CompeleteFragment newFragment = CompeleteFragment.newInstance("Complete");
//            newFragment.show(getFragmentManager(), "dialog");
        } else if (new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_CONFIRM_EMAIL).equals("0"))
        {
            ConfimEmailFragment newFragment = ConfimEmailFragment.newInstance("Complete");
//            newFragment.show(getFragmentManager(), "dialog");

        }
            return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {

    }

    public void showVechelTypeDialog()
    {  // show confirmation dialog
       // CompeleteFragment dialogFragment = new CompeleteFragment();

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }


}
