package com.Qaabel.org.helpers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import com.Qaabel.org.R
import com.Qaabel.org.view.fragment.MainActivity.home.MapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.android.synthetic.main.custom_marker.view.*
import kotlinx.android.synthetic.main.custom_place_marker.view.*


class CustomMarker(var mapFragment: MapFragment) {
    private val TAG = "CustomMarker"



    fun createCustomMarker(image:Bitmap?,markerColor: Int,showNotification:Boolean): BitmapDescriptor? {
        if(mapFragment.context!=null){
            val markerView=LayoutInflater.from(mapFragment.context).inflate(R.layout.custom_marker,null)
            var profileImage=markerView.profileImage
            var markerImage=markerView.markerImage
            markerView.notificationDot.toggleVisiblity(showNotification)
            if(image!=null){
                profileImage.setImageBitmap(image)
            }

            if(markerColor!=-1){
                markerImage.setImageResource(markerColor)
            }
            val displayMetrics = DisplayMetrics()
            markerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            markerView.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
            markerView.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
            markerView.buildDrawingCache()
            val bitmap = Bitmap.createBitmap(markerView.measuredWidth, markerView.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            markerView.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

       else return null
    }

    fun createPlaceMarker(image: String?,numberOfUser:Int) :BitmapDescriptor{
        val markerView=LayoutInflater.from(mapFragment.context).inflate(R.layout.custom_place_marker,null)
        markerView?.numUsers?.text= numberOfUser.toString()
        val displayMetrics=DisplayMetrics()
        markerView?.layoutParams=ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        markerView?.measure(displayMetrics.widthPixels,displayMetrics.heightPixels)
        markerView?.layout(0,0,displayMetrics.widthPixels,displayMetrics.heightPixels)
        markerView?.buildDrawingCache()
        val bitmap=Bitmap.createBitmap(markerView.measuredWidth,markerView.measuredHeight,Bitmap.Config.ARGB_8888)
        val canvas=Canvas(bitmap)
        markerView.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)


    }


//    private fun getImage(context: Context, profileImage: ImageView,imageUrl:String) {
//        Log.d(TAG, "getImage: --------------------------------")
//        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_uil_user).into(profileImage,object:Callback{
//            override fun onSuccess() {
//
//                Handler().postDelayed({
//                    var bitmap=((profileImage.drawable) as BitmapDrawable).bitmap
//                    Log.d(TAG, "onSuccess: ---------------------"+bitmap)
//                    profileImage.setImageBitmap(bitmap)
//                }, 100)
//            }
//
//            override fun onError(e: Exception?) {
//                Log.d(TAG, "onError: ------------------------------")
//            }
//        })
//    }



}
