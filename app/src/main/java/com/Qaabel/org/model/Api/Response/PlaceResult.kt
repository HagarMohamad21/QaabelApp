package com.Qaabel.org.model.Api.Response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class PlaceResult {
    @SerializedName("formatted_address")
    @Expose
    private var formattedAddress: String? = null
    fun getFormattedAddress(): String? {
        return formattedAddress
    }

    fun setFormattedAddress(formattedAddress: String?) {
        this.formattedAddress = formattedAddress
    }
    @SerializedName("geometry")
    @Expose
    private var geometry: Geometry? = null
    @SerializedName("icon")
    @Expose
    private var icon: String? = null
    @SerializedName("id")
    @Expose
    private var id: String? = null
    @SerializedName("name")
    @Expose
    private var name: String? = null
    @SerializedName("place_id")
    @Expose
    private var placeId: String? = null


    fun getGeometry(): Geometry? {
        return geometry
    }

    fun setGeometry(geometry: Geometry?) {
        this.geometry = geometry
    }

    fun getIcon(): String? {
        return icon
    }

    fun setIcon(icon: String?) {
        this.icon = icon
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getPlaceId(): String? {
        return placeId
    }

    fun setPlaceId(placeId: String?) {
        this.placeId = placeId
    }

}