package com.Qaabel.org.helpers

import android.util.Log
import android.view.View

public fun View.toggleVisiblity(show: Boolean){
    if(show)
        this.visibility=View.VISIBLE
    else
        this.visibility=View.GONE

}
