package com.Qaabel.org.view.fragment.Account;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.viewModel.viewModel.account.ResetPassViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class ForgetPassFragment extends Fragment {
    private static final String TAG = "ForgetPassFragment";
    String bundle="";
    Button openEmailBtn, doneButton;
    TextView emailTxt, emailTxtDes,error_tv,titleTxt;
    RelativeLayout back_login;
    private ResetPassViewModel resetPassViewModel;


    public ForgetPassFragment() {
        // Required empty public constructor
    }

    public static ForgetPassFragment newInstance(String param1, String param2) {
        ForgetPassFragment fragment = new ForgetPassFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b=getArguments();
        if(b!=null){
            if(b.getString("source").equals("edit_profile")){
                bundle="Reset Password";

            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_pass, container, false);

        init(view);
        actions();
        return view;
    }

    @SuppressLint("CheckResult")
    private void actions() {
        back_login.setOnClickListener
                (view -> Navigation.findNavController(getActivity(),
                        R.id.account_nav_fragment).navigate(R.id.action_navigation_forgetPassword_to_navigation_signIn));
        RxView.clicks(doneButton).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    if (doneButton.isActivated()) {
                        error_tv.setVisibility(View.INVISIBLE);
                        UserModel userModel = new UserModel();
                        userModel.setEmail(emailTxt.getText().toString().trim());
                        resetPassViewModel.forgetPass(new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN), userModel).observe(ForgetPassFragment.this, apiLoginResponse -> {
                            if (apiLoginResponse != null)
                                if (apiLoginResponse.getStatus().equals("200")) {
                                    emailTxt.setVisibility(View.GONE);
                                    doneButton.setVisibility(View.GONE);
                                    emailTxtDes.setVisibility(View.VISIBLE);
                                    openEmailBtn.setVisibility(View.VISIBLE);
                                    back_login.setVisibility(View.VISIBLE);
                                } else {
                                    error_tv.setVisibility(View.VISIBLE);
                                    Log.d(TAG, "actions: 8888888888888888888888888888888888"+apiLoginResponse.getMessage());
                                    // Toast.makeText(getContext(), apiLoginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            else{
                                        // Toast.makeText(getContext(), "Check your connection ", Toast.LENGTH_SHORT).show();
                                    }

                        });

//                                 Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();

                    } else {
                        checkRequiredData();
                    }
                });
        RxView.clicks(openEmailBtn).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                    getActivity().startActivity(intent);
                });


        emailTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                activeButton();
            }
        });
    }

    private void init(View view) {
        titleTxt=view.findViewById(R.id.titleTxt);
        openEmailBtn = view.findViewById(R.id.open_email_btn);
        back_login = view.findViewById(R.id.back_login);
        doneButton = view.findViewById(R.id.done_btn);
        error_tv=view.findViewById(R.id.error_tv);
        doneButton.setActivated(false);
        emailTxt = view.findViewById(R.id.email_txt);
        emailTxtDes=view.findViewById(R.id.emailTxtDes);
        resetPassViewModel = ViewModelProviders.of(this.getActivity()).get(ResetPassViewModel.class);
        if(bundle.equals("Reset Password")){
            titleTxt.setText(bundle);
            back_login.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void checkRequiredData() {
        if (emailTxt.getText().toString().isEmpty()) {
            emailTxt.setError(getString(R.string.field_require));
            emailTxt.requestFocus();
        }

    }

    private void activeButton() {
        if (emailTxt.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailTxt.getText().toString().trim()).matches()) {
            doneButton.setActivated(false);
        } else {
            doneButton.setActivated(true);

        }
    }
}
