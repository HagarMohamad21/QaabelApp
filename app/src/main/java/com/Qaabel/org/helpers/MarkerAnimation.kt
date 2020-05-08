package com.Qaabel.org.helpers

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

class MarkerAnimation {

    fun animate(){
        var valueAnimation=ValueAnimator.ofInt(0,100)
        valueAnimation.duration=2000
        valueAnimation.interpolator=LinearInterpolator()
        valueAnimation.addUpdateListener {

        }
    }
}