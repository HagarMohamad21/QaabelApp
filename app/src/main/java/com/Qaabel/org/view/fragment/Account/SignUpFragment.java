package com.Qaabel.org.view.fragment.Account;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.navigation.Navigation;
import com.Qaabel.org.R;
import com.Qaabel.org.model.Api.Response.ApiSignUpResponse;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.viewModel.viewModel.account.SignUpViewModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import java.util.concurrent.TimeUnit;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class SignUpFragment extends Fragment implements CountryCodePicker.OnCountryChangeListener
{
    private static final String TAG = "SignUpFragment";
    View mView;
    String countryName="";
    EditText nameET, userNameET, phoneET, confirmPasswordET, passwordET;
    TextView loginTV, terms_tx, terms_of_use,unavailableUsername,nameErrorTv;
    Button signUpBTN;
    ImageView  wrong_user_img, backImg;
    SignUpViewModel signUpViewModel;
    ProgressBar progressBar;
    CheckBox termsCheckBox;
    ConstraintLayout termsLayout;
    CountryCodePicker codePicker;
    private ImageView wrong_phone_img;
    private TextView unavailablePhone;
    public static String codeSelectedByUser="";
    private TextView passwordsDoesntMatch;
    String code;

    public SignUpFragment()
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
        mView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        init(mView);
        actions();
        return mView;
    }

    private void init(View v)
    {
        nameET = v.findViewById(R.id.email_et);
        terms_of_use = v.findViewById(R.id.term_of_use);
        terms_tx = v.findViewById(R.id.terms_txt);
        userNameET = v.findViewById(R.id.username_txt);
        phoneET = v.findViewById(R.id.phone_txt);
        unavailableUsername=v.findViewById(R.id.unavailableUsername);
        passwordET = v.findViewById(R.id.password_txt);
        confirmPasswordET = v.findViewById(R.id.confirm_pass_txt);
        signUpBTN = v.findViewById(R.id.sign_up_btn);
        signUpBTN.setActivated(false);
        loginTV = v.findViewById(R.id.Login_tv);
        progressBar = v.findViewById(R.id.progressBar);
        termsCheckBox = v.findViewById(R.id.term_agrred_checkBox);
        wrong_user_img = v.findViewById(R.id.wrong_user_img);
        backImg = v.findViewById(R.id.back_term_img);
        termsLayout = v.findViewById(R.id.term_layout);
        progressBar.setVisibility(View.GONE);
       codePicker=v.findViewById(R.id.ccp);
        termsLayout.setVisibility(View.GONE);
        terms_tx.setMovementMethod(new ScrollingMovementMethod());
        unavailablePhone=v.findViewById(R.id.unavailablePhone);
        wrong_phone_img=v.findViewById(R.id.wrong_phone_img);
        passwordsDoesntMatch=v.findViewById(R.id.passwordsDoesntMatch);
        nameErrorTv=v.findViewById(R.id.nameErrorTv);
         code=codePicker.getSelectedCountryCode();
         if(!codeSelectedByUser.equals("")){
             codePicker.setCountryForPhoneCode(Integer.parseInt(codeSelectedByUser));
         }
        Log.d(TAG, "init: _______________________________"+code);
    }





    @SuppressLint("CheckResult")
    private void actions()
    {
        RxView.clicks(signUpBTN)
                .throttleFirst(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    if (signUpBTN.isActivated())
                    {
                        nameErrorTv.setVisibility(View.GONE);
                        passwordsDoesntMatch.setVisibility(View.GONE);
                        wrong_user_img.setVisibility(View.GONE);
                        unavailableUsername.setVisibility(View.GONE);
                        wrong_phone_img.setVisibility(View.GONE);
                        unavailablePhone.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);



                        UserModel userModel = new UserModel(nameET.getText().toString(), userNameET.getText().toString(), (code) + phoneET.getText().toString(), passwordET.getText().toString());

                        signUpViewModel = ViewModelProviders.of(this.getActivity()).get(SignUpViewModel.class);
                        String token=new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
                         if( token.equals("0")){
                             signUpViewModel.register(userModel).observe(this, apiSignUpResponse -> {
                                 if (apiSignUpResponse.getStatus() == 200)
                                 {
                                     wrong_user_img.setImageResource(R.drawable.ic_right);
                                     wrong_user_img.setVisibility(View.VISIBLE);
                                     new SharedPref(getContext()).saveUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER, userModel);
                                     new SharedPref(getContext()).saveString(AppSharedPrefs.SHARED_PREF_TOKRN, apiSignUpResponse.getAuthToken());
                                     Log.d(TAG, "actions: ******************************* code is"+apiSignUpResponse.getMessage());
                                     Log.d(TAG, "actions: ******************************* code is"+apiSignUpResponse.getCode());
                                     Navigation.findNavController(getActivity(),
                                             R.id.account_nav_fragment).
                                             navigate(R.id.action_navigation_Friend_to_navigation_verfiy_phone);
                                     new SharedPref(getContext()).saveString(AppSharedPrefs.SHARED_PREF_CONFIRM_EMAIL, "0");

                                 } else
                                 {
                                     showErrors(apiSignUpResponse);

                                 }

                                 progressBar.setVisibility(View.GONE);
                                 Utilities.MsignUpUser = null;
                             });
                         }
                         else{

                             UserModel editedUser=new UserModel(nameET.getText().toString(), userNameET.getText().toString(), (code) + phoneET.getText().toString(), passwordET.getText().toString());
                             signUpViewModel.editSignupData(token,editedUser).observe(this,apiSignUpResponse -> {
                                 if(apiSignUpResponse!=null){
                                     if(apiSignUpResponse.getStatus()==200){
                                         new SharedPref(getContext()).saveUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER, editedUser);
                                         new SharedPref(getContext()).saveString(AppSharedPrefs.SHARED_PREF_TOKRN, apiSignUpResponse.getAuthToken());
                                         Log.d(TAG, "actions: ******************************* code is"+apiSignUpResponse.getMessage());
                                         Log.d(TAG, "actions: ******************************* code is"+apiSignUpResponse.getCode());

                                         Navigation.findNavController(getActivity(),
                                                 R.id.account_nav_fragment).
                                                 navigate(R.id.action_navigation_Friend_to_navigation_verfiy_phone);
                                         progressBar.setVisibility(View.GONE);
                                     }
                                     else{
                                         showErrors(apiSignUpResponse);
                                         progressBar.setVisibility(View.GONE);

                                     }
                                 }
                             });

                         }

                    }
                    else
                    {
                        checkRequiredData();
                    }
                });

        RxView.clicks(loginTV).throttleFirst(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> Navigation.findNavController(getActivity(), R.id.account_nav_fragment).navigate(R.id.action_navigation_signUp_to_navigation_signIn));
        RxView.clicks(termsCheckBox).throttleFirst(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> ActiveButton());
        RxView.clicks(terms_of_use).throttleFirst(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    termsLayout.setVisibility(View.VISIBLE);

                    terms_tx.setVisibility(View.VISIBLE);

                });
        RxView.clicks(backImg).throttleFirst(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    termsLayout.setVisibility(View.GONE);


                });

        userNameET.addTextChangedListener(new TextWatcher()
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
                ActiveButton();
            }
        });
        nameET.addTextChangedListener(new TextWatcher()
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
                ActiveButton();
            }
        });

        passwordET.addTextChangedListener(new TextWatcher()
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
                ActiveButton();
            }
        });
        confirmPasswordET.addTextChangedListener(new TextWatcher()
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
                ActiveButton();
            }
        });
        phoneET.addTextChangedListener(new TextWatcher()
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
                ActiveButton();
            }
        });
        userNameET.addTextChangedListener(new TextWatcher()
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
                ActiveButton();
            }
        });
        codePicker.setOnCountryChangeListener(selectedCountry -> {
            codeSelectedByUser=selectedCountry.getPhoneCode();
        });
    }

    private void showErrors(ApiSignUpResponse apiSignUpResponse) {
        Log.d(TAG, "showErrors: +++++==++++++");
        switch (apiSignUpResponse.getMessage().trim()){
            case "This Username is Taken":
                unavailableUsername.setText("This username is taken");
                wrong_user_img.setVisibility(View.VISIBLE);
                unavailableUsername.setVisibility(View.VISIBLE);
                break;

            case "This phone is Taken":
                unavailablePhone.setText("This phone is taken");
                wrong_phone_img.setVisibility(View.VISIBLE);
                unavailablePhone.setVisibility(View.VISIBLE);
                break;


            case "\"name\" length must be at least 2 characters long":
                nameErrorTv.setVisibility(View.VISIBLE);
                break;

            case "\"username\" length must be at least 3 characters long":
                unavailableUsername.setText("Username length must be at least 3 characters");
                unavailableUsername.setVisibility(View.VISIBLE);
                break;

            case "\"phone\" length must be at least 9 characters long":
                unavailablePhone.setText("Phone must be at least 9 characters");
                unavailablePhone.setVisibility(View.VISIBLE);
                break;

            default:
                Toast.makeText(getActivity(),apiSignUpResponse.getMessage(),Toast.LENGTH_SHORT).show();

        }
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

    private void checkRequiredData()
    {
        if (nameET.getText().toString().isEmpty())
        {
            nameET.setError(getString(R.string.field_require));
            nameET.requestFocus();
        } else if (userNameET.getText().toString().isEmpty())
        {
            userNameET.setError(getString(R.string.field_require));
            userNameET.requestFocus();
        } else if (userNameET.getText().toString().length() < 3)
        {
            userNameET.setError(getString(R.string.username_length));
            userNameET.requestFocus();
        }
        else if (phoneET.getText().toString().isEmpty())
        {
            phoneET.setError(getString(R.string.field_require));
            phoneET.requestFocus();
        } else if (passwordET.getText().toString().isEmpty())
        {
            passwordET.setError(getString(R.string.field_require));
            passwordET.requestFocus();
        }
        else if(passwordET.getText().length()<8){
            passwordET.setError("Password length should be more than 8 chars");
            passwordET.requestFocus();
        }
        else if (!termsCheckBox.isChecked())

        {
            termsCheckBox.setError(" you should accept the Terms ");
            termsCheckBox.requestFocus();
        }
        else if (!(passwordET.getText().toString()).equals(confirmPasswordET.getText().toString()))
        {
            passwordsDoesntMatch.setVisibility(View.VISIBLE);

        }

    }

    private void ActiveButton()
    {termsCheckBox.setError(null);
        if (userNameET.getText().toString().isEmpty() || phoneET.getText().toString().isEmpty() || passwordET.getText().toString().isEmpty() || confirmPasswordET.getText().toString().isEmpty() || nameET.getText().toString().isEmpty() || !termsCheckBox.isChecked() ||
                (!(passwordET.getText().toString()).equals(confirmPasswordET.getText().toString()))||passwordET.getText().length()<8)
        {
            signUpBTN.setActivated(false);
        } else
        {
            signUpBTN.setActivated(true);
        }
    }


    @Override
    public void onCountrySelected(com.rilixtech.widget.countrycodepicker.Country selectedCountry)
    {

    }
    @Override
    public void onResume() {
        super.onResume();
        ActiveButton();
    }
}
