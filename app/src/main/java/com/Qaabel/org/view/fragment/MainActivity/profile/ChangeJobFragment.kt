package com.Qaabel.org.view.fragment.MainActivity.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.Qaabel.org.R
import com.Qaabel.org.model.SharedPref.AppSharedPrefs
import com.Qaabel.org.model.SharedPref.SharedPref
import com.Qaabel.org.model.entities.UserModel
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel
import kotlinx.android.synthetic.main.fragment_change_job.*

/**
 * A simple [Fragment] subclass.
 */
class ChangeJobFragment : Fragment() {
    lateinit var friendProfileViewModel:FriendProfileViewModel
    var token=""
    lateinit var USER:UserModel
    lateinit var userData:UserModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        actions()
        getUser()

    }

    private fun actions() {
        update_data_img.setOnClickListener {
            updateData()
        }
        back_img.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
    }

    private fun updateData() {
        friendProfileViewModel.editProfile(token,getEditedUser()).observe(this, Observer {
            if (it!=null){
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getEditedUser(): UserModel? {
       var user=UserModel()
        System.out.println("---------------GET EDITED USER------------------"+job_et.text.toString())

        if(job_et.text.toString().trim() != ""){
            user.job=job_et.text.toString().trim()
        }

        else{
            user.job="   "
        }
        if(company_et.text.toString().trim() != "")
        user.company=company_et.text.toString().trim()
        else{
            user.company="   "
        }
        return user
    }

    private  fun init(){
        friendProfileViewModel=ViewModelProviders.of(this).get(FriendProfileViewModel::class.java)
        userData = SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER)
        token = SharedPref(context).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
    }
    private fun getUser() {
        friendProfileViewModel.getUserProfile(token,userData).observe(this, Observer {
            if(it!=null){
                USER=it.user
                fillData()
            }
        })
    }

    private fun fillData() {
        if(USER!=null){
            company_et.setText(USER.company)
            job_et.setText(USER.job)
        }

    }

}
