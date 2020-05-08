package com.Qaabel.org.helpers

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DownloadMarkerImage(var mapFragment: MapFragment) {
    private val TAG = "DownloadMarkerImage"
    @SuppressLint("CheckResult")

    fun downloadImage(userName: String, url: String, resourse: Int){
        getBitmapSingle(Picasso.get(), url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bitmap ->
                 mapFragment.markers[userName]?.setIcon(CustomMarker(mapFragment).createCustomMarker(bitmap,resourse,false))
              mapFragment.nearUsersProfiles[userName] = bitmap
                }, Throwable::printStackTrace)
    }


    fun getBitmapSingle(picasso: Picasso, url: String): Single<Bitmap> = Single.create {
        try {
            if (!it.isDisposed) {
                val bitmap: Bitmap = picasso.load(url).get()
                it.onSuccess(bitmap)
            }
        } catch (e: Throwable) {
            it.onError(e)
        }
    }


}