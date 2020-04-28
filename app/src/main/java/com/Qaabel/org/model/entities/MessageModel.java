package com.Qaabel.org.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MessageModel
{
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("seenby")
    @Expose
    private List<String> seenby = null;
    @SerializedName("deletedby")
    @Expose
    private List<Object> deletedby = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("message")
    @Expose
    private String message;

    private Boolean isSent;





    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getSeenby() {
        return seenby;
    }

    public void setSeenby(List<String> seenby) {
        this.seenby = seenby;
    }

    public List<Object> getDeletedby() {
        return deletedby;
    }

    public void setDeletedby(List<Object> deletedby) {
        this.deletedby = deletedby;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }
}
