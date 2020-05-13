package com.Qaabel.org.view.fragment.MainActivity.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.navigation.Navigation
import com.Qaabel.org.R
import com.Qaabel.org.helpers.Common
import com.Qaabel.org.helpers.LocationsHelper
import com.Qaabel.org.helpers.hideKeyboard
import com.Qaabel.org.interfaces.OnPlaceSelected
import com.Qaabel.org.model.Api.PlaceRequest
import com.Qaabel.org.model.Api.Response.PlaceResult
import com.Qaabel.org.model.Api.Response.PlacesResponse
import com.Qaabel.org.view.ItemDecoration
import com.Qaabel.org.view.adapter.Recycler.SearchPlacesAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() ,OnPlaceSelected{
    private val TAG = "SearchFragment"
    var predictionList: List<PlaceResult?>?=null
    private val REQUEST_LOCATION = 1001
    var placesClient: PlacesClient? = null
    var autocompleteSessionToken: AutocompleteSessionToken? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      setListeners()
        initPlaces()
    }



    private fun setListeners() {
        searchBar.setOnClickListener {
            closeBtn.visibility=View.VISIBLE


        }
        closeBtn.setOnClickListener {
            closeBtn.visibility=View.GONE
            searchBar.text.clear()
            searchList.visibility=View.GONE
        }
        searchBar.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                    closeBtn.visibility=View.VISIBLE
                    searchList.visibility=View.VISIBLE
                if(Common.USER_LOCATION!=null)
                    getSuggestions(s.toString())

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        searchBar.setOnEditorActionListener { view, actionId, event ->
         if(actionId==EditorInfo.IME_ACTION_SEARCH
                 ||actionId==EditorInfo.IME_ACTION_DONE){
             //hide keyboard
             hideKeyboard()
             getSuggestions(searchBar.text.toString())
             return@setOnEditorActionListener true
         }
            return@setOnEditorActionListener false
        }
    }





    private fun getSuggestions(query: String) {
        Log.d(TAG, "getSuggestions: -----------------------------------------")
        val url = "https://maps.googleapis.com/maps/api/place/"
        val googlePlaceApiCall = PlaceRequest.getAPI(url)
        googlePlaceApiCall?.getNearByPlaces(query, getString(R.string.placesApiKey),"5000",
                LocationsHelper().getLocationBias(Common.USER_LOCATION!!))
                ?.enqueue(object : Callback<PlacesResponse?> {
            override fun onResponse(call: Call<PlacesResponse?>, response: Response<PlacesResponse?>) {
                if (response.body() != null) {
                    if (response.body()!!.getStatus() == "OK") {
                        Log.d(TAG, "onResponse: --------------------------------------")
                        predictionList = response.body()!!.getResults()
                        if(predictionList!=null)
                        if(predictionList!!.isNotEmpty()){
                            Log.d(TAG, "onResponse: -----------------------NOT EMPTY LIST-------------------")
                            createList(null)
                        }
                    } else {
                        if(response.body()?.getStatus()=="ZERO_RESULTS")
                            googlePlaceApiCall?.getPlaces(query, getString(R.string.placesApiKey))?.enqueue(object :Callback<PlacesResponse>{
                                override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
                                    Log.d(TAG, "onFailure: ------------------------"+t.message)
                                }

                                override fun onResponse(call: Call<PlacesResponse>, response: Response<PlacesResponse>) {
                                    Log.d(TAG, "onResponse: -----------FROM HERE--------"+response.body()?.getStatus())
                                    if(response.body()?.getStatus()=="OK"){
                                        predictionList=null
                                        createList(response.body()?.getResults())

                                    }

                                }
                            })
                    }
                }
            }

            override fun onFailure(call: Call<PlacesResponse?>, t: Throwable) {
                Log.d(TAG, "onFailure: ----------------------------------" + t.message)
            }
        })

    }

    private fun createList(candidates: List<PlaceResult?>?) {
        Log.d(TAG, "createList: ---------------------------")
        if(predictionList!=null)
        {
            if(!predictionList!!.isNullOrEmpty()){
                populateAdapter(predictionList!!,null)
            }
        }
        if(predictionList.isNullOrEmpty()&&!candidates.isNullOrEmpty()){
            populateAdapter(null,candidates)
        }
    }

    private fun populateAdapter(predictionList: List<PlaceResult?>?, candidates: List<PlaceResult?>?) {
        Log.d(TAG, "populateAdapter: ----------------------")
        var linearLayoutManager=LinearLayoutManager(context)
        var adapter=SearchPlacesAdapter(context!!,predictionList,candidates)
        searchList?.addItemDecoration(ItemDecoration(context!!))
        adapter.onPlaceSelected=this
        searchList?.layoutManager=linearLayoutManager
        searchList?.adapter=adapter

    }

    private fun initPlaces() {
        Places.initialize(context!!, getString(R.string.placesApiKey))
        placesClient = Places.createClient(context!!)
        autocompleteSessionToken = AutocompleteSessionToken.newInstance()

    }

    override fun onSelected(place: PlaceResult) {
        hideKeyboard()
        Log.d(TAG, "onSelected: ")
        var location=place.getGeometry()!!.getLocation()

        var bundle=Bundle()
        if(location!=null){
            bundle.putParcelable("SEARCH LOCATION",LatLng(location.getLat()!!,location.getLng()!!))
            Log.d(TAG, "onSelected:------------------------------------------------ "+place.getName())
            bundle.putString("SELECTED LOCATION NAME",place.getName())
        }

        Navigation.findNavController(activity!!, R.id.shopping_nav_host_fragment).navigate(R.id.action_navigation_search_to_navigation_home,bundle)

    }


}