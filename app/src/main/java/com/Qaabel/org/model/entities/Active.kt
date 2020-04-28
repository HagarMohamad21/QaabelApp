package com.Qaabel.org.model.entities

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Active {
    @SerializedName("active")
    @Expose
    private var active: Boolean? = null

    fun getActive(): Boolean? {
        return active
    }

    fun setActive(active: Boolean?) {
        this.active = active
    }
}