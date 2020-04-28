package com.Qaabel.org.view.adapter.Recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.Qaabel.org.R
import com.Qaabel.org.helpers.toggleVisiblity
import com.Qaabel.org.interfaces.OnPlaceSelected
import com.Qaabel.org.model.Api.Response.PlaceResult
import kotlinx.android.synthetic.main.place_list_item.view.*

class SearchPlacesAdapter(var context: Context, var places: List<PlaceResult?>?, var candidates: List<PlaceResult?>?) :RecyclerView.Adapter<SearchPlacesAdapter.PlaceViewHolder>() {
     var onPlaceSelected:OnPlaceSelected?=null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PlaceViewHolder {
       var view=LayoutInflater.from(context).inflate(R.layout.place_list_item,null,false)
        return PlaceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(places!=null)
            places!!.size
        else
            candidates!!.size
    }

    override fun onBindViewHolder(p0: PlaceViewHolder, p1: Int) {
        p0.bind(p1)
    }


    inner class PlaceViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        fun bind(index: Int) {
          if(places!=null){
              var place=places!![index]
              itemView.placeTxt.text=place!!.getName()
              itemView.setOnClickListener { if(onPlaceSelected!=null){
                  onPlaceSelected!!.onSelected(place)
              } }
          }
            else if(candidates!=null){
              var place=candidates!![index]
              itemView.placeTxt.text=place?.getName()
              itemView.addressTxt.toggleVisiblity(true)
               itemView.addressTxt.text=place?.getFormattedAddress()
              itemView.setOnClickListener { if(onPlaceSelected!=null){
                  onPlaceSelected!!.onSelected(place!!)
              } }

          }
        }

    }
}