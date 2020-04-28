package com.Qaabel.org.helpers

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import java.io.IOException
import java.util.*

class LocationsHelper {
     fun getAdrress(location: Location,geocoder: Geocoder): String? {
        try {

            var addresses: List<Address>? = null
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val cityName = addresses[0].getAddressLine(0)
            val stateName = addresses[0].getAddressLine(1)
            val countryName = addresses[0].getAddressLine(2)
            return addresses?.get(0)?.adminArea?.replace("Governorate", "") ?: "Current Location "
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "Current Location"
    }
    private val TAG = "LocationsHelper"
    fun getCountryName(context: Context?,location:Location?): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address>? = null
         try {

            Log.d(TAG, "getCountryName: ---------------TRY")
            if(location!=null){
                Log.d(TAG, "getCountryName: --------------NOT NULL------")
                Log.d(TAG, "getCountryName: "+location.latitude+"  "+location.longitude)
                addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)   
            }
           
            if (addresses != null && addresses.isNotEmpty()) {
               var countryName= addresses[0].countryName.substring(0,2).toLowerCase()
                Log.d(TAG, "getCountryName: ----------------------------"+countryName).toString()

                return countryName
            } else return null
        } catch (ignored: IOException) { //do something
            return null
        }
    }

    fun getLocationBias(location:Location):String{
        if(location==null) return ""
            var lat=location.latitude
            var lng=location.longitude
         return "$lat,$lng"
    }



}