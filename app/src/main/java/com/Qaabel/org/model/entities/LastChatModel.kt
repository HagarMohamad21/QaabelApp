package com.Qaabel.org.model.entities

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class LastChatModel {
    @SerializedName("deleted")
    @Expose
    private var deleted: Boolean? = null
    @SerializedName("unread")
    @Expose
    private var unread: Int? = null
    @SerializedName("_id")
    @Expose
    private var id: String? = null
    @SerializedName("user")
    @Expose
    private var user: FriendModel? = null
    @SerializedName("chat")
    @Expose
    private var chat: Chat? = null

    fun getDeleted(): Boolean? {
        return deleted
    }

    fun setDeleted(deleted: Boolean?) {
        this.deleted = deleted
    }

    fun getUnread(): Int? {
        return unread
    }

    fun setUnread(unread: Int?) {
        this.unread = unread
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getUser(): FriendModel? {
        return user
    }

    fun setUser(user: FriendModel?) {
        this.user = user
    }

    fun getChat(): Chat? {
        return chat
    }

    fun setChat(chat: Chat?) {
        this.chat = chat
    }

}