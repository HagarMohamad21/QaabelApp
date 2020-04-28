package com.Qaabel.org.model.Api.Response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class PlacePhoto {
    @SerializedName("height")
    @Expose
    private var height: Int? = null
    @SerializedName("html_attributions")
    @Expose
    private var htmlAttributions: List<String?>? = null
    @SerializedName("photo_reference")
    @Expose
    private var photoReference: String? = null
    @SerializedName("width")
    @Expose
    private var width: Int? = null

    fun getHeight(): Int? {
        return height
    }

    fun setHeight(height: Int?) {
        this.height = height
    }

    fun getHtmlAttributions(): List<String?>? {
        return htmlAttributions
    }

    fun setHtmlAttributions(htmlAttributions: List<String?>?) {
        this.htmlAttributions = htmlAttributions
    }

    fun getPhotoReference(): String? {
        return photoReference
    }

    fun setPhotoReference(photoReference: String?) {
        this.photoReference = photoReference
    }

    fun getWidth(): Int? {
        return width
    }

    fun setWidth(width: Int?) {
        this.width = width
    }
}