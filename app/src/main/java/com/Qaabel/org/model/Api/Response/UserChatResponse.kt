package com.Qaabel.org.model.Api.Response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class UserChatResponse {
    @SerializedName("status")
    @Expose
    private var status: Int? = null
    @SerializedName("chatId")
    @Expose
    private var chatId: String? = null

    fun getStatus(): Int? {
        return status
    }

    fun setStatus(status: Int?) {
        this.status = status
    }

    fun getChatId(): String? {
        return chatId
    }

    fun setChatId(chatId: String?) {
        this.chatId = chatId
    }
}