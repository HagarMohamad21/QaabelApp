package com.Qaabel.org.view.fragment.Account;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.view.activity.MainActivity;
import com.Qaabel.org.view.activity.SplashActivity;
import com.Qaabel.org.viewModel.viewModel.account.SignUpViewModel;
import com.alimuzaffar.lib.pin.PinEntryEditText;


public class VerficationFragment extends Fragment
{
    private static final String TAG = "VerficationFragment";
    PinEntryEditText code;
    Button verfiyButton;
    SignUpViewModel signUpViewModel;
    String mcode = "",editedPhone;
    TextView resend_tv,wrongCode;
    View view;
    String token;
    TextView backButton;
    boolean actionPhone=false;




    public VerficationFragment()
    {
    }

    public static VerficationFragment newInstance(String param1, String param2)
    {
        VerficationFragment fragment = new VerficationFragment();
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
         view = inflater.inflate(R.layout.fragment_verfication, container, false);
        init(view);
        startTimer();
        actions();

        return view;
    }

    private void init(View view)
    {
        signUpViewModel = ViewModelProviders.of(this.getActivity()).get(SignUpViewModel.class);
        code = view.findViewById(R.id.code_pin);
        resend_tv = view.findViewById(R.id.resend_tv);
        verfiyButton = view.findViewById(R.id.verify_btn);
        verfiyButton.setActivated(false);
        token = new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        backButton= view.findViewById(R.id.backButton);
        wrongCode=view.findViewById(R.id.wrongCodeTxt);

        if (getArguments()!=null){
            if(getArguments().getString("SOURCE").equals("CHANGE PHONE")){
                actionPhone=true;
            }
            editedPhone=getArguments().getString("PHONE");
        }
    }

    private void actions()
    { backButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: BACK BUTTON CLICKED)))))))))))))))))))");
            getFragmentManager().popBackStack();
        }
    });
        code.setOnPinEnteredListener(str -> {
            if (str.toString().length() < 6)
            {
                verfiyButton.setActivated(false);
            } else
            {
                mcode = str.toString();
                verfiyButton.setActivated(true);
            }
        });
        resend_tv.setOnClickListener(view -> {
            startTimer();
            signUpViewModel.resendCode(token).observe(this, apiLoginResponse -> {
                if (apiLoginResponse != null)
                {
                    if (apiLoginResponse.getStatus().equals("200"))
                    {

                    } else{

                    }


                }
            });
        });
        verfiyButton.setOnClickListener(view1 -> {
            if (verfiyButton.isActivated())
            {
                UserModel userModel = new SharedPref(getContext()).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER);
                userModel.setCode(mcode);
                signUpViewModel.activePhone(new SharedPref(getContext())
                        .getStrin(AppSharedPrefs.SHARED_PREF_TOKRN), userModel)
                        .observe(VerficationFragment.this, apiLoginResponse -> {
                    if (apiLoginResponse != null)
                    {
                        if (apiLoginResponse.getStatus().equals("200"))

                        { if(actionPhone){
                            getFragmentManager().popBackStack();
                            getFragmentManager().popBackStack();
                            getFragmentManager().popBackStack();
                            //update phone
                           userModel.setPhone(editedPhone);



                        }
                        else{
                            new SharedPref(getContext()).saveString(AppSharedPrefs.PHONE_VERIFIED, "TRUE");
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        }
                        } else
                            wrongCode.setVisibility(View.VISIBLE);
                    }
                });
            } else
            {
                wrongCode.setVisibility(View.VISIBLE);
            }
        });

    }
    private int counter=0;
    private void startTimer() {
            counter=60;
        resend_tv.setEnabled(false);
        new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){
                resend_tv.setText("Resend in "+String.valueOf(counter)+" ..");
                counter--;
            }
            public  void onFinish(){
                resend_tv.setText("Resend");
                resend_tv.setEnabled(true);
            }
        }.start();
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
