package com.Qaabel.org.model.entities

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class SentMessageModel {
    @SerializedName("chat_id")
    @Expose
    private var chatId: String? = null
    @SerializedName("message")
    @Expose
    private var message: String? = null

    fun getChatId(): String? {
        return chatId
    }

    fun setChatId(chatId: String?) {
        this.chatId = chatId
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }
}