package com.Qaabel.org.model.entities

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class SocketModel{
    @SerializedName("user")
    @Expose
    private var user: FriendModel? = null
    @SerializedName("message")
    @Expose
    private var message: MessageModel? = null

    fun getUser(): FriendModel? {
        return user
    }

    fun setUser(user: FriendModel?) {
        this.user = user
    }

    fun getMessage(): MessageModel? {
        return message
    }

    fun setMessage(message: MessageModel?) {
        this.message = message
    }
}