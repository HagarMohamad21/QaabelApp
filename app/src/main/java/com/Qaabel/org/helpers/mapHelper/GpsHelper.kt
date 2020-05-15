package com.Qaabel.org.helpers.mapHelper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.util.Log
import android.view.View
import com.Qaabel.org.R
import com.Qaabel.org.helpers.toggleVisiblity
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_location.*

fun MapFragment.checkGps() {
    val manager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    isGpsOn = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

 fun MapFragment.addGpsListener() {
    mGpsSwitchStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "android.location.PROVIDERS_CHANGED") {
                toggleMap()
                if(locationGrated&&isGpsOn&&!NEAR_USER_AVAILABLE){
                    getDeviceLocation()
                    getNearUsers()
                    NEAR_USER_AVAILABLE=true
                }
                initMapAndGpsViews()
            }
        }
    }
}
 fun MapFragment.initMapAndGpsViews() {
    checkGps()
    if (isGpsOn && locationGrated) {
        locationTxtView?.visibility = View.GONE
        currentLocationBtn?.visibility = View.VISIBLE
        go_map?.visibility = View.VISIBLE
        available_num?.visibility = View.VISIBLE
        city_name?.visibility = View.VISIBLE
        onlineView?.visibility = View.VISIBLE
    }
    else {
        warningImg?.toggleVisiblity(false)
        warningTxt?.toggleVisiblity(false)
        onlineView?.toggleVisiblity(false)
        popupRootView?.toggleVisiblity(false)
        currentLocationBtn?.visibility = View.GONE
        locationTxtView?.visibility = View.VISIBLE
        available_num?.visibility = View.GONE
        city_name?.visibility = View.GONE
        go_map?.visibility = View.GONE
    }
}

