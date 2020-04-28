package com.Qaabel.org.model.entities

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Chat {
    @SerializedName("_id")
    @Expose
    private var id: String? = null
    @SerializedName("last")
    @Expose
    private var last: LastChat? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getLast(): LastChat? {
        return last
    }

    fun setLast(last: LastChat?) {
        this.last = last
    }

}