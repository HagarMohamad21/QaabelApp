package com.Qaabel.org.view.fragment.MainActivity.profile;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.model.entities.UserModel;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


public class ProfileFragment extends Fragment
{

    ImageView editImageView, backImageView, profileImageView;
    TextView editProfileView, nameTextView, ageTextView, descriptionTextView, jobTextView,userName;
    TextView log_out_btn;
    UserModel USER;
    String token;
    FriendProfileViewModel friendProfileViewModel;


    public ProfileFragment()
    {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2)
    {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        actions();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init(View view)
    {
        nameTextView = view.findViewById(R.id.user_name_et);
        editProfileView = view.findViewById(R.id.edit_profile_et);
        ageTextView = view.findViewById(R.id.user_age_et);
        descriptionTextView = view.findViewById(R.id.user_details_et);
        profileImageView=view.findViewById(R.id.profile_img);
        jobTextView = view.findViewById(R.id.user_job_et);
        log_out_btn = view.findViewById(R.id.messageBtn);
        userName=view.findViewById(R.id.userName);
        backImageView = view.findViewById(R.id.back_img);
        editImageView = view.findViewById(R.id.settingsBtn);
       friendProfileViewModel= ViewModelProviders.of(this).get(FriendProfileViewModel.class);
       token=new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
       getUser();
    }


    private void getUser() {
        friendProfileViewModel.getUserProfile(token,new SharedPref(getContext()).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER)).observe(this,apiLoginResponse ->
        {
            if(apiLoginResponse!=null){
                if(apiLoginResponse.getStatus().equals("200")){

                    USER= apiLoginResponse.getUser();
                    fillData();

                }
            }
        });
    }

    @SuppressLint("CheckResult")
    private void actions()
    {
        Consumer<Object> objectConsumer = o -> {
            Navigation.findNavController(getActivity(), R.id.shopping_nav_host_fragment).navigate(R.id.action_navigation_profile_to_profileEdit);
        };
        RxView.clicks(editImageView).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(
                o -> {
                    Navigation.findNavController(getActivity(),R.id.shopping_nav_host_fragment).navigate(
                     R.id.action_navigation_profile_to_settingsFragment
                    );
                }
        );
        RxView.clicks(editProfileView).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(objectConsumer);
        RxView.clicks(log_out_btn).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> Utilities.LogOut(getActivity()));


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

    private static final String TAG = "ProfileFragment";

    private void fillData()
    {
        if(USER!=null){
            Log.d(TAG, "fillData: -------------------------------------"+USER.get_id());
            userName.setText("@"+USER.getUsername().toLowerCase());
            nameTextView.setText(USER.getName());
            descriptionTextView.setText(USER.getDescription());
            if(!USER.getCompany().trim().equals(""))
            jobTextView.setText(USER.getJob()+" at "+USER.getCompany());
            else
                jobTextView.setText(USER.getJob());
            if(USER.getImage()!=null&&!USER.getImage().equals(""))
            {
                Log.d(TAG, "fillData: ------"+USER.getImage());
                Picasso.get().load(USER.getImage()).into(profileImageView);
            }
        }

    }
}
