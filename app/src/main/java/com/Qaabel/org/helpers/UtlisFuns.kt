package com.Qaabel.org.helpers

import android.app.Activity
import android.app.ActivityManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.location.Location
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.Qaabel.org.model.Api.Response.UsersInPlaceResponse
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

public fun View.toggleVisiblity(show: Boolean){
    if(show)
        this.visibility=View.VISIBLE
    else
        this.visibility=View.GONE



}
public fun Context.isMyServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Int.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}



fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun MapFragment.getUsersInSearchedLocation(){
    var searchedLocation= Location("")
    searchedLocation.latitude=bundleLocation?.latitude!!
    searchedLocation.longitude=bundleLocation?.longitude!!
   nearUsersViewModel?.usersInPlace(mtoken,searchedLocation)?.observe(this, Observer {
       addUsersInThatPlaceToMap(it,bundleLocation!!)
   })
}

fun MapFragment.addUsersInThatPlaceToMap(it: UsersInPlaceResponse?,pos:LatLng) {
    if(it?.users.isNullOrEmpty()){
        var marker= mGoogleMap?.addMarker(MarkerOptions().position(pos)
                .title(searchedText)
                .icon(customMarker?.createPlaceMarker("",it?.numbers!!)))

    }
    else{
        //we are fairly close to this location
    }
}
