package com.Qaabel.org.model.Api.Response

import com.google.android.libraries.places.api.internal.impl.net.pablo.PlaceResult.Geometry.Viewport

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Geometry {
    @SerializedName("location")
    @Expose
    private var location: PlaceLocation? = null
    @SerializedName("viewport")
    @Expose
    private var viewport: Viewport? = null

    fun getLocation(): PlaceLocation? {
        return location
    }

    fun setLocation(location: PlaceLocation?) {
        this.location = location
    }

    fun getViewport(): Viewport? {
        return viewport
    }

    fun setViewport(viewport: Viewport?) {
        this.viewport = viewport
    }
}