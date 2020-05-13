package com.Qaabel.org.view.fragment.Account;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Qaabel.org.R;
import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.viewModel.viewModel.account.CompleteDataViewModel;
import com.Qaabel.org.viewModel.viewModel.account.LoginViewModel;
import com.Qaabel.org.viewModel.viewModel.account.SignUpViewModel;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.android.volley.VolleyLog.TAG;


public class ConfimEmailFragment extends DialogFragment
{

    TextView mailTextView, resendTextView;
    UserModel userData;
    private SignUpViewModel signUpViewModel;
    String token;
    private DialogInterface.OnDismissListener onDismissListener;
    private ImageView closeButton;
    private CompleteDataViewModel completeDataViewModel;
    FriendProfileViewModel friendProfileViewModel;
   LoginViewModel loginViewModel;
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener)
    {
        this.onDismissListener = onDismissListener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            userData=bundle.getParcelable("USER MODEL");
        }
        token = new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        friendProfileViewModel=ViewModelProviders.of(this).get(FriendProfileViewModel.class);
        loginViewModel=ViewModelProviders.of(this).get(LoginViewModel.class);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_email, container, false);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(500, 300);
        window.setGravity(Gravity.CENTER);
        signUpViewModel = ViewModelProviders.of(this.getActivity()).get(SignUpViewModel.class);
        init(view);
        action();
        completeDataViewModel = ViewModelProviders.of(this.getActivity()).get(CompleteDataViewModel.class);
        return view;
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);
        if (onDismissListener != null)
        {
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    public static ConfimEmailFragment newInstance(String title)
    {
        ConfimEmailFragment frag = new ConfimEmailFragment();
        Bundle args = new Bundle();

        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        //TODO:
    }

    private void init(View view)
    {
        mailTextView  = view.findViewById(R.id.emailTxt);
        resendTextView   = view.findViewById(R.id.resend_tv);
        closeButton=view.findViewById(R.id.closeButton);
    }

    @SuppressLint("CheckResult")
    private void action()
    {
        UserModel u = new UserModel();
        Log.d(TAG, "action: ---------------------------------------------"+userData);

        if(userData!=null){
            u.setEmail(userData.getEmail());
            u.setSex(userData.getSex());
            u.setBirthdayDate(userData.getBirthdayDate());
        }
        RxView.clicks(resendTextView).throttleFirst(1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(o ->{
            completeDataViewModel.completeData(token,u).observe(this,apiLoginResponse -> {

                if(apiLoginResponse!=null){
                    // Toast.makeText(getContext(),"api response not null",Toast.LENGTH_SHORT).show();

                    if (apiLoginResponse.getStatus().equals("200")){
                        //tell user that an email has been sent
                        Toast.makeText(getContext(), "Email has been sent", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.d(TAG, "action: *****************API RESPONSE *******************"+apiLoginResponse.getMessage());
                    }
                }

            });
        });



         mailTextView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(Intent.ACTION_MAIN);
                 intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                 getActivity().startActivity(intent);
             }
         });
         closeButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(new SharedPref(getContext()).isEmailVerified(AppSharedPrefs.EMAIL_VERIFIED_KEY)){
                     Log.d(TAG, "onClick: ----------------------------------"+new SharedPref(getContext()).isEmailVerified(AppSharedPrefs.EMAIL_VERIFIED_KEY));
                     dismiss();
                 }
                 else{
                     Log.d(TAG, "onClick: ----------------------GET CORRECT TOKEN---------------");
                    getCorrectToken();

                 }

             }
         });
    }

    private void getCorrectToken() {
        UserModel user=new SharedPref(getContext()).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER);
       if(user!=null ){

           String email=user.getEmail();
           if(email==null){
               email=user.getTempUserNameOrPhone();
           }
           if(email==null){
               email=user.getUsername();
           }

           String password=user.getPassword();
           if(email!=null&&password!=null){
               Utilities.MsignUpUser = new UserModel(email.trim(),password.trim());
               loginViewModel.login().observe(this,apiLoginResponse -> {
                   if(apiLoginResponse!=null&&apiLoginResponse.getStatus().equals("200")){
                      saveLoginData(apiLoginResponse,true);
                   }
                   else{
                       Utilities.LogOut(getActivity(),false);
                   }

               });
           }
           else{
               Log.d(TAG, "getCorrectToken: --------------SIGN UP----------"+user.getUsername());
           }
       }

    }
    private void saveLoginData(ApiLoginResponse apiSignUpResponse, boolean isEmailVerified)
    {
        Log.d(TAG, "saveLoginData: ++++++++++++++++++++++++++"+apiSignUpResponse.getAuthToken());
        new SharedPref(getContext()).saveString(AppSharedPrefs.PHONE_VERIFIED, "TRUE");
        SharedPref sharedPref = new SharedPref(getContext());
        sharedPref.saveString(AppSharedPrefs.SHARED_PREF_TOKRN, apiSignUpResponse.getAuthToken());
        sharedPref.saveUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER, apiSignUpResponse.getUser());

        if(isEmailVerified){
            new SharedPref(getContext()).setEmailVerified(AppSharedPrefs.EMAIL_VERIFIED_KEY,true);
        }


    }


}
