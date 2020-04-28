package com.Qaabel.org.model.Api.Response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class PlaceLocation {
    @SerializedName("lat")
    @Expose
    private var lat: Double? = null
    @SerializedName("lng")
    @Expose
    private var lng: Double? = null

    fun getLat(): Double? {
        return lat
    }

    fun setLat(lat: Double?) {
        this.lat = lat
    }

    fun getLng(): Double? {
        return lng
    }

    fun setLng(lng: Double?) {
        this.lng = lng
    }
}