package com.Qaabel.org.view.adapter.Recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.Qaabel.org.R
import com.Qaabel.org.model.entities.MessageModel
import com.Qaabel.org.model.entities.UserModel
import kotlinx.android.synthetic.main.recieved_message_list_item.view.*
import kotlinx.android.synthetic.main.sent_message_list_item.view.*


class MessagesAdapter(var context:Context,var messages :ArrayList<MessageModel>,var currentUser:UserModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val VIEW_TYPE_SENDER = 1
    private val VIEW_TYPE_RECEIVER = 2




    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "onCreateViewHolder: ---------------------------------")
        var vw:View?=null
        vw = if(p1==VIEW_TYPE_SENDER){
            LayoutInflater.from(context).inflate(R.layout.sent_message_list_item,p0,false)
        }
        else{
            LayoutInflater.from(context).inflate(R.layout.recieved_message_list_item,p0,false)

        }
        return MessageHolder(vw)
    }

    override fun getItemCount()=messages.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        (p0 as MessageHolder).bind(messages[p1],p0.itemViewType)
    }

    override fun getItemViewType(pos: Int): Int {
        return if (checkIfSender(currentUser._id,messages[pos].sender)) {
            VIEW_TYPE_SENDER
        } else VIEW_TYPE_RECEIVER
    }

    private fun checkIfSender(userId: String?, sender: String?): Boolean {
        //Log.d(TAG, "checkIfSender: ###################################"+userId)
        //Log.d(TAG, "checkIfSender: ###################################"+sender)
        return userId==sender
    }

    private val TAG = "MessagesAdapter"
    inner class MessageHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bind(pos:MessageModel,viewType:Int){
            if(viewType==VIEW_TYPE_SENDER){

                itemView.sentMessageTxt.text=pos.message
            }
            else if (viewType==VIEW_TYPE_RECEIVER){
                itemView.receivedMessageTxt.text=pos.message
            }
        }
    }

    fun addNewMessage(message:MessageModel?){
        messages.add(message!!)
        notifyDataSetChanged()
    }
}
