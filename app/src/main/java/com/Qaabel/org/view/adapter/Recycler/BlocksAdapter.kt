package com.Qaabel.org.view.adapter.Recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.Qaabel.org.R
import com.Qaabel.org.interfaces.UnblockedUserClicked
import com.Qaabel.org.model.entities.FriendModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_frien_block.view.*

class BlocksAdapter(var context: Context,var blocks:List<FriendModel>) : RecyclerView.Adapter<BlocksAdapter.BlockHolder>() {
    var unblockUserClicked:UnblockedUserClicked?=null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BlockHolder {
    val view=LayoutInflater.from(context).inflate(R.layout.card_frien_block,p0,false)
        return BlockHolder(view)
    }

    override fun getItemCount()=blocks.size

    override fun onBindViewHolder(p0: BlockHolder, p1: Int) {
     p0.bind(blocks[p1])
    }


    inner class BlockHolder(var view: View) : RecyclerView.ViewHolder(view) {


        fun bind(pos: FriendModel){
            Picasso.get().load(pos.image).into(view.profile_img)
            view.name.text=pos.name
            view.unblock_user.setOnClickListener {
                 if(unblockUserClicked!=null)
                     unblockUserClicked!!.OnClicked(pos)
        }

        }
    }}

