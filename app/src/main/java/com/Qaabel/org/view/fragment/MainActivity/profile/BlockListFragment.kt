package com.Qaabel.org.view.fragment.MainActivity.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.Qaabel.org.R
import com.Qaabel.org.interfaces.OnBlockClicked
import com.Qaabel.org.interfaces.UnblockedUserClicked
import com.Qaabel.org.model.Api.Response.ApiLoginResponse
import com.Qaabel.org.model.SharedPref.AppSharedPrefs
import com.Qaabel.org.model.SharedPref.SharedPref
import com.Qaabel.org.model.Utilities.Utilities
import com.Qaabel.org.model.entities.FriendModel
import com.Qaabel.org.view.adapter.Recycler.BlocksAdapter
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel
import kotlinx.android.synthetic.main.fragment_blocks.*

/**
 * A simple [Fragment] subclass.
 */
class BlockListFragment : Fragment() {

    var blocksAdapter: BlocksAdapter? = null
    private var profileViewModel: FriendProfileViewModel? = null
    private var token: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_blocks, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setupListeners()
    }

    fun init(){
        token=SharedPref(context).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
        profileViewModel=ViewModelProviders.of(this).get(FriendProfileViewModel::class.java)
        getBlocks()
    }

    private fun getBlocks() {
        profileViewModel!!.getBlocks(token).observe(this, Observer {
            if(it!=null){
                if(it.blocks.isNotEmpty()){
                    populateList(it.blocks)
                }
            }
        })
    }

    private fun populateList(blocks: List<FriendModel>) {
        val mLayoutManager = LinearLayoutManager(context)
        blocks_recycler.layoutManager = mLayoutManager
        blocks_recycler.itemAnimator = DefaultItemAnimator()
        blocksAdapter=BlocksAdapter(context!!,blocks)
        blocksAdapter!!.unblockUserClicked=object:UnblockedUserClicked{
            override fun OnClicked(user:FriendModel) {
                var ut=Utilities()
                ut.blockDialog(activity,user.name,true,false)
                ut.setOnBlockClicked(object :OnBlockClicked{
                    override fun onBlockClicked() {
                    }
                    override fun onUnBlockClicked() {
                        unblockUser(user)
                    }
                })
            }
        }
        blocks_recycler.adapter=blocksAdapter
    }

    private fun unblockUser(user: FriendModel) {
        profileViewModel!!.UnblockUser(token,user.id).observe(this, Observer {
            if (it != null) {
                if (it.getStatus() == 200) {
                    Toast.makeText(context, "User unblocked", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupListeners() {
        back_img.setOnClickListener { fragmentManager!!.popBackStack() }

    }

}
