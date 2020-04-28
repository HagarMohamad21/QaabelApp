package com.Qaabel.org.view.fragment.MainActivity.profile;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.view.fragment.Account.ConfimEmailFragment;
import com.Qaabel.org.viewModel.viewModel.account.SignUpViewModel;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmailFragment extends Fragment {
    private static final String TAG = "EmailFragment";
    ImageView backBrn,updateImage,verifiedImage;
    EditText email_et;
    SignUpViewModel signUpViewModel;
    TextView errorTxt;
    UserModel userModel=new UserModel();
    String token;
    FriendProfileViewModel friendProfileViewModel;
    public EmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_email, container, false);
        initViews(view);
        actions();
        return view;
    }

    private void actions() {
        backBrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorTxt.setVisibility(View.GONE);
               if(email_et.getText().toString().trim().equals("")){
                   email_et.setError("This field is required");
               }

               else{
                   updateEmail();
               }


            }
        });
    }

    private void updateEmail() {
        userModel.setEmail(email_et.getText().toString().trim());
     signUpViewModel.changeEmail(token,userModel).observe(this,apiSignUpResponse -> {
         if(apiSignUpResponse!=null){
             Log.d(TAG, "updateEmail: ---------------------------------------"+apiSignUpResponse.getMessage());
             if(apiSignUpResponse.getMessage().equals("This Email is Taken")){
                 errorTxt.setVisibility(View.VISIBLE);
             }
             else if(apiSignUpResponse.getMessage().equals("verification email has been sent")){
             userModel=new SharedPref(getContext()).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER);
                 Bundle bundle = new Bundle();
                 bundle.putParcelable("MyData", userModel);
                 ConfimEmailFragment newFragment = ConfimEmailFragment.newInstance("Complete");
                 newFragment.setArguments(bundle);
                 newFragment.setCancelable(true);
                 newFragment.show(getFragmentManager(), "dialog");
             }

         }
     });
    }

    private void initViews(View view) {
        signUpViewModel= ViewModelProviders.of(this.getActivity()).get(SignUpViewModel.class);
       backBrn=view.findViewById(R.id.back_img);
       updateImage=view.findViewById(R.id.update_data_img);
       errorTxt=view.findViewById(R.id.errorTxt);
       email_et=view.findViewById(R.id.email_et);
       token=new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        friendProfileViewModel = ViewModelProviders.of(this.getActivity()).get(FriendProfileViewModel.class);
       getUser();

    }

    private void getUser() {
        friendProfileViewModel.getUserProfile(token,new SharedPref(getContext()).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER)).observe(this,apiLoginResponse ->
        {
            if(apiLoginResponse!=null){
                if(apiLoginResponse.getStatus().equals("200")){
                    email_et.setText(apiLoginResponse.getUser().getEmail());
                }
            }
        });
    }

}
