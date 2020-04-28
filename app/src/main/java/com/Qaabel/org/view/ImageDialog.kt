package com.Qaabel.org.view

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.Qaabel.org.R
import kotlinx.android.synthetic.main.choose_image_dialog.*
import java.io.ByteArrayOutputStream
import java.io.IOException


class ImageDialog : DialogFragment(){
    interface ImageDialogDismissListener{
        fun onDismiss(action:String)
    }


    lateinit var imageDialogDismissListener:ImageDialogDismissListener



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.choose_image_dialog,null,false)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE);
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
    }
    private fun setListeners() {
        closeBtn.setOnClickListener { dismiss() }
        cameraTxt.setOnClickListener {if(imageDialogDismissListener!=null)
            imageDialogDismissListener.onDismiss("CAMERA")
        dismiss()}
        galleryTxt.setOnClickListener {  if(imageDialogDismissListener!=null)
            imageDialogDismissListener.onDismiss("GALLERY")
        dismiss()}
    }




    public fun openGallery() :Intent?{
        var imageIntent=Intent(Intent.ACTION_PICK)
        imageIntent.type = "image/*"
        var mimeTypes= arrayOf("image/jpeg","image/png")
        imageIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes)
        if(imageIntent.resolveActivity(context!!.packageManager)!=null)
        return imageIntent
        else
            return null
        }
    public fun openCamera() :Intent?{
     var ImageIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(ImageIntent.resolveActivity(context!!.packageManager)!=null)
            return ImageIntent
        else return null
    }

}