package com.Qaabel.org.helpers.mapHelper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.Qaabel.org.R
import com.Qaabel.org.helpers.Common
import com.Qaabel.org.model.entities.FriendModel
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import com.google.android.gms.maps.model.Marker

fun MapFragment.initBroadCast() {

    mMessageReceiver=object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.getStringExtra(Common.SERVICE_MESSAGE)== Common.NotificationType_FLASH){
                val flashUser: FriendModel = intent.getParcelableExtra(Common.SERVICE_USER) as FriendModel
                val marker: Marker?=markers[flashUser.username]
                val user=marker?.tag as FriendModel
                val gender=user.sex
                val resourse=if(gender==0) { R.drawable.ic_bluemarker }
                else{
                    R.drawable.ic_pinkmarker}
                marker?.setIcon(customMarker?.createCustomMarker(nearUsersProfiles[user.username],resourse,true))
                user.isfriend=false
                user.isflashed=false
                user.isflashedyou=true
                marker?.tag=user
            }
        }
    }
}