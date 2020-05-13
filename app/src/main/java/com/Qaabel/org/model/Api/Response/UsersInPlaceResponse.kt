package com.Qaabel.org.model.Api.Response


import com.Qaabel.org.model.entities.FriendModel
import com.google.gson.annotations.SerializedName

data class UsersInPlaceResponse(
    @SerializedName("numbers")
    val numbers: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("users")
    val users: List<FriendModel>
)