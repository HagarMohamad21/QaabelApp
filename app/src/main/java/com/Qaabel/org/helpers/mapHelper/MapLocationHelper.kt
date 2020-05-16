package com.Qaabel.org.helpers.mapHelper

import android.location.Location
import android.util.Log
import android.view.View
import android.widget.Toast
import com.Qaabel.org.helpers.Common
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.fragment_location.*


 fun MapFragment.buildLocationRequest() {
    locationRequest = LocationRequest.create()
    locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    locationRequest?.interval=2000
    locationRequest?.fastestInterval = 1500
    locationRequest?.smallestDisplacement = 4f
}

 fun MapFragment.findDistanceBetweenCurrentLocationAndPlace(location: LatLng?): Float {
    val results = FloatArray(1)
    if(lastLocation!=null)
        Location.distanceBetween(lastLocation?.latitude!!,lastLocation?.longitude!!,location?.latitude!!,location.longitude,results)
    Log.d(TAG, "findDistanceBetweenCurrentLocationAndPlace: ---------------------------------------"+results[0])
    if(isGpsOn){
        if(results[0]>40f){
            warningImg?.visibility= View.VISIBLE
            warningTxt?.visibility= View.VISIBLE
            Common.DISTANCE =results[0]
        }
    }
    return results[0]
}

 fun MapFragment.getDeviceLocation() {


    fusedLocationProviderClient?.lastLocation?.addOnCompleteListener(OnCompleteListener {

        if (it.isSuccessful) {
            GotDeviceLocation=true
            lastLocation = it.result
            Common.USER_LOCATION =lastLocation

            if(bundleLocation!=null)
                findDistanceBetweenCurrentLocationAndPlace(bundleLocation)

            buildLocationRequest()
            buildLocationCallback()
            fusedLocationProviderClient?.requestLocationUpdates(locationRequest,locationCallback,null)
        }

    })
}


 fun MapFragment.buildLocationCallback() {


    Log.d(TAG, "buildLocationCallback: --------------------------------------------------------")
    locationCallback= object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult?) {
            if(p0 == null)
                return
            lastLocation=p0.lastLocation
            Common.USER_LOCATION =lastLocation
            if(firstTimeOpenMap){
                moveCamera(null)
                firstTimeOpenMap=false
            }
            else
                markerAnimation.animate(currentUserMarker, LatLng(lastLocation?.latitude!!,lastLocation?.longitude!!))
        }
    }

}