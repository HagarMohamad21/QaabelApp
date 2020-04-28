package com.Qaabel.org.model.entities;

public class NotificationModel
{
    String type;
    FriendModel friend;

    public NotificationModel(String type, FriendModel friend)
    {
        this.type = type;
        this.friend = friend;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public FriendModel getFriend()
    {
        return friend;
    }

    public void setFriend(FriendModel friend)
    {
        this.friend = friend;
    }
}
