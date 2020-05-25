package com.Qaabel.org.helpers.mapHelper

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.navigation.Navigation
import com.Qaabel.org.R
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import kotlinx.android.synthetic.main.fragment_location.*

 fun MapFragment.setListeners() {
    messagesBtn.setOnClickListener {
        Navigation.findNavController(this.activity!!, R.id.map).navigate(R.id.action_navigation_home_to_messagesFragment)

    }
    locationTxtView.setOnClickListener { startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
    currentLocationBtn.setOnClickListener {
        currentUserMarker?.isVisible=true
        for ( marker in markers.values){
            marker?.isVisible=true
        }
        currentLocationBtnClicked = true
        warningImg.visibility = View.GONE
        warningTxt.visibility = View.GONE
        clearSearch=true
        city_name.text=userCity
        if(nearUsersNum!=-1)
        available_num.text = "${nearUsersNum} available"
        bundleLocation = null
        searchedText=""
        moveCamera(null)
    }
   go_map.setOnClickListener {
       var bundle:Bundle?
       if (fromSearch&&!clearSearch)
       {   bundle=Bundle()
           bundle.putBoolean("FROM SEARCH",fromSearch)
           bundle.putInt("Number Of Users",numberOfSearchedUsers)
           Navigation.findNavController(this.activity!!, R.id.map).navigate(R.id.action_navigation_Friend_to_navigation_home,bundle)
       }
       else
           Navigation.findNavController(this.activity!!, R.id.map).navigate(R.id.action_navigation_Friend_to_navigation_home,null)


   }
}