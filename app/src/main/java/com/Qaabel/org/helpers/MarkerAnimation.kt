package com.Qaabel.org.helpers

import android.animation.ValueAnimator
import android.widget.Toast
import com.Qaabel.org.helpers.mapHelper.moveCamera
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

import java.lang.Math.*


class MarkerAnimation(var latLngInterpolator: LatLngInterpolator,var mapFragment: MapFragment){

    fun animate(marker:Marker?,endPosition:LatLng){
        val startPosition = marker?.position

        val valueAnimator = ValueAnimator()
        valueAnimator.addUpdateListener { animation ->
            val v = animation.animatedFraction
            val newPosition = latLngInterpolator.interpolate(v, startPosition!!, endPosition)
            marker.position = newPosition!!
            mapFragment.moveCamera(null,computeHeading(startPosition,endPosition))
        }
        valueAnimator.setFloatValues(0f, 1f) // Ignored.
        valueAnimator.duration = 3000
        valueAnimator.start()
        }


    fun computeHeading(from: LatLng, to: LatLng): Double { // http://williams.best.vwh.net/avform.htm#Crs
        val fromLat: Double = toRadians(from.latitude)
        val fromLng: Double = toRadians(from.longitude)
        val toLat: Double = toRadians(to.latitude)
        val toLng: Double = toRadians(to.longitude)
        val dLng = toLng - fromLng
        val heading: Double = atan2(
                sin(dLng) * cos(toLat),
                cos(fromLat) * sin(toLat) - sin(fromLat) * cos(toLat) * cos(dLng))
        return wrap(toDegrees(heading), -180.0, 180.0)
    }
    private fun wrap(n: Double, min: Double, max: Double): Double {
        return if (n >= min && n < max) n else mod(n - min, max - min) + min
    }

    private fun mod(x: Double, m: Double): Double {
        return (x % m + m) % m
    }
    }
