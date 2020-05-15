package com.Qaabel.org.helpers.mapHelper

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment

fun MapFragment.requestPermission() {
    locationGrated = if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        false
    } else {
        true
    }
    toggleMap()
}