package com.Qaabel.org.model.Api.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.Qaabel.org.model.entities.FriendModel;

import java.util.List;

public class ApiFreindsResponse
{
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("numbers")
    @Expose
    private Integer numbers;
    @SerializedName("friends")
    @Expose
    private List<FriendModel> friends = null;
    @SerializedName("flashes")
    @Expose
    private List<FriendModel> flashes = null;
    @SerializedName("blocks")
    @Expose
    private List<FriendModel> blocks = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getNumbers()
    {
        return numbers;
    }

    public void setNumbers(Integer numbers)
    {
        this.numbers = numbers;
    }

    public List<FriendModel> getUsers()
    {
        return friends;
    }

    public void setUsers(List<FriendModel> users)
    {
        this.friends = users;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<FriendModel> getFriends()
    {
        return friends;
    }

    public List<FriendModel> getBlocks()
    {
        return blocks;
    }

    public void setBlocks(List<FriendModel> blocks)
    {
        this.blocks = blocks;
    }

    public void setFriends(List<FriendModel> friends)
    {
        this.friends = friends;
    }

    public List<FriendModel> getFlashes()
    {
        return flashes;
    }

    public void setFlashes(List<FriendModel> flashes)
    {
        this.flashes = flashes;
    }
}
