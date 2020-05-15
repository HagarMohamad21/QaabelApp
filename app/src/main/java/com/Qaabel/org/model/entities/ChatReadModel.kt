package com.Qaabel.org.model.entities


import com.google.gson.annotations.SerializedName

data class ChatReadModel(
    @SerializedName("chat_id")
    val chatId: String,
    @SerializedName("unreaded")
    val unreaded: Int
)