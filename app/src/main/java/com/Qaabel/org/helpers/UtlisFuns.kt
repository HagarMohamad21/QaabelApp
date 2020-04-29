package com.Qaabel.org.helpers

import android.app.ActivityManager
import android.content.Context
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.view.View
import com.Qaabel.org.view.activity.MainActivity

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
