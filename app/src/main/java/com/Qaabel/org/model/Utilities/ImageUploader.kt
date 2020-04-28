package com.Qaabel.org.model.Utilities

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import com.Qaabel.org.viewModel.viewModel.account.CompleteDataViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File


class ImageUploader {
    companion object{
         fun createFileUploaded(file: File?): MultipartBody.Part? {
            var fileUploaded: MultipartBody.Part? = null
            if (file != null) {
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                fileUploaded = MultipartBody.Part.createFormData("image", "image", requestFile)
            }
            return fileUploaded
        }
        }


    }







