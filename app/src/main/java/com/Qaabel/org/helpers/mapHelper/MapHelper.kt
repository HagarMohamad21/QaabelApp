package com.Qaabel.org.helpers.mapHelper

import android.location.Geocoder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.Qaabel.org.R
import com.Qaabel.org.helpers.LocationsHelper
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.*

fun MapFragment.toggleMap() {

    checkGps()
    if (locationGrated && isGpsOn) {
        Log.d(TAG, "toggleMap: TRUE")
        mapFragment?.view?.visibility = View.VISIBLE
    } else {
        mapFragment?.view?.visibility = View.GONE
        Log.d(TAG, "toggleMap: FASLE")
    }
}

fun MapFragment.setupMap() {
    mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
    mapFragment?.getMapAsync(this)
}

fun MapFragment.moveCamera(location: LatLng?, bearing: Double = 0.0) {
    Log.d(TAG, "moveCamera: --------------------------------------")
    val cameraUpdate: CameraUpdate
    if (location == null && lastLocation != null) {
        var lastLocationLatLang = LatLng(lastLocation!!.latitude, lastLocation!!.longitude)
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(lastLocationLatLang, DEFAULT_ZOOM)
        if (currentUserMarker == null) {
            if (currentUser != null) {
                if (currentUser?.image != defImage && currentUser?.image != null) {
                    downloadMarkerImage.downloadImage("Me", currentUser?.image!!, -1)
                } else
                    nearUsersProfiles["Me"] = null
                currentUserMarker = mGoogleMap?.addMarker(MarkerOptions().icon(customMarker
                        ?.createCustomMarker(null, -1, false))
                        .title("Me")
                        .flat(true)
                        .position(lastLocationLatLang))
                currentUserMarker?.tag = null
                markers["Me"] = currentUserMarker

            } else {
                Log.d(TAG, "moveCamera: -----------CURRENT USER IS NULL")

            }

        } else {
            Log.d(TAG, "moveCamera:currentUserMarker -------------------not null ")
        }
        if (bundle == null)
            mGoogleMap?.moveCamera(cameraUpdate)
        else if (currentLocationBtnClicked) {
            mGoogleMap?.moveCamera(cameraUpdate)
            currentLocationBtnClicked = false
        }
    } else {
        Log.d(TAG, "moveCamera: -----------ELSE--------------------")
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM)
        mGoogleMap?.moveCamera(cameraUpdate)
    }
    if (lastLocation != null)
        initAddressText()
}


 fun MapFragment.initAddressText() {

    if (searchedText != "") {
        city_name?.text = searchedText
    }
    else {
       activity?.apply {
           val geocoder = Geocoder(activity, Locale.ENGLISH)

           if (city_name != null && lastLocation != null){
               userCity=LocationsHelper().getAdrress(lastLocation!!, geocoder)

           }
           city_name?.text = userCity
       }


    }


}