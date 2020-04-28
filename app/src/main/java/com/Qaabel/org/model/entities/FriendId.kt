package com.Qaabel.org.model.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FriendId {
    @SerializedName("user_id")
    @Expose
     var user_id: String? = null
}