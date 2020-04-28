package com.Qaabel.org.model.Api.Response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CandidatePlace {
    @SerializedName("formatted_address")
    @Expose
    private var formattedAddress: String? = null
    @SerializedName("name")
    @Expose
    private var name: String? = null

    @SerializedName("geometry")
    @Expose
    private var geometry: Geometry? = null

    fun getGeometry(): Geometry? {
        return geometry
    }

    fun setGeometry(geometry: Geometry?) {
        this.geometry = geometry
    }
    fun getFormattedAddress(): String? {
        return formattedAddress
    }

    fun setFormattedAddress(formattedAddress: String?) {
        this.formattedAddress = formattedAddress
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

}