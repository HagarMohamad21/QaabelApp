package com.Qaabel.org.model.Api.Response;

import com.Qaabel.org.model.entities.ChatModel;
import com.Qaabel.org.model.entities.LastChatModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiChatResponse
{
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("chats")
    @Expose
    private List<LastChatModel> chats = null;

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public List<LastChatModel> getChats()
    {
        return chats;
    }

    public void setChats(List<LastChatModel> chats)
    {
        this.chats = chats;
    }
}
