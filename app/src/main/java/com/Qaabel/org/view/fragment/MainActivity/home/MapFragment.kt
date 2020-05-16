package com.Qaabel.org.view.fragment.MainActivity.home

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.navigation.Navigation
import com.Qaabel.org.R
import com.Qaabel.org.helpers.*
import com.Qaabel.org.helpers.mapHelper.*
import com.Qaabel.org.interfaces.OnLocationSent
import com.Qaabel.org.model.Api.Response.ApiLoginResponse
import com.Qaabel.org.model.Api.Response.ApiNearUsersResponse
import com.Qaabel.org.model.SharedPref.AppSharedPrefs
import com.Qaabel.org.model.SharedPref.SharedPref
import com.Qaabel.org.model.Utilities.Utilities
import com.Qaabel.org.model.entities.Active
import com.Qaabel.org.model.entities.FriendModel
import com.Qaabel.org.model.entities.UserModel
import com.Qaabel.org.view.fragment.Account.CompeleteFragment
import com.Qaabel.org.view.fragment.Account.ConfimEmailFragment
import com.Qaabel.org.viewModel.viewModel.account.LoginViewModel
import com.Qaabel.org.viewModel.viewModel.friend.NearUsersViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.socket.client.Socket
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap


class MapFragment : Fragment(), OnMapReadyCallback, OnLocationSent, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    var searchedText: String? = ""
    val TAG = "MapFragment"
    var mGoogleMap: GoogleMap? = null
    var mapFragment: SupportMapFragment? = null
    var vw: View? = null
    var isGpsOn = false
    var locationGrated = false
    val DEFAULT_ZOOM = 18f
    var NEAR_USER_AVAILABLE = false
    val HIDING_ZOOM = 15f
    val SHOWING_ZOOM = 16f
    val REQUEST_LOCATION = 1001
    var currentUserMarker: Marker? = null
    var lastLocation: Location? = null
    var mGpsSwitchStateReceiver: BroadcastReceiver? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var locationRequest: LocationRequest? = null
    var locationCallback: LocationCallback? = null
    var mUsers: List<FriendModel>? = null
    var currentUser: UserModel? = null
    var bundle: Bundle? = null
    var bundleLocation: LatLng? = null
    var currentLocationBtnClicked = false;
    var isActive = Active()
    var defImage = "https://qaabel.s3.amazonaws.com/def.png"
    var mtoken = ""
    var nearUsersViewModel: NearUsersViewModel? = null
    var popupYOffset = 0
    var popupXOffset = 0
    private val POPUP_POSITION_REFRESH_INTERVAL: Long = 16
    private var handler: Handler? = null
    private var markerPos: LatLng? = null
    private var positionUpdaterRunnable: Runnable? = null
    private var infoWindowListener: InfoWindowListener? = null
    private var markerHeight = 0
    var downloadMarkerImage = DownloadMarkerImage(this)
    var markers = HashMap<String, Marker?>()
    var nearUsersProfiles = HashMap<String, Bitmap?>()
    var customMarker: CustomMarker? = null
    lateinit var markerAnimation: MarkerAnimation
    var firstTimeOpenMap = true
    var fromSearch = false
    var userCity:String?="Current Location"
    var nearUsersNum=-1
    var GotDeviceLocation=false
    var placeMarker:Marker?=null
    companion object {
        var visible = false
    }

    lateinit var mMessageReceiver: BroadcastReceiver




    override fun onResume() {
        handler = Handler(Looper.getMainLooper())
        positionUpdaterRunnable = PositionUpdateRunnable()
        handler?.post(positionUpdaterRunnable)
        visible = true
        Log.d(TAG, "onResume: ---------------------------------VISIBLE IS TRUE--------------------------$visible")
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        visible = false
        Log.d(TAG, "onPause: -----------------------------------VISISBLE ---------------------------$visible")
        popupRootView.viewTreeObserver.removeOnGlobalLayoutListener(infoWindowListener!!)
        handler?.removeCallbacks(positionUpdaterRunnable)
    }

    override fun onDestroy() {
        popupRootView?.viewTreeObserver?.removeOnGlobalLayoutListener(infoWindowListener!!)
        handler?.removeCallbacks(positionUpdaterRunnable)
        try {
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            Log.d(TAG, "onDestroy: ${e.printStackTrace()}")
        }

        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        activity?.registerReceiver(mGpsSwitchStateReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
        LocalBroadcastManager.getInstance(context!!).registerReceiver(mMessageReceiver, IntentFilter(Common.NEW_FLASH_FILTER))



        checkGps()
        if (locationGrated && isGpsOn && !NEAR_USER_AVAILABLE) {
            getDeviceLocation()
            getNearUsers()
            NEAR_USER_AVAILABLE = true
        }
        initMapAndGpsViews()
        toggleMap()

    }

    override fun onStop() {
        super.onStop()
        NEAR_USER_AVAILABLE=false
        activity?.unregisterReceiver(mGpsSwitchStateReceiver)
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(mMessageReceiver)
        try {
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            Log.d(TAG, "onDestroy: ${e.printStackTrace()}")
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        visible = true
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
        nearUsersViewModel = ViewModelProviders.of(activity!!).get(NearUsersViewModel::class.java)
        mtoken = SharedPref(context).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
        currentUser = SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER)
        customMarker = CustomMarker(this)
        checkIfUserCompletedData()
        markerHeight = resources.getDrawable(R.drawable.ic_pinkmarker).intrinsicHeight
        initBroadCast()
        markerAnimation = MarkerAnimation(LatLngInterpolator.Linear(), this)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        vw = inflater.inflate(R.layout.fragment_location, container, false)
        return vw
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bundle = arguments
        setupMap()
        checkGps()
        requestPermission()
        addGpsListener()
        setListeners()
        infoWindowListener = InfoWindowListener()
        popupRootView.viewTreeObserver.addOnGlobalLayoutListener(infoWindowListener)
        if(!GotDeviceLocation)
            getDeviceLocation()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationGrated = true
                checkGps()
                setupMap()
                toggleMap()
                initMapAndGpsViews()
                getDeviceLocation()
            }
        }

    }

    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap = p0
        mGoogleMap?.setOnMarkerClickListener(this)
        mGoogleMap?.setOnMapClickListener(this)
        mGoogleMap?.setOnCameraChangeListener { newPosition ->

            if (newPosition.zoom < HIDING_ZOOM) {
               currentUserMarker?.isVisible=false
                placeMarker?.isVisible=false
                for ( marker in markers.values){
                    marker?.isVisible=false
                }
            }
        }
        if (locationGrated && isGpsOn && !NEAR_USER_AVAILABLE) {
            getDeviceLocation()
            getNearUsers()
            NEAR_USER_AVAILABLE = true
        }
        fromSearch = bundle != null
        if (locationGrated && isGpsOn && bundle != null) {
            bundleLocation = bundle?.getParcelable("SEARCH LOCATION")
            searchedText = bundle?.getString("SELECTED LOCATION NAME")
            getUsersInSearchedLocation()
            initAddressText()
            moveCamera(bundleLocation)
        }
    }


    override fun onLocationSent(message: String) {
        if (message == "Location updated successfully") {
            nearUsersViewModel?.nearUsers(mtoken)?.observe(this, android.arch.lifecycle.Observer<ApiNearUsersResponse> { apiNearUsersResponse: ApiNearUsersResponse? ->
                if (apiNearUsersResponse != null) {
                    mUsers = apiNearUsersResponse.users
                    addNearUserToMap()
                    nearUsersNum=apiNearUsersResponse.numbers
                    if(!fromSearch)
                    available_num.text = "${nearUsersNum} available"
                    NEAR_USER_AVAILABLE = true
                }
            })

        } else {
            NEAR_USER_AVAILABLE = false
        }
    }


    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker?.tag == null) return false
        var user = (marker?.tag) as FriendModel?
        var projection = mGoogleMap?.projection
        markerPos = marker.position
        var screenPoint = projection?.toScreenLocation(markerPos)
        if (screenPoint != null)
            screenPoint.y -= popupYOffset / 2
        popupRootView.toggleVisiblity(true)
        if (user != null)
            CustomInfoWindow(this, popupRootView, user)
        var gender = user?.sex
        var resourse = if (gender == 0) {
            R.drawable.ic_bluemarker
        } else {
            R.drawable.ic_pinkmarker
        }

        marker?.setIcon(customMarker?.createCustomMarker(nearUsersProfiles[user?.username], resourse, false))
        return true
    }


    override fun onMapClick(p0: LatLng?) {
        popupRootView?.toggleVisiblity(false)
    }


    inner class InfoWindowListener : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            popupXOffset = popupRootView.width / 2
            popupYOffset = popupRootView.height
        }
    }

    inner class PositionUpdateRunnable : Runnable {
        private var lastXPos = Integer.MIN_VALUE
        private var lastYPos = Integer.MAX_VALUE
        override fun run() {
            handler?.postDelayed(this, POPUP_POSITION_REFRESH_INTERVAL)
            if (popupRootView.visibility == View.VISIBLE && markerPos != null) {
                var targetPos = mGoogleMap?.projection?.toScreenLocation(markerPos)
                if (lastXPos != targetPos?.x || lastYPos != targetPos.y) {
                    if (targetPos != null) {
                        popupRootView.x = (targetPos.x - popupXOffset).toFloat()
                        popupRootView.y = (targetPos.y - popupYOffset - markerHeight).toFloat()
                    }
                    lastXPos = targetPos?.x!!
                    lastYPos = targetPos?.y
                }
            }
        }

    }





}
