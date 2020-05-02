package com.Qaabel.org.model.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class SocketModel() :Parcelable{
    @SerializedName("user")
    @Expose
    private var user: FriendModel? = null
    @SerializedName("message")
    @Expose
    private var message: MessageModel? = null

    constructor(parcel: Parcel) : this() {
        user = parcel.readParcelable(FriendModel::class.java.classLoader)
    }

    fun getUser(): FriendModel? {
        return user
    }

    fun setUser(user: FriendModel?) {
        this.user = user
    }

    fun getMessage(): MessageModel? {
        return message
    }

    fun setMessage(message: MessageModel?) {
        this.message = message
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object CREATOR : Parcelable.Creator<SocketModel> {
        override fun createFromParcel(parcel: Parcel): SocketModel {
            return SocketModel(parcel)
        }

        override fun newArray(size: Int): Array<SocketModel?> {
            return arrayOfNulls(size)
        }
    }
}