package com.Qaabel.org.view.fragment.MainActivity.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.Navigation
import com.Qaabel.org.R
import com.Qaabel.org.helpers.Common
import com.Qaabel.org.interfaces.OnFlashBack
import com.Qaabel.org.interfaces.OnFlashedSent
import com.Qaabel.org.interfaces.OnLocationSent
import com.Qaabel.org.interfaces.RequestNavigation
import com.Qaabel.org.model.Api.Response.ApiNearUsersResponse
import com.Qaabel.org.model.SharedPref.AppSharedPrefs
import com.Qaabel.org.model.SharedPref.SharedPref
import com.Qaabel.org.model.Utilities.RecyclerItemTouchHelper
import com.Qaabel.org.model.Utilities.Utilities
import com.Qaabel.org.model.entities.Active
import com.Qaabel.org.model.entities.FriendModel
import com.Qaabel.org.view.adapter.Recycler.NearUsersAdapter
import com.Qaabel.org.viewModel.viewModel.friend.FlashUserViewModel
import com.Qaabel.org.viewModel.viewModel.friend.NearUsersViewModel
import kotlinx.android.synthetic.main.fragment_location.*
import kotlinx.android.synthetic.main.fragment_near_users_list.*
import kotlinx.android.synthetic.main.fragment_near_users_list.go_map


class NearUsersListFragment : Fragment(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, OnLocationSent, OnFlashedSent,OnFlashBack,RequestNavigation {
    private val TAG = "NearUsersListFragment"
    private var mUsers: List<FriendModel>?=null
    private var nearUsersViewModel: NearUsersViewModel?=null
    private var mtoken=""
    private var isActive= Active()
    private var flashUserViewModel:FlashUserViewModel?=null
    var onFlashBack:OnFlashBack?=null
    var onFlashedSent:OnFlashedSent?=null


    private fun getNearUsers() {
        isActive.setActive(true)
        val mtoken = SharedPref(context).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
        nearUsersViewModel!!.isActive(mtoken, isActive).observe(this, android.arch.lifecycle.Observer {

            if(it!=null){
                if(it.getMessage()=="done"){
                    if(Common.USER_LOCATION!=null){
                        var utilities= Utilities()
                        utilities.setOnLocationSent(this)
                        utilities.SendMyLocation(Common.USER_LOCATION,context!!)
                    }



                }

            }})
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mtoken = SharedPref(context).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
        nearUsersViewModel = ViewModelProviders.of(activity!!).get(NearUsersViewModel::class.java)
        flashUserViewModel=ViewModelProviders.of(this).get(FlashUserViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_near_users_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNearUsers()
        actions()
    }




    private fun actions() {
        go_map.setOnClickListener { Navigation.findNavController(this.activity!!, R.id.shopping_nav_host_fragment).navigate(R.id.action_navigation_Friend_to_navigation_home) }
        back_img.setOnClickListener { fragmentManager!!.popBackStack() }
    }




    override fun onLocationSent(message: String) {
        if(message=="Location updated successfully"){
            nearUsersViewModel!!.nearUsers(mtoken).observe(this, android.arch.lifecycle.Observer<ApiNearUsersResponse> {
                apiNearUsersResponse: ApiNearUsersResponse? ->
                if (apiNearUsersResponse != null) {
                    mUsers = apiNearUsersResponse.users
                    populateList()
                }
            })
        }
        else{
            Toast.makeText(context!!,message,Toast.LENGTH_SHORT).show()

        }
    }

    private fun populateList() {
       var friendsAdapter = NearUsersAdapter(context!!,mUsers!!,this)
        friendsAdapter.onFlashedSent=this
        friendsAdapter.onFlashBack=this
        friendsAdapter.requestNavigation=this
        val animation = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_animation_fall_down)
        nearUsersList!!.layoutAnimation = animation
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        nearUsersList!!.layoutManager = mLayoutManager
        nearUsersList!!.itemAnimator = DefaultItemAnimator()
        nearUsersList!!.adapter = friendsAdapter

        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(nearUsersList)
        //ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
        val itemTouchHelperCallback1: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { // Row is swiped from recycler view
// remove it from adapter
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        // attaching the touch helper to recycler view
        ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(nearUsersList)
    }

    override fun onFlashed(pos:Int) {
    }

    override fun sendFlash(userId: String,pos:Int) {
        Log.d(TAG, "sendFlash: -------------------")
        flashUserViewModel!!.flashUser(mtoken,userId).observe(this, Observer {
            if(it!=null){
               if(it.getMessage()=="user flashed"){
                  if(onFlashedSent!=null){
                      onFlashedSent!!.onFlashed(pos)
                  }
               }
                   Toast.makeText(context,it.getMessage(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun sendFlashBack(userId: String) {
        flashUserViewModel!!.flashUserBack(mtoken,userId).observe(this, Observer {
            if(it!=null){
                if(it.getMessage()=="you are now friends "){
                    if(onFlashBack!=null){
                        onFlashBack!!.onFlashedBack()
                    }
                }
                Toast.makeText(context!!,it.getMessage(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onFlashedBack() {
    }

    override fun onNavigationRequested(des: String,friendModel: FriendModel) {
       if(des=="PROFILE"){
           var bundle=Bundle()
           bundle.putParcelable("FRIEND_USER",friendModel)
           Navigation.findNavController(this.activity!!, R.id.shopping_nav_host_fragment).navigate(R.id.action_navigation_Friend_to_navigation_user_profile,bundle)
       }

        if(des=="CHAT"){
            var bundle=Bundle()
            bundle.putParcelable("FRIEND_USER",friendModel)
            Navigation.findNavController(activity!!,R.id.shopping_nav_host_fragment).navigate(R.id.action_navigation_Friend_to_navigation_chat,bundle)
        }

    }


}
