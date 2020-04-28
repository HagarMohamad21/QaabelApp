package com.Qaabel.org.model.Api.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.Qaabel.org.model.entities.FriendModel;

import java.util.List;

public class ApiNearUsersResponse
{
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("numbers")
    @Expose
    private Integer numbers;
    @SerializedName("users")
    @Expose
    private List<FriendModel> users = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer numbers) {
        this.numbers = numbers;
    }

    public List<FriendModel> getUsers() {
        return users;
    }

    public void setUsers(List<FriendModel> users) {
        this.users = users;
    }
}
