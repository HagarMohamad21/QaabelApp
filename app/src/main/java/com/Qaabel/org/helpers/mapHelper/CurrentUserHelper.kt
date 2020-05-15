package com.Qaabel.org.helpers.mapHelper

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import com.Qaabel.org.R
import com.Qaabel.org.model.Api.Response.ApiLoginResponse
import com.Qaabel.org.model.SharedPref.AppSharedPrefs
import com.Qaabel.org.model.SharedPref.SharedPref
import com.Qaabel.org.model.Utilities.Utilities
import com.Qaabel.org.model.entities.UserModel
import com.Qaabel.org.view.fragment.Account.CompeleteFragment
import com.Qaabel.org.view.fragment.Account.ConfimEmailFragment
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import com.Qaabel.org.viewModel.viewModel.account.LoginViewModel

 fun MapFragment.getCorrectToken() {
    Log.d(TAG, "getCorrectToken: ----------------------------")
    var loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    val email = SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER).email
    val password = SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER).password
    if (email != null && password != null) {
        Utilities.MsignUpUser = UserModel(email.trim { it <= ' ' }, password.trim { it <= ' ' })
        loginViewModel.login().observe(this, android.arch.lifecycle.Observer<ApiLoginResponse> { apiLoginResponse: ApiLoginResponse? ->
            if (apiLoginResponse != null && apiLoginResponse.status == "200") { //email is verified
                Log.d(TAG, "getCorrectToken: ---EMAIL IS VERIFIED------")
                SharedPref(context).saveString(AppSharedPrefs.SHARED_PREF_TOKRN, apiLoginResponse.authToken)
                SharedPref(context).setEmailVerified(AppSharedPrefs.EMAIL_VERIFIED_KEY, true)
                SharedPref(context).saveUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER,apiLoginResponse.user)
                Log.d(TAG, "getCorrectToken: ----------------------------------------"+apiLoginResponse.user.image)
                Log.d(TAG, "getCorrectToken: ----------------------------------------"+apiLoginResponse.user.email)
            } else {
                val newFragment = ConfimEmailFragment.newInstance("Complete")
                newFragment.isCancelable = false
                newFragment.show(fragmentManager, "dialog")
            }
        })
    }
}

 fun MapFragment.checkIfUserCompletedData() {
    Log.d(TAG, "checkIfEmailNotVerified: ********************CHECKING EMAIL ******************")

    if (SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER) != null)
        if (SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER).email == null)
        {
            var  reg=(SharedPref(context).getStrin(getString(R.string.emailRegistered)))
            if(reg==getString(R.string.True)){
                Log.d(TAG, "checkIfUserCompletedData: --------------EMAIL IS REGISTERED BUT NOT VERIFIED")
                var user=SharedPref(context).getUser(AppSharedPrefs.TEMP_USER_FROM_COMPLETE_DATA)
                var bundle= Bundle()
                bundle!!.putParcelable("USER MODEL", user)
                val newFragment = ConfimEmailFragment.newInstance("Complete")
                newFragment.isCancelable = false
                newFragment.show(fragmentManager, "dialog")
                newFragment.arguments = bundle
            }
            else{
                Log.d(TAG, "checkIfUserCompletedData: -----------------EMAIL IS NULL-----------------------")
                var  newFragment = CompeleteFragment.newInstance("Complete")
                newFragment.isCancelable = false
                newFragment.show(fragmentManager, "dialog")
            }



        }
        else
        {
            if (!SharedPref(context).isEmailVerified(AppSharedPrefs.EMAIL_VERIFIED_KEY)) {
                Log.d(TAG, "checkIfUserCompletedData: ---------------------------------EMAIL NOT VERIFIED----------------------")
                getCorrectToken()
            } else { //email is verified

            }
        }
    else
    {
        Log.d(TAG, "checkIfUserCompletedData: --------------USER IS NULL---------------")
        //user is null
        var  newFragment = CompeleteFragment.newInstance("Complete")
        newFragment.isCancelable = false
        newFragment.show(fragmentManager, "dialog")
    }

}