package com.Qaabel.org.helpers

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.Qaabel.org.R
import com.Qaabel.org.model.entities.FriendModel
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import com.Qaabel.org.viewModel.viewModel.friend.FlashUserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_info_windo.view.friendName
import kotlinx.android.synthetic.main.fragment_location.view.*

class CustomInfoWindow(var mapFragment:MapFragment,var popWindowView:View,nearUser:FriendModel) {
    private var flashUserViewModel: FlashUserViewModel?=ViewModelProviders.of(mapFragment).get(FlashUserViewModel::class.java)
    private val TAG = "CustomInfoWindow"
    init {
        Log.d(TAG, "-----------------: "+nearUser.id)
        Log.d(TAG, "---------DISTANCE--------: "+nearUser.dist.calculated)

        popWindowView.friendName.text=nearUser.name
        Picasso.get().load(nearUser.image).into(popWindowView.profile_img)
        if(nearUser.isflashed){
            Log.d(TAG, "------------------IS FLASHED-------------: ")
           flashSentView()
        }
        else if(nearUser.isflashedyou){
            Log.d(TAG, "----------------------IS FLASHED YOU------------------: ")
            isFlashedViews()
        }
        else if(nearUser.isfriend){
            Log.d(TAG, "--------------------------IS FRIEND---------------------------: ")
            friendsViews()
        }
        else{
            Log.d(TAG, "------------------------------------FLASH VIEWS-----------------------: ")
           flashViews()
        }

        popWindowView.flashBtn.setOnClickListener {
            //flash
            flashUserViewModel?.flashUser(mapFragment.mtoken,nearUser.id)?.observe(mapFragment, Observer {
                if(it!=null){
                    if(it.getMessage()=="user flashed"){
                       flashSentView()
                    }
                    else
                    Toast.makeText(mapFragment.context,it.getMessage(),Toast.LENGTH_SHORT).show()
                }
            })
        }
        popWindowView.flashBackBtn.setOnClickListener {
            flashUserViewModel?.flashUserBack(mapFragment.mtoken,nearUser.id)?.observe(mapFragment, Observer {
                if(it!=null){
                    if(it.getMessage()=="you are now friends "){
                        friendsViews()
                    }
                    Toast.makeText(mapFragment.context,it.getMessage(),Toast.LENGTH_SHORT).show()
                }
            })
        }
        popWindowView.ignoreBtn.setOnClickListener {
            flashUserViewModel?.ignoreUser(mapFragment.mtoken,nearUser.id)?.observe(mapFragment, Observer {
                if(it!=null){
                    if(it.getStatus()==200){
                        Toast.makeText(mapFragment.context,"User ignored",Toast.LENGTH_SHORT).show()
                        flashViews()
                    }
                }
            })
        }
        popWindowView.messageBtn.setOnClickListener {
            var bundle= Bundle()
            bundle.putParcelable("FRIEND_USER",nearUser)
            Navigation.findNavController(mapFragment.activity!!, R.id.shopping_nav_host_fragment).navigate(R.id.action_navigation_home_to_navigation_chat2,bundle)
        }
        popWindowView.markerPopup.setOnClickListener {
            if(Common.DISTANCE>40){
                return@setOnClickListener
            }
            //open profile
            var bundle=Bundle()
            bundle.putParcelable("FRIEND_USER",nearUser)
            Navigation.findNavController(mapFragment.activity!!, R.id.shopping_nav_host_fragment).navigate(R.id.action_navigation_home_to_navigation_user_profile,bundle)
        }

    }

    private fun friendsViews() {
        popWindowView.ignoreBtn.toggleVisiblity(false)
        popWindowView.flashBackBtn.toggleVisiblity(false)
        popWindowView.flashSentTxt.toggleVisiblity(false)
        popWindowView.flashBtn.toggleVisiblity(false)
        popWindowView.flashedText.toggleVisiblity(false)
        popWindowView.messageBtn.toggleVisiblity(true)
    }

    private fun isFlashedViews() {
        popWindowView.messageBtn.toggleVisiblity(false)
        popWindowView.ignoreBtn.toggleVisiblity(true)
        popWindowView.flashBackBtn.toggleVisiblity(true)
        popWindowView.flashSentTxt.toggleVisiblity(false)
        popWindowView.flashBtn.toggleVisiblity(false)
        popWindowView.flashedText.toggleVisiblity(true)
    }

    private fun flashViews() {
        popWindowView.messageBtn.toggleVisiblity(false)
        popWindowView.ignoreBtn.toggleVisiblity(false)
        popWindowView.flashBackBtn.toggleVisiblity(false)
        popWindowView.flashSentTxt.toggleVisiblity(false)
        popWindowView.flashBtn.toggleVisiblity(true)
        popWindowView.flashedText.toggleVisiblity(false)
    }

    private fun outOfDistanceViews(){
        popWindowView.messageBtn.toggleVisiblity(false)
        popWindowView.ignoreBtn.toggleVisiblity(false)
        popWindowView.flashBackBtn.toggleVisiblity(false)
        popWindowView.flashSentTxt.toggleVisiblity(false)
        popWindowView.flashBtn.toggleVisiblity(false)
        popWindowView.flashedText.toggleVisiblity(false)
    }

    private fun flashSentView() {
        popWindowView.messageBtn.toggleVisiblity(false)
        popWindowView.ignoreBtn.toggleVisiblity(false)
        popWindowView.flashBackBtn.toggleVisiblity(false)
        popWindowView.flashSentTxt.toggleVisiblity(true)
        popWindowView.flashBtn.toggleVisiblity(false)
        popWindowView.flashedText.toggleVisiblity(false)
    }
}