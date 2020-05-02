package com.Qaabel.org.helpers

import android.location.Location
import java.util.*

class Common {
    companion object{
        var DISTANCE: Float=-1f
        var USER_LOCATION: Location?=null
        fun getAge(year: Int, month: Int, day: Int): String? {
            val dob: Calendar = Calendar.getInstance()
            val today: Calendar = Calendar.getInstance()
            dob.set(year, month, day)
            var age: Int = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            val ageInt = age
            return ageInt.toString()
        }

        val NotificationType_FLASH="FLASH"
        val NotificationType_MESSAGE="MESSAGE"
        val SERVICE_MESSAGE="SERVICE_MESSAGE"
        val SERVICE_USER="USER"
        val NEW_FLASH_FILTER="NEW_FLASH"
        val SERVICE_CHAT_MESSAGE="MESSAGE"
        val NEW_MESSAGE_FILTER="NEW_MESSAGE"


    }
}