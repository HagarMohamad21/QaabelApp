package com.Qaabel.org.view.fragment.Account;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.viewModel.viewModel.account.ResetPassViewModel;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class ResetPassFragment extends Fragment
{
    private static final String TAG = "ResetPassFragment";
    EditText oldPassEditText, newPassWordTxt, confirmPasswordTxt;
    Button resetPassButton;
    boolean isPasswordMatch=false,isOldSameAsNew=false;
    TextInputLayout oldTextInputLayout;
    private FriendProfileViewModel resetPassViewModel;
    private String token;
    TextView backButton,error_tv;

    public ResetPassFragment()
    {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_reset_pass, container, false);
        init(view);
        actions();

        return view;
    }

    private void init(View view)
    {
        newPassWordTxt = view.findViewById(R.id.new_pass_txt);
        oldTextInputLayout = view.findViewById(R.id.old_pass_layout);
        oldPassEditText = view.findViewById(R.id.old_pass_txt);
        confirmPasswordTxt = view.findViewById(R.id.confirm_pass_txt);
        resetPassButton = view.findViewById(R.id.reset_pass_txt);
        backButton=view.findViewById(R.id.backButton);
        error_tv=view.findViewById(R.id.error_tv);
        resetPassButton.setActivated(false);
        Bundle bundle = getArguments();
        resetPassViewModel = ViewModelProviders.of(this.getActivity()).get(FriendProfileViewModel.class);
        token = new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        if (bundle != null)
        {
            String fr = bundle.getString("source");
            if (fr.equals("edit_profile"))
            {
                oldTextInputLayout.setVisibility(View.VISIBLE);
            } else oldTextInputLayout.setVisibility(View.GONE);

        }
    }

    @SuppressLint("CheckResult")
    private void actions()
    {
    backButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getFragmentManager().popBackStack();
        }
    });
        RxView.clicks(resetPassButton).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    if (resetPassButton.isActivated())
                    {
                        isOldSameAsNew=oldPassEditText.getText().toString().equals(newPassWordTxt.getText().toString());
                        if(isOldSameAsNew){
                            error_tv.setText("Please choose a new password");
                            error_tv.setVisibility(View.VISIBLE);
                            return;
                        }
                        isPasswordMatch=newPassWordTxt.getText().toString().equals(confirmPasswordTxt.getText().toString());
                        if(!isPasswordMatch){
                            error_tv.setVisibility(View.VISIBLE);
                            error_tv.setText("Passwords should match");
                            return;
                        }
                        error_tv.setVisibility(View.GONE);
                        UserModel userModel = new UserModel();
                        userModel.setPassword(newPassWordTxt.getText().toString());
                        userModel.setOldPassword(oldPassEditText.getText().toString());

                        resetPassViewModel.editProfile(token, userModel).observe(getActivity(), apiLoginResponse -> {

                            if (apiLoginResponse != null)
                            {
                               if(apiLoginResponse.getStatus().equals("200"))
                               {
                                   Toast.makeText(getContext(), apiLoginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                               else{
                                   error_tv.setVisibility(View.VISIBLE);
                                   error_tv.setText(apiLoginResponse.getMessage());
                               }
                            }
                        });
                    } else
                    {
                        checkRequiredData();
                    }
                });

        confirmPasswordTxt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                activeButton();
            }
        });
        oldPassEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                activeButton();
            }
        });
        newPassWordTxt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                activeButton();
            }
        });
    }


//    private void checkPasswordIFSame() {
//        if(newPassWordTxt.getText().toString().equals(confirmPasswordTxt.getText().toString())){
//            continuePass=true;
//        }
//        else{
//            error_tv.setText("Passwords should match");
//        }
//        if(oldPassEditText.getText().toString().equals(newPassWordTxt)){
//            error_tv.s
//        }
//    }


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

    private void checkRequiredData()
    {
        if (oldPassEditText.getText().toString().isEmpty())
        {
            oldPassEditText.setError(getString(R.string.field_require));
            oldPassEditText.requestFocus();
        } else if (newPassWordTxt.getText().toString().isEmpty())
        {
            newPassWordTxt.setError(getString(R.string.field_require));
            newPassWordTxt.requestFocus();
        } else if (confirmPasswordTxt.getText().toString().isEmpty())
        {
            confirmPasswordTxt.setError(getString(R.string.field_require));
            confirmPasswordTxt.requestFocus();
        }

    }

    private void activeButton()
    {
        if (confirmPasswordTxt.getText().toString().isEmpty() || oldPassEditText.getText().toString().isEmpty() || newPassWordTxt.getText().toString().isEmpty())
        {
            resetPassButton.setActivated(false);
        } else
        {

            resetPassButton.setActivated(true);

        }
    }
}
