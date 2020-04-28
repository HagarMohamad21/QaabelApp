package com.Qaabel.org.interfaces

import com.Qaabel.org.model.entities.FriendModel

interface RequestNavigation {
    fun onNavigationRequested(des:String,friendModel: FriendModel)
}