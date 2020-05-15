package com.Qaabel.org.helpers

import android.app.Activity
import android.app.ActivityManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.location.Location
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.Qaabel.org.model.Api.Response.NearPlace
import com.Qaabel.org.model.Api.Response.UsersInPlaceResponse
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_location.*

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


