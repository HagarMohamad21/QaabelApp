package com.Qaabel.org.model.Api.Response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class PlacesResponse {
    @SerializedName("html_attributions")
    @Expose
    private var htmlAttributions: List<Any?>? = null
    @SerializedName("results")
    @Expose
    private var results: List<PlaceResult?>? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null

    fun getHtmlAttributions(): List<Any?>? {
        return htmlAttributions
    }

    fun setHtmlAttributions(htmlAttributions: List<Any?>?) {
        this.htmlAttributions = htmlAttributions
    }

    fun getResults(): List<PlaceResult?>? {
        return results
    }

    fun setResults(results: List<PlaceResult?>?) {
        this.results = results
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }

}