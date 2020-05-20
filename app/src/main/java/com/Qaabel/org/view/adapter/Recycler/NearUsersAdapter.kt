package com.Qaabel.org.view.adapter.Recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.Qaabel.org.R
import com.Qaabel.org.helpers.Common
import com.Qaabel.org.helpers.toggleVisiblity
import com.Qaabel.org.interfaces.OnFlashBack
import com.Qaabel.org.interfaces.OnFlashedSent
import com.Qaabel.org.interfaces.RequestNavigation
import com.Qaabel.org.model.SharedPref.AppSharedPrefs
import com.Qaabel.org.model.SharedPref.SharedPref
import com.Qaabel.org.model.entities.FriendModel
import com.Qaabel.org.view.fragment.MainActivity.home.NearUsersListFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_friend_flash.view.*
import kotlinx.android.synthetic.main.card_friend_flash.view.flashBackBtn
import kotlinx.android.synthetic.main.card_friend_flash.view.flashBtn



class NearUsersAdapter(var context: Context,var nearUsers:List<FriendModel> ,var fragment:NearUsersListFragment) : RecyclerView.Adapter<NearUsersAdapter.NearUserHolder>() {
    var token=SharedPref(context).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
    var onFlashedSent:OnFlashedSent?=null
    var requestNavigation:RequestNavigation?=null
    var onFlashBack:OnFlashBack?=null


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NearUserHolder {
        var view= LayoutInflater.from(context).inflate(R.layout.card_friend_flash, p0, false);
        return NearUserHolder(view)
    }

    override fun getItemCount()=nearUsers.size

    override fun onBindViewHolder(p0: NearUserHolder, p1: Int) {
       p0.bind(nearUsers[p1],p1)
    }

    inner class NearUserHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        fun bind(pos: FriendModel, index: Int) {

            itemView.friendDes.text=pos.desciption
            itemView.friend_img.setOnClickListener {
               openProfile(pos)
            }
            itemView.setOnClickListener {
               openProfile(pos)
            }
            if(!pos.agePrivate)
                itemView.friendAge.text=getAge(pos)

          itemView.friendDes.text=pos.desciption
          itemView.friendName.text=pos.name
            Glide.with(itemView)
                    .load(pos.image)
                    .centerCrop()
                    .into(itemView.friend_img)
            when {
                pos.isflashed -> {
                    flashViews()
                }
                pos.isflashedyou -> {
                    flashBackViews()
                }
                pos.isfriend -> {
                    friendView()
                }

            }
            itemView.flashBackBtn.setOnClickListener {
                //Toast.makeText(context!!,"MESSAGE ",Toast.LENGTH_SHORT).show()
                if(pos.isflashedyou){
                    //flash back
                    if(onFlashBack!=null){
                        onFlashBack!!.sendFlashBack(pos.id)
                        fragment.onFlashBack=object :OnFlashBack{
                            override fun sendFlashBack(userId: String) {
                            }

                            override fun onFlashedBack() {
                             friendView()
                            }

                        }
                    }
                }
            }
            itemView.messageBtn.setOnClickListener {
                // send message
                if(requestNavigation!=null)
                    requestNavigation!!.onNavigationRequested("CHAT",pos)
            }
            itemView.flashBtn.setOnClickListener { if(onFlashedSent!=null) onFlashedSent!!.sendFlash(pos.id,index)
                fragment.onFlashedSent=object :OnFlashedSent{
                    override fun onFlashed(pos: Int) {
                       flashViews()
                    }

                    override fun sendFlash(userId: String, pos: Int) {
                    }
                }
            }

        }

        private fun friendView() {
            itemView.flashBtn.visibility=View.GONE
            itemView.flashBackBtn.visibility=View.GONE
            itemView.friendIcon.visibility=View.GONE
            itemView.friendIcon2.visibility=View.GONE
            itemView.messageBtn.toggleVisiblity(true)
            itemView.messageBubble.toggleVisiblity(true)

        }

        private fun flashBackViews() {
            itemView.flashBtn.visibility=View.GONE
            itemView.flashBackBtn.visibility=View.VISIBLE
            itemView.friendIcon.visibility=View.GONE
            itemView.friendIcon2.visibility=View.VISIBLE
            itemView.friendIcon2.setImageResource(R.drawable.ic_blackflash)
            itemView.flashBackBtn.text="Flash Back"
            itemView.messageBtn.toggleVisiblity(false)
            itemView.messageBubble.toggleVisiblity(false)

        }

        private fun flashViews() {
            itemView.flashBtn.visibility=View.GONE
            itemView.flashBackBtn.visibility=View.VISIBLE
            itemView.friendIcon.visibility=View.GONE
            itemView.friendIcon2.visibility=View.VISIBLE
            itemView.messageBtn.toggleVisiblity(false)
            itemView.messageBubble.toggleVisiblity(false)
        }


    }

    private fun openProfile(friendModel: FriendModel) {
        if(requestNavigation!=null)
            requestNavigation!!.onNavigationRequested("PROFILE",friendModel)
    }
    private fun getAge(friendUser: FriendModel):String{
        var age= Common.getAge(
                friendUser.birthdayDate.substring(0,4).toInt()
                , friendUser.birthdayDate.substring(5,7).toInt()
                , friendUser.birthdayDate.substring(8,10).toInt())
        return  "/$age YO"
    }

}