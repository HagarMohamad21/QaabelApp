package com.Qaabel.org.model.entities

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class LastChat {
    @SerializedName("time")
    @Expose
    private var time: String? = null
    @SerializedName("seenby")
    @Expose
    private var seenby: List<String?>? = null
    @SerializedName("deletedby")
    @Expose
    private var deletedby: List<Any?>? = null
    @SerializedName("_id")
    @Expose
    private var id: String? = null
    @SerializedName("sender")
    @Expose
    private var sender: String? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null

    fun getTime(): String? {
        return time
    }

    fun setTime(time: String?) {
        this.time = time
    }

    fun getSeenby(): List<String?>? {
        return seenby
    }

    fun setSeenby(seenby: List<String?>?) {
        this.seenby = seenby
    }

    fun getDeletedby(): List<Any?>? {
        return deletedby
    }

    fun setDeletedby(deletedby: List<Any?>?) {
        this.deletedby = deletedby
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getSender(): String? {
        return sender
    }

    fun setSender(sender: String?) {
        this.sender = sender
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }
}