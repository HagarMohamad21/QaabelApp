package com.Qaabel.org.model.Api.Response;

import com.Qaabel.org.model.entities.MessageModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageApiResponse
{
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("messages")
    @Expose
    private List<MessageModel> messages = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public List<MessageModel> getMessages()
    {
        return messages;
    }

    public void setMessages(List<MessageModel> messages)
    {
        this.messages = messages;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
