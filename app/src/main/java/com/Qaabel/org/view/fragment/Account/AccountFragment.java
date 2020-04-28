package com.Qaabel.org.view.fragment.Account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.navigation.Navigation;


import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.jakewharton.rxbinding2.view.RxView;
import com.Qaabel.org.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


public class AccountFragment extends Fragment
{
    View view;
    Button singUpButton, logInButton;
    public AccountFragment()
    {
        // Required empty public constructor
    }


    public static AccountFragment newInstance(String param1, String param2)
    {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        actions();
        return view;

    }

    private void init(View view)
    {

        singUpButton = view.findViewById(R.id.sign_up_btn);
        logInButton = view.findViewById(R.id.sign_in_btn);
    }

    @SuppressLint("CheckResult")
    private void actions()
    {

        RxView.clicks(singUpButton).throttleFirst(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        Navigation.findNavController(view).navigate(R.id.action_navigation_account_to_navigation_signIn);
                Navigation.findNavController(view).navigate(R.id.action_navigation_signIn_to_navigation_signUp);
                    }
                });
        RxView.clicks(logInButton).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe((Consumer<Object>) o -> Navigation.findNavController(view).navigate(R.id.action_navigation_account_to_navigation_signIn));

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
