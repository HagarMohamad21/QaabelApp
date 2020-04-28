package com.Qaabel.org.model.Api.Response;

import com.Qaabel.org.model.entities.FriendModel;
import com.Qaabel.org.model.entities.MessageModel;

import org.json.JSONException;
import org.json.JSONObject;

public class SocketMessageModel
{
    FriendModel friend;
    MessageModel message;

    public SocketMessageModel(JSONObject json)
    {
//        try
//        {
//            //this.friend = new FriendModel(json.getJSONObject("user"));
//           // this.message = new MessageModel(json.getJSONObject("message"));
//        }
//        catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
    }

    public FriendModel getFriend()
    {
        return friend;
    }

    public void setFriend(FriendModel friend)
    {
        this.friend = friend;
    }

    public MessageModel getMessage()
    {
        return message;
    }

    public void setMessage(MessageModel message)
    {
        this.message = message;
    }
}
