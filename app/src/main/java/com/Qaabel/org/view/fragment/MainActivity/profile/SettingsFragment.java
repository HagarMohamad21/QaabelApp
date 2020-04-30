package com.Qaabel.org.view.fragment.MainActivity.profile;


import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.viewModel.viewModel.account.SignUpViewModel;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */


public class SettingsFragment extends Fragment {
TextView  phoneEditTxt;
TextView emailEditTxt;
ImageView backBrn,updateImage,verifiedImage;
TextView blockListTxt;
UserModel userModel;
ConstraintLayout emailLayout;
FriendProfileViewModel friendProfileViewModel;
SignUpViewModel signupModel;
 SharedPref mSharedPref;
 String token;
 UserModel USER;
View view;
    private TextView errorTxt;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null){
            view= inflater.inflate(R.layout.fragment_settings, container, false);
            initViews();
            actions();
            getUser();
        }
        return  view;
    }

    private void getUser() {
        friendProfileViewModel.getUserProfile(token,new SharedPref(getContext()).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER)).observe(this,apiLoginResponse ->
        {
            if(apiLoginResponse!=null){
                if(apiLoginResponse.getStatus().equals("200")){

                    USER= apiLoginResponse.getUser();
                    filldata();

                }
            }
        });
    }

    private void actions() {
        backBrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        phoneEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open phone fragment
                Navigation.findNavController(getActivity(),R.id.shopping_nav_host_fragment).navigate(R.id.action_settingsFragment_to_changePhoneFragment);
            }
        });
        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorTxt.setVisibility(View.GONE);
              checkData();

            }
        });
        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ==============================================");
                Navigation.findNavController(getActivity(),R.id.shopping_nav_host_fragment).navigate(R.id.action_settingsFragment_to_emailFragment);
            }
        });
        blockListTxt.setOnClickListener(v -> {
             Navigation.findNavController(getActivity(),R.id.shopping_nav_host_fragment).navigate(R.id.action_settingsFragment_to_navigation_blocks);
        });


    }

    private void initViews() {
        emailLayout=view.findViewById(R.id.emailLayout);
        blockListTxt=view.findViewById(R.id.blockListBtn);
        emailEditTxt=view.findViewById(R.id.email_et);
        phoneEditTxt=view.findViewById(R.id.phone_et);
        updateImage=view.findViewById(R.id.update_data_img);
        backBrn= view.findViewById(R.id.back_img);
        verifiedImage=view.findViewById(R.id.verifiedImage);
        errorTxt=view.findViewById(R.id.errorTxt);
         mSharedPref=new SharedPref(getContext());
        signupModel = ViewModelProviders.of(this.getActivity()).get(SignUpViewModel.class);
        friendProfileViewModel=ViewModelProviders.of(this).get(FriendProfileViewModel.class);
        token=new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
    }

    private void filldata()
    {

       if(USER!=null)
        emailEditTxt.setText(USER.getEmail());
        phoneEditTxt.setText(USER.getPhone());

    }

    private void checkData(){
        if(emailEditTxt.getText().equals("")||phoneEditTxt.getText().equals("")){
          emailEditTxt.setError("This Field is required");
          phoneEditTxt.setError("This Field is required");
        }

        else{

            showDialog();
        }

    }

    private void showDialog() {
        {
            final Dialog delete;
            delete = new Dialog(getActivity(), android.R.style.Theme_Dialog);
            delete.setContentView(R.layout.dialog_edit);
            delete.setCancelable(false);
            delete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView yes = delete.findViewById(R.id.yes_btn);
            TextView no = delete.findViewById(R.id.no_btn);
            TextView msg = delete.findViewById(R.id.msg);
            TextView yesB = delete.findViewById(R.id.yes_btn);
            yesB.setText("Yes");
            msg.setText("Are You sure you want to update your profile");
            yes.setOnClickListener(v -> {
                String token = mSharedPref.getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
                mSharedPref.getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER).setPhone(phoneEditTxt.getText().toString());;
                updateDate(token, mSharedPref.getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER));
                delete.dismiss();


            });
            no.setOnClickListener(view -> delete.dismiss());
            delete.show();
        }
    }

    private void updateDate(String token, UserModel userModel)
    {

    }


}
