package com.Qaabel.org.helpers

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class TimeUtils {
    private val TAG = "TimeUtils"
    //take a date and format it to get date and time
    fun getFormattedDate(timeStamp:String):String{

        var timeStamp=timeStamp.replace('T',' ',false)
        var mipulatedString=timeStamp.replace('Z',' ',false)
        var  time=mipulatedString.substring(11,16)
        var date=mipulatedString.substring(0,10)
        Log.d(TAG, "getFormattedDate: ----------------------------"+time)
        Log.d(TAG, "getFormattedDate: ------------------------------"+date)
        return date
//        //val timeFormatted: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//        val formattedDate: String = sdf.format(Date())


    }


}