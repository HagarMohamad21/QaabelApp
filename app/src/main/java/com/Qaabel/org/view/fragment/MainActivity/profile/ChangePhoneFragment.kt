package com.Qaabel.org.view.fragment.MainActivity.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.Qaabel.org.R
import com.Qaabel.org.model.SharedPref.AppSharedPrefs
import com.Qaabel.org.model.SharedPref.SharedPref
import com.Qaabel.org.model.entities.UserModel
import com.Qaabel.org.view.fragment.Account.SignUpFragment
import com.Qaabel.org.viewModel.viewModel.account.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_change_phone.*


class ChangePhoneFragment : Fragment() {
     lateinit var vw:View
     var code:String=""
    var token:String=""
    var codeSelectedByUser = ""
    lateinit var signUpViewModel:SignUpViewModel;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        vw = inflater.inflate(R.layout.fragment_change_phone, container, false)
        return vw;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        actions()

    }

    private fun init() {
        code=ccp.selectedCountryCode
        token=SharedPref(context).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
        signUpViewModel=ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        if (codeSelectedByUser != "") {
            ccp.setCountryForPhoneCode(codeSelectedByUser.toInt())
        }

    }

    private fun actions() {
        backButton.setOnClickListener { fragmentManager!!.popBackStack() }
        phone_txt.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                done_btn.isActivated=true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        done_btn.setOnClickListener{
            var phone=(code) + phone_txt.text.toString()
            sendCode(phone)

        }
        ccp.setOnCountryChangeListener {
            codeSelectedByUser=it.phoneCode
        }
    }

    private fun sendCode(phone: String) {
        var user=getUser(phone)
        signUpViewModel.editSignupData(token,user).observe(this, Observer {
            run {
                if (it != null) {
                    if (it.status == 200) {
                        System.out.println("----------------------------------"+it.message)
                        //navigate to verification
                        Navigation.findNavController(activity!!, R.id.shopping_nav_host_fragment).navigate(R.id.action_changePhoneFragment_to_verficationFragment,sendBundle(phone))
                    } else {
                        error_tv.text=it.message
                        System.out.println("-------------------------------------" + it.message)
                    }
                }
            }
        })


    }

    private fun sendBundle(phone: String): Bundle? {
        var bundle=Bundle()
        bundle.putString("SOURCE","CHANGE PHONE")
        bundle.putString("PHONE",phone)
        return bundle
    }

    private fun getUser(phone: String): UserModel {
      var currentUser=SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER)
        var user= UserModel(currentUser.name,currentUser.username,phone,currentUser.password)
        return user
    }
}
