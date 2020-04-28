package com.Qaabel.org.view.fragment.Account;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.Qaabel.org.R;
import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.view.activity.MainActivity;
import com.Qaabel.org.viewModel.viewModel.account.LoginViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static com.android.volley.VolleyLog.TAG;


public class SignInFragment extends Fragment
{

    EditText emailEditText, passwordEditText;
    Button loginButton;
    TextView error_tv, forget_pass_txt, sign_up_TextView;
    boolean validPassword, validName;
    ProgressBar loadingProgressBar;
    private LoginViewModel loginViewModel;
    View view;

    private boolean visablePass = false;

    public SignInFragment()
    {
        // Required empty public constructor
    }


    public static SignInFragment newInstance(String param1, String param2)
    {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
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
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        init(view);
        actions();

        return view;
    }


    private void init(View view)
    {
        emailEditText = view.findViewById(R.id.username_et);
        error_tv = view.findViewById(R.id.error_tv);
        passwordEditText = view.findViewById(R.id.password_et);
        sign_up_TextView = view.findViewById(R.id.sign_up_tv);
        forget_pass_txt = view.findViewById(R.id.forget_pass_txt);
        loginButton = view.findViewById(R.id.login);
        loginButton.setActivated(false);
        loadingProgressBar = view.findViewById(R.id.loading);
        loadingProgressBar.setVisibility(View.GONE);
        validName = validPassword = false;
        loginViewModel = ViewModelProviders.of(this.getActivity()).get(LoginViewModel.class);

    }

    private void getPhone() {
        if(new SharedPref(getContext()).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER)!=null){
        String phone=new SharedPref(getContext()).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER).getPhone();
        if(!phone.equals("0")){
            emailEditText.setText(phone);
        }}
    }

    LiveData<ApiLoginResponse> loginResponse;

    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void actions()
    {
        RxView.clicks(loginButton).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    if (loginButton.isActivated())
                    {
                        error_tv.setVisibility(View.GONE);
                        emailEditText.setBackgroundResource(R.drawable.ic_text_background);
                        passwordEditText.setBackgroundResource(R.drawable.ic_text_background);

                        Utilities.MsignUpUser = new UserModel(emailEditText.getText().toString().trim(), passwordEditText.getText().toString());
                        loadingProgressBar.setVisibility(View.VISIBLE);
                        loginViewModel.login().observe(this, apiSignUpResponse -> {
                            loadingProgressBar.setVisibility(View.GONE);
                            if (apiSignUpResponse != null)
                                if (apiSignUpResponse.getStatus().equals("200"))
                                {
                                    error_tv.setVisibility(View.GONE);
                                    emailEditText.setBackgroundResource(R.drawable.ic_text_background);
                                    passwordEditText.setBackgroundResource(R.drawable.ic_text_background);
                                    saveLoginData(apiSignUpResponse,true);
                                    getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();
                                }

                            else if(apiSignUpResponse.getStatus().equals("403")){
                                    error_tv.setVisibility(View.GONE);
                                    emailEditText.setBackgroundResource(R.drawable.ic_text_background);
                                    passwordEditText.setBackgroundResource(R.drawable.ic_text_background);
                                    saveLoginData(apiSignUpResponse,false);
                                    getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();
                                }
                            else
                                {
                                    Log.d(TAG, "actions: ++++++++++++++++++++++++++++++++++++++++++"+apiSignUpResponse.getMessage());
                                    Log.d(TAG, "actions: ++++++++++++++++++++++++++++++++++++++++++"+apiSignUpResponse.getStatus());
                                    error_tv.setVisibility(View.VISIBLE);
                                    error_tv.setText((R.string.wrong_login));
                                    emailEditText.setBackgroundResource(R.drawable.ic_wrong_txt);
                                    passwordEditText.setBackgroundResource(R.drawable.ic_wrong_txt);

                                }
                            Utilities.MsignUpUser = null;
                        });
                    } else
                    {
                        error_tv.setVisibility(View.GONE);
                        checkRequiredData();
                    }
                });

        RxView.clicks(forget_pass_txt).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> Navigation.findNavController(getActivity(), R.id.account_nav_fragment).navigate(R.id.action_navigation_signIn_to_navigation_forgetPassword));


        RxView.clicks(sign_up_TextView).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> Navigation.findNavController(getActivity(), R.id.account_nav_fragment).navigate(R.id.action_navigation_signIn_to_navigation_signUp));


        passwordEditText.addTextChangedListener(new TextWatcher()
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
        emailEditText.addTextChangedListener(new TextWatcher()
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



    private void checkRequiredData()
    {
        if (emailEditText.getText().toString().isEmpty())
        {
            emailEditText.setError(getString(R.string.field_require));
            emailEditText.requestFocus();
        } else if (passwordEditText.getText().toString().isEmpty())
        {
            passwordEditText.setError(getString(R.string.field_require));
            passwordEditText.requestFocus();
        }
    }

    private void activeButton()
    {
        if (emailEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty())
        {
            loginButton.setActivated(false);
        } else
        {
            loginButton.setActivated(true);
        }

    }


    private void saveLoginData(ApiLoginResponse apiSignUpResponse,boolean isEmailVerified)
    {
        Log.d(TAG, "saveLoginData: ++++++++++++++++++++++++++"+apiSignUpResponse.getAuthToken());
        new SharedPref(getContext()).saveString(AppSharedPrefs.PHONE_VERIFIED, "TRUE");
        SharedPref sharedPref = new SharedPref(getContext());
        sharedPref.saveString(AppSharedPrefs.SHARED_PREF_TOKRN, apiSignUpResponse.getAuthToken());
        if(apiSignUpResponse.getUser()==null){
            UserModel userModel=new UserModel();
            userModel.setTempUserNameOrPhone(emailEditText.getText().toString().trim());
            userModel.setPassword(passwordEditText.getText().toString());
            sharedPref.saveUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER, userModel);
        }
        else{
            sharedPref.saveUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER, apiSignUpResponse.getUser());
        }
        if(isEmailVerified){
            new SharedPref(getContext()).setEmailVerified(AppSharedPrefs.EMAIL_VERIFIED_KEY,true);
        }


    }

    private boolean isEmail(String email)
    {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            return true;
        } else
        {
            return false;
        }
    }
}
