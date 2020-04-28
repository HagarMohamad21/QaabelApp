package com.Qaabel.org.model.Sevice;


import org.json.JSONObject;


public interface OnSocketListener
{
    void doAfterReceiveSocketData(MSocket.SocketMassageType socketMassageType, JSONObject jsonObject);

    void notificationRecieved(JSONObject jsonObject);

    void chatMessageRecieved(JSONObject jsonObject);
}
