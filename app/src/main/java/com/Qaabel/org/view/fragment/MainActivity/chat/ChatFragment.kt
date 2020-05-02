package com.Qaabel.org.view.fragment.MainActivity.chat

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.Qaabel.org.R
import com.Qaabel.org.helpers.Common
import com.Qaabel.org.helpers.toggleVisiblity
import com.Qaabel.org.model.SharedPref.AppSharedPrefs
import com.Qaabel.org.model.SharedPref.SharedPref
import com.Qaabel.org.model.Utilities.Utilities
import com.Qaabel.org.model.entities.*
import com.Qaabel.org.view.adapter.Recycler.MessagesAdapter
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.dialog_edit.*
import kotlinx.android.synthetic.main.fragment_chat.*
import java.lang.NullPointerException
import java.net.URISyntaxException
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {
    private val TAG = "ChatFragment"
    private var isConnected=false
    lateinit var vw:View
    var token=""
    var friendProfileViewModel:FriendProfileViewModel?=null
    var user:FriendModel?=null
    var chatId:String?=null
    var currentUser:UserModel?=null
    var adapter:MessagesAdapter?=null
    lateinit var mMessageReceiver:BroadcastReceiver

    private lateinit var mSocket: Socket

    companion object{
        var visible=false
    }


    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(context!!).registerReceiver(
                mMessageReceiver,  IntentFilter(Common.NEW_MESSAGE_FILTER))

    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(mMessageReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        friendProfileViewModel=ViewModelProviders.of(this).get(FriendProfileViewModel::class.java)
        token=SharedPref(context).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
        currentUser=SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER)
       initBroadCast()

    }

    override fun onResume() {
        visible=true
        super.onResume()
    }

    override fun onPause() {
        visible=false
        super.onPause()
    }
     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: -------------------------")
  vw = inflater.inflate(R.layout.fragment_chat, container, false)
        return vw
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()

    }

    private fun setListeners() {

        sendBtn.setOnClickListener {
            if(message_et.text.isNotEmpty()){
                sendMessage(message_et.text.toString())
            }
        }
        back_img.setOnClickListener { fragmentManager?.popBackStack() }

        profileImage.setOnClickListener {
            openProfile() }
        nameTxt.setOnClickListener { openProfile() }

        message_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG, "onTextChanged: ----------------------------------------------------"+s)
               // mSocket.emit("isTyping")
             if(s.isNullOrEmpty()){

             }
                else{

             }
            }

        })
    }


    private fun openProfile() {
        var bundle=Bundle()
        bundle.putParcelable("FRIEND_USER",user)
        bundle.putString("SOURCE","MESSAGES")
        if(user==null) return
        Navigation.findNavController(activity!!,R.id.shopping_nav_host_fragment).navigate(R.id.action_navigation_chat_to_navigation_user_profile,bundle)
    }

    private fun sendMessage(message: String) {
        message_et.text.clear()
        if(chatId!=null){
            var messageModel=SentMessageModel()
            messageModel.setChatId(chatId)
            messageModel.setMessage(message)
            friendProfileViewModel?.sendMessage(token,messageModel)?.observe(this, Observer {

                if(it?.status=="200"){
                   buildMessage(message)
                }
            })
        }

        else{
            //handle message not sent and these stuff
        }

    }

    private fun buildMessage(message: String) {
       var messageModel=MessageModel()
        messageModel.sender=currentUser?._id
        messageModel.message=message
       adapter?.addNewMessage(messageModel)
        try {
            chatlist?.scrollToPosition(adapter?.itemCount!!-1)
        }

        catch (e:NullPointerException){
            Log.d(TAG, "buildMessage: --------------------CHAT LIST RECYCLERVIEW---------------------"+chatlist)
            Log.d(TAG, "buildMessage: -----------------------ADAPTER------------------"+adapter)
            Log.d(TAG, "buildMessage: ---------------------------ITEM COUNT-------------------"+adapter?.itemCount)
        }
    }

    private fun init(){
   user=arguments?.getParcelable("FRIEND_USER")
   chatId=arguments?.getString("CHAT_ID")
   Picasso.get().load(user?.image).into(profileImage)
   nameTxt.text=user?.name
   getMessages()
}

    private fun getMessages() {
        if(chatId==null)
        friendProfileViewModel?.getUserChat(token,user?.id)?.observe(this, Observer { it ->
            if(it?.getStatus()==200){
             chatId= it.getChatId().toString()
              getChats()
          }
        })
        else getChats()

    }

    private fun getChats() {
        friendProfileViewModel?.getMessages(token,chatId)?.observe(this, Observer {

            if(it?.message=="done"){
                if(it.messages.isNotEmpty()){
                    populateList(it.messages as ArrayList<MessageModel>)
                }
            }
        })
    }


    private fun populateList(messages: ArrayList<MessageModel>) {
        messages.reverse()
         adapter=MessagesAdapter(context!!,messages,currentUser!!)
         var linearLayoutManager=LinearLayoutManager(context)
         linearLayoutManager.stackFromEnd = true
        chatlist.layoutManager=linearLayoutManager
         chatlist.adapter=adapter

    }


    private fun initBroadCast() {

        mMessageReceiver=object: BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                    val message:SocketModel= intent?.getParcelableExtra(Common.SERVICE_CHAT_MESSAGE) as SocketModel
                    adapter?.addNewMessage(message.getMessage())
                    chatlist.scrollToPosition(adapter?.itemCount!!-1)

            }
        }
    }

}


