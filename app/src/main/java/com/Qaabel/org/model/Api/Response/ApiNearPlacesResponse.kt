package com.Qaabel.org.model.Api.Response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ApiNearPlacesResponse {
    @SerializedName("status")
    @Expose
    private var status: Int? = null
    @SerializedName("places")
    @Expose
    private var places: List<NearPlace?>? = null

    fun getStatus(): Int? {
        return status
    }

    fun setStatus(status: Int?) {
        this.status = status
    }

    fun getPlaces(): List<NearPlace?>? {
        return places
    }

    fun setPlaces(places: List<NearPlace?>?) {
        this.places = places
    }
}