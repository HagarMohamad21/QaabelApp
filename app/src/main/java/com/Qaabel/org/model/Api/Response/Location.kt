package com.Qaabel.org.model.Api.Response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Location {
    @SerializedName("coordinates")
    @Expose
    private var coordinates: List<Double?>? = null

    fun getCoordinates(): List<Double?>? {
        return coordinates
    }

    fun setCoordinates(coordinates: List<Double?>?) {
        this.coordinates = coordinates
    }

}