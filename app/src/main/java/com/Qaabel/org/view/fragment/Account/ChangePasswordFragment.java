package com.Qaabel.org.view.fragment.Account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.jakewharton.rxbinding2.view.RxView;
import com.Qaabel.org.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class ChangePasswordFragment extends Fragment
{
    View view;
    Button changePassBtn;
    EditText oldPassEditText, newPassEditText, confirmPassEditText;
    ImageView backImageView;

    public ChangePasswordFragment()
    {
        // Required empty public constructor
    }


    public static ChangePasswordFragment newInstance(String param1, String param2)
    {
        ChangePasswordFragment fragment = new ChangePasswordFragment();


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
        view = inflater.inflate(R.layout.fragment_chang_pass, container, false);
        init(view);
        actions();
        return view;

    }

    private void init(View view)
    {
        changePassBtn = view.findViewById(R.id.sign_up_btn);
        oldPassEditText = view.findViewById(R.id.old_pass_txt);
        confirmPassEditText = view.findViewById(R.id.confirm_pass_txt);
        newPassEditText = view.findViewById(R.id.old_pass_txt);
        backImageView = view.findViewById(R.id.back_img);
    }

    @SuppressLint("CheckResult")
    private void actions()
    {
        RxView.clicks(backImageView).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> Navigation.findNavController(view).navigate(R.id.action_changePassword_to_profileEdit));

        RxView.clicks(changePassBtn).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    if (changePassBtn.isActivated())
                    {
                        Toast.makeText(getActivity(), "Change Password", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        checkMissed();
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
                activeBtn();
            }
        });
        newPassEditText.addTextChangedListener(new TextWatcher()
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
                activeBtn();
            }
        });
        confirmPassEditText.addTextChangedListener(new TextWatcher()
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
                activeBtn();
            }
        });
    }

    private void activeBtn()
    {
        if (oldPassEditText.getText().toString().equals("") || newPassEditText.getText().toString().equals("") || confirmPassEditText.getText().toString().equals(""))
            changePassBtn.setActivated(false);
        else
        {
            if (checkPass()) changePassBtn.setActivated(true);
        }
    }

    private boolean checkPass()
    {
        if (!newPassEditText.getText().toString().equals(confirmPassEditText.getText().toString()))
        {
            Toast.makeText(getActivity(), (R.string.identical_pass), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void checkMissed()
    {
        if (oldPassEditText.getText().toString().equals(""))
        {
            oldPassEditText.setError(getActivity().getString(R.string.field_require));
            oldPassEditText.requestFocus();
        }
        if (newPassEditText.getText().toString().equals(""))
        {
            newPassEditText.setError(getActivity().getString(R.string.field_require));
            newPassEditText.requestFocus();
        }
        if (confirmPassEditText.getText().toString().equals(""))
        {
            confirmPassEditText.setError(getActivity().getString(R.string.field_require));
            confirmPassEditText.requestFocus();
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


}
