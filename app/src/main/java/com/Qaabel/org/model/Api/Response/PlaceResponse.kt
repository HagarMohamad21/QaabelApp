package com.Qaabel.org.model.Api.Response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class PlaceResponse {
    @SerializedName("candidates")
    @Expose
    private var candidates: List<CandidatePlace>? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null

    fun getCandidates(): List<CandidatePlace>? {
        return candidates
    }

    fun setCandidates(candidates: List<CandidatePlace>?) {
        this.candidates = candidates
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }
}