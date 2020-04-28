package com.Qaabel.org.view.fragment.MainActivity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.Qaabel.org.helpers.toggleVisiblity
import com.Qaabel.org.R
import com.Qaabel.org.helpers.Common
import com.Qaabel.org.interfaces.OnBlockClicked
import com.Qaabel.org.model.SharedPref.AppSharedPrefs
import com.Qaabel.org.model.SharedPref.SharedPref
import com.Qaabel.org.model.Utilities.Utilities
import com.Qaabel.org.model.entities.FriendModel
import com.Qaabel.org.viewModel.viewModel.friend.FlashUserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_friend_profile.*

/**
 * A simple [Fragment] subclass.
 */
class FriendProfileFragment : Fragment(), OnBlockClicked {
    private var vw:View?=null
    private val TAG = "FriendProfileFragment"
    private var friendUser:FriendModel?=null
    private var flashUserViewModel:FlashUserViewModel?=null
    private var mtoken=""
    private var source:String?=null
    private var isFriend:Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flashUserViewModel= ViewModelProviders.of(this).get(FlashUserViewModel::class.java)
        friendUser=arguments?.getParcelable("FRIEND_USER")
        source=arguments?.getString("SOURCE")
       if(source=="MESSAGES"||source=="FRIENDS ADAPTER"){
           friendUser?.isfriend=true
       }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        vw = inflater.inflate(R.layout.fragment_friend_profile, container, false)
        return vw
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setupListeners()
    }

    private fun setupListeners() {
        messageBtn.setOnClickListener {
            if(friendUser!=null){
                if(friendUser!!.isfriend){
                    //open chat
                    var bundle=Bundle()
                    bundle.putParcelable("FRIEND_USER",friendUser)
                    Navigation.findNavController(activity!!,R.id.shopping_nav_host_fragment).navigate(R.id.action_navigation_user_profile_to_navigation_chat,bundle)
                }
            }
        }
        flashBtn.setOnClickListener {
            sendFlash()
        }
        flashBackBtn.setOnClickListener {
          sendFlashBack()
        }
        ignoreBtn.setOnClickListener {
            ignoreUser()
        }
        blockBtn.setOnClickListener {
            //block user dialog
            var utils=Utilities()
           utils.blockDialog(activity, friendUser?.name, false,false)
            utils.setOnBlockClicked(this)
        }
        back_img.setOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    private fun ignoreUser() {
       flashUserViewModel?.ignoreUser(mtoken,friendUser?.id)?.observe(this, Observer {
           if(it!=null){
               if(it.getStatus()==200){
                   Toast.makeText(context,"User ignored",Toast.LENGTH_SHORT).show()
                   ignoreViews()
               }
           }
       })
    }



    private fun init(){
        mtoken=SharedPref(context!!).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
             if(friendUser!=null){
                 if(friendUser?.image!=null)
                 Picasso.get().load(friendUser?.image).into(profile_img)
                     if(friendUser?.agePrivate!=null&&!friendUser?.agePrivate!!)
                     user_age_et.text=getAge()
                 userName.text=friendUser?.username?.toLowerCase()
                 user_name_et.text=friendUser?.name
                 user_job_et.text=friendUser?.job
                 user_details_et.text=friendUser?.desciption
                 setupFlashes()

             }
         }

    private fun setupFlashes() {
        Log.d(TAG, "setupFlashes: -------------------------Friend:-${friendUser!!.isfriend}--------ISFlased:-${friendUser!!.isflashed}")
        if(source!="FLASHES")
        when {
            friendUser!!.isfriend -> {
                Log.d(TAG, "setupFlashes: is friend you")
               friendViews()
            }
            friendUser!!.isflashedyou -> {
                Log.d(TAG, "setupFlashes: is flashed you")
               flashBackViews()
            }
            friendUser!!.isflashed -> {
                Log.d(TAG, "setupFlashes: is isflased")
              flashSentViews()
            }
        }
    }

    private fun flashSentViews() {
        messageIcon.toggleVisiblity(false)
        messageBtn.text="Flash Sent"
        messageBtn.toggleVisiblity(true)
        flashBackBtn.toggleVisiblity(false)
        flashBackIcon.toggleVisiblity(false)
        flashIcon.toggleVisiblity(false)
        flashBtn.toggleVisiblity(false)
        ignoreBtn.toggleVisiblity(false)
    }

    private fun flashBackViews() {
        messageIcon.toggleVisiblity(false)
        messageBtn.toggleVisiblity(false)
        flashBackBtn.toggleVisiblity(true)
        flashBackIcon.toggleVisiblity(true)
        flashIcon.toggleVisiblity(false)
        flashBtn.toggleVisiblity(false)
        ignoreBtn.toggleVisiblity(true)
    }
    private fun ignoreViews() {
        messageIcon.toggleVisiblity(false)
        messageBtn.toggleVisiblity(false)
        flashBackBtn.toggleVisiblity(false)
        flashIcon.toggleVisiblity(false)
        flashBtn.toggleVisiblity(false)
        flashBackIcon.toggleVisiblity(false)
        ignoreBtn.toggleVisiblity(false)
    }
    private fun friendViews() {
        messageBtn.text=""
        messageIcon.toggleVisiblity(true)
        messageBtn.toggleVisiblity(true)
        flashBackBtn.toggleVisiblity(false)
        flashIcon.toggleVisiblity(false)
        flashBtn.toggleVisiblity(false)
        flashBackIcon.toggleVisiblity(false)
        ignoreBtn.toggleVisiblity(false)
    }

    private fun getAge():String{
           var age= Common.getAge(
                   friendUser!!.birthdayDate.substring(0,4).toInt()
                   ,friendUser!!.birthdayDate.substring(5,7).toInt()
                   ,friendUser!!.birthdayDate.substring(8,10).toInt())
        return  "/$age YO"
       }

     private fun sendFlash() {

        flashUserViewModel!!.flashUser(mtoken,friendUser!!.id).observe(this, android.arch.lifecycle.Observer {
            if(it!=null){
                if(it.getMessage()=="user flashed"){
                    flashSentViews()
                }
                Toast.makeText(context,it.getMessage(),Toast.LENGTH_SHORT).show()
            }
        })

    }

     private fun sendFlashBack() {
        flashUserViewModel!!.flashUserBack(mtoken,friendUser!!.id).observe(this, android.arch.lifecycle.Observer {
          if(it!=null){
              if(it.getMessage()=="you are now friends "){
                friendViews()
              }
              Toast.makeText(context!!,it.getMessage(), Toast.LENGTH_SHORT).show()
          }
        })
    }

    override fun onBlockClicked() {
        //blocked

        flashUserViewModel!!.blockUser(mtoken,friendUser!!.id).observe(this, Observer {
            if(it!=null){
                if(it.getStatus()==200){
                Toast.makeText(context!!,"User blocked",Toast.LENGTH_SHORT).show()
                ignoreViews()}
            }

        })
    }

    override fun onUnBlockClicked() {

    }
}



