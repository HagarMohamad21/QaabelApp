package com.Qaabel.org.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatModel implements Serializable
{
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("chat_id")
    @Expose
    private String chat_id;
    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("user")
    @Expose
    private FriendModel user;
    @SerializedName("chat")
    @Expose
    private MessageModel chat;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public FriendModel getUser()
    {
        return user;
    }

    public void setUser(FriendModel user)
    {
        this.user = user;
    }

    public MessageModel getChat()
    {
        return chat;
    }

    public void setChat(MessageModel chat)
    {
        this.chat = chat;
    }

    public String getChat_id()
    {
        return chat_id;
    }

    public void setChat_id(String chat_id)
    {
        this.chat_id = chat_id;
    }

    public String getPage()
    {
        return page;
    }

    public void setPage(String page)
    {
        this.page = page;
    }
}
