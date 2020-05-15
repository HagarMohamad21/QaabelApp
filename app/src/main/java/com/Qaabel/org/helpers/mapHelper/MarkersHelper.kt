package com.Qaabel.org.helpers.mapHelper

import android.arch.lifecycle.Observer
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.Qaabel.org.R
import com.Qaabel.org.helpers.Common
import com.Qaabel.org.model.Api.Response.NearPlace
import com.Qaabel.org.model.Api.Response.UsersInPlaceResponse
import com.Qaabel.org.model.SharedPref.AppSharedPrefs
import com.Qaabel.org.model.SharedPref.SharedPref
import com.Qaabel.org.model.Utilities.Utilities
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_location.*

fun MapFragment.addNearUserToMap() {
    var latLng:LatLng
    var marker: Marker
    for(user in mUsers!!){
        Log.d(TAG, "addNearUserToMap: "+user.image)
        latLng= LatLng(user.location.coordinates[0],user.location.coordinates[1])
        if(findDistanceBetweenCurrentLocationAndPlace(latLng)>40){
            Log.d(TAG, "addNearUserToMap: SOME USER IS AWAY BY MORE THAN 10-------------${user.name}")

            if(Common.USER_LOCATION!=null)
                nearUsersViewModel?.nearPlaces(mtoken,lastLocation)?.observe(this, android.arch.lifecycle.Observer {
                    addPlacesToMap(it?.getPlaces())
                    if(it?.getPlaces()?.isEmpty()!!){
                        var marker= mGoogleMap!!.addMarker(MarkerOptions().position(latLng)
                                .title(user.name)
                                .icon(customMarker?.createPlaceMarker("",1)))
                    }
                })
        }

        else{
            var gender=user.sex
            var resourse=if(gender==0) { R.drawable.ic_bluemarker }
            else{
                R.drawable.ic_pinkmarker}

            marker= mGoogleMap!!.addMarker(MarkerOptions().position(latLng)
                    .title(user.name)
                    .icon(customMarker?.createCustomMarker(null,resourse,false)))
            marker.tag=user
            markers[user.username] = marker
            if(user?.image!=defImage){
                downloadMarkerImage.downloadImage(user?.username,user?.image,resourse)
            }
            else nearUsersProfiles[user?.username]=null
        } }
}

fun MapFragment.getUsersInSearchedLocation(){
    var searchedLocation= Location("")
    searchedLocation.latitude=bundleLocation?.latitude!!
    searchedLocation.longitude=bundleLocation?.longitude!!
    nearUsersViewModel?.usersInPlace(mtoken,searchedLocation)?.observe(this, Observer {
        addUsersInThatPlaceToMap(it,bundleLocation!!,searchedLocation)
    })
}

fun MapFragment.addUsersInThatPlaceToMap(it: UsersInPlaceResponse?, pos: LatLng, searchedLocation: Location) {
    available_num.text="${it?.numbers} available"
    if(it?.numbers!!>0){
        if(it?.users.isNullOrEmpty()){
            var marker= mGoogleMap?.addMarker(MarkerOptions().position(pos)
                    .title(searchedText)
                    .icon(customMarker?.createPlaceMarker("", it.numbers)))

        }
    }
    else{
        nearUsersViewModel?.nearPlaces(mtoken,searchedLocation)?.observe(this, Observer {
            addPlacesToMap(it?.getPlaces())
        })
    }


}

fun MapFragment.addPlacesToMap(places: List<NearPlace?>?) {
    Log.d(TAG, "addPlacesToMap: --------------------------------------------"+places?.size)
    for(Place in places!!){
        var numberOfUsers=Place?.getNumberOfUsers()
        var latLng= LatLng(Place?.getGeometry()?.getLocation()?.getLat()!!,Place?.getGeometry()?.getLocation()?.getLng()!!)
        var marker= mGoogleMap?.addMarker(MarkerOptions().position(latLng)
                .title(Place?.getName())
                .icon(customMarker?.createPlaceMarker("",numberOfUsers!!)))

        available_num.text="$numberOfUsers available"


    }
}

fun MapFragment.getNearUsers() {
    isActive.setActive(true)
    nearUsersViewModel!!.isActive(mtoken, isActive).observe(this, android.arch.lifecycle.Observer {

        if(it!=null){
            if(it.getMessage()=="done"){
                if(lastLocation!=null){
                    Log.d(TAG, "getNearUsers: --------------------------------------"+lastLocation!!.longitude)
                    var utilities= Utilities()
                    utilities.setOnLocationSent(this)
                    utilities.SendMyLocation(lastLocation,context!!)

                }

            }

        }})
}