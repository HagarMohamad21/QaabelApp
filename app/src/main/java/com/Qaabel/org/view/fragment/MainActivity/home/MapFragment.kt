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
import com.Qaabel.org.interfaces.OnLocationSent
import com.Qaabel.org.interfaces.ShowMarkerNotificationDot
import com.Qaabel.org.model.Api.Response.ApiLoginResponse
import com.Qaabel.org.model.Api.Response.ApiNearUsersResponse
import com.Qaabel.org.model.Api.Response.NearPlace
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.gson.Gson
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap


class MapFragment : Fragment() , OnMapReadyCallback ,OnLocationSent,GoogleMap.OnMarkerClickListener,GoogleMap.OnMapClickListener{
    val TAG = "MapFragment"
    var mGoogleMap: GoogleMap? = null
    var mapFragment: SupportMapFragment? = null
    var vw: View? = null
    var isGpsOn = false
    var locationGrated = false
    val DEFAULT_ZOOM = 18f
    var  NEAR_USER_AVAILABLE=false
    val HIDING_ZOOM=15f
    val SHOWING_ZOOM=16f
    val REQUEST_LOCATION = 1001
    var currentUserMarker: Marker?=null
    var lastLocation: Location? = null
    var mGpsSwitchStateReceiver: BroadcastReceiver? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var locationRequest: LocationRequest? = null
    var locationCallback:LocationCallback?=null
    var mUsers: List<FriendModel>?=null
    var currentUser:UserModel?=null
    var bundle:Bundle?=null
    var bundleLocation:LatLng?=null
    var currentLocationBtnClicked=false;
    var isActive=Active()
    var defImage="https://qaabel.s3.amazonaws.com/def.png"
    var mtoken=""
    var nearUsersViewModel:NearUsersViewModel?=null
    var popupYOffset=0
    var popupXOffset=0
    private val POPUP_POSITION_REFRESH_INTERVAL:Long = 16
    private var handler: Handler? = null
    private var markerPos:LatLng?=null
    private var positionUpdaterRunnable: Runnable? = null
    private var infoWindowListener:InfoWindowListener?=null
    private var markerHeight=0
    private var mSocket: Socket?=null
    var downloadMarkerImage=DownloadMarkerImage(this)
    var markers=HashMap<String,Marker?>()
    var nearUsersProfiles=HashMap<String,Bitmap?>()
    var customMarker:CustomMarker?=null
    private lateinit var markerAnimation:MarkerAnimation
    private var firstTimeOpenMap=true
    companion object{
        var visible=false
    }

    lateinit var mMessageReceiver:BroadcastReceiver
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



    override fun onResume() {
        handler=Handler(Looper.getMainLooper())
        positionUpdaterRunnable=PositionUpdateRunnable()
        handler?.post(positionUpdaterRunnable)
        visible=true
        Log.d(TAG, "onResume: ---------------------------------VISIBLE IS TRUE--------------------------$visible")
        super.onResume()
    }




    override fun onDestroy() {
        popupRootView?.viewTreeObserver?.removeOnGlobalLayoutListener(infoWindowListener!!)
        handler?.removeCallbacks(positionUpdaterRunnable)
        try{
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        }
        catch (e:Exception){
            Log.d(TAG, "onDestroy: ${e.printStackTrace()}")
        }

        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        activity?.registerReceiver(mGpsSwitchStateReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
        LocalBroadcastManager.getInstance(context!!).registerReceiver(
                mMessageReceiver,  IntentFilter(Common.NEW_FLASH_FILTER))



        checkGps()
        if(locationGrated&&isGpsOn&&!NEAR_USER_AVAILABLE){
            getDeviceLocation()
            getNearUsers()
            NEAR_USER_AVAILABLE=true
        }
        initMapAndGpsViews()
        toggleMap()

    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(mGpsSwitchStateReceiver)
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(mMessageReceiver)
        try{
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        }
        catch (e:Exception){
            Log.d(TAG, "onDestroy: ${e.printStackTrace()}")
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        currentUser!!.image
        visible=true
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
        nearUsersViewModel = ViewModelProviders.of(activity!!).get(NearUsersViewModel::class.java)
        mtoken = SharedPref(context).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
        currentUser = SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER)
        customMarker= CustomMarker(this)
        checkIfUserCompletedData()
        markerHeight=resources.getDrawable(R.drawable.ic_pinkmarker).intrinsicHeight
        initBroadCast()
        markerAnimation= MarkerAnimation(LatLngInterpolator.Linear(),this)


    }

    private fun initBroadCast() {

        mMessageReceiver=object:BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent?.getStringExtra(Common.SERVICE_MESSAGE)==Common.NotificationType_FLASH){
                    val flashUser:FriendModel= intent.getParcelableExtra(Common.SERVICE_USER) as FriendModel
                    val marker:Marker?=markers[flashUser.username]
                    val user=marker?.tag as FriendModel
                    val gender=user.sex
                    val resourse=if(gender==0) { R.drawable.ic_bluemarker }
                    else{R.drawable.ic_pinkmarker}
                    marker?.setIcon(customMarker?.createCustomMarker(nearUsersProfiles[user.username],resourse,true))
                    user.isfriend=false
                    user.isflashed=false
                    user.isflashedyou=true
                    marker?.tag=user
                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        vw = inflater.inflate(R.layout.fragment_location, container, false)
        return vw
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bundle=arguments
        setupMap()
        checkGps()
        requestPermission()
        addGpsListener()
        setListeners()
        infoWindowListener=InfoWindowListener()
        popupRootView.viewTreeObserver.addOnGlobalLayoutListener(infoWindowListener)

    }

    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap=p0
        mGoogleMap?.setOnMarkerClickListener(this)
        mGoogleMap?.setOnMapClickListener(this)
        if (locationGrated && isGpsOn&&!NEAR_USER_AVAILABLE) {
            getDeviceLocation()
            getNearUsers()
            NEAR_USER_AVAILABLE=true
            mGoogleMap?.setOnCameraChangeListener { newPosition ->
                if (newPosition.zoom <HIDING_ZOOM ) {


                } else if(newPosition.zoom >= SHOWING_ZOOM) {
                    if(mUsers!=null){
                        if(currentUserMarker!=null){
                            currentUserMarker?.isVisible=true
                        }
                        // getNearUsers()
                    }


                }
            }
        }
        if(locationGrated&&isGpsOn&&bundle!=null){
            bundleLocation=bundle!!.getParcelable("SEARCH LOCATION")
            moveCamera(bundleLocation)
        }
    }
    private fun setListeners() {
        messagesBtn.setOnClickListener {
            Navigation.findNavController(this.activity!!, R.id.map).navigate(R.id.action_navigation_home_to_messagesFragment)

        }
        locationTxtView.setOnClickListener { startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        currentLocationBtn.setOnClickListener {
            currentLocationBtnClicked=true
            warningImg.visibility=View.GONE
            warningTxt.visibility=View.GONE
            bundleLocation=null
            moveCamera(null) }
        RxView.clicks(go_map).throttleFirst(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe()
        { _: Any? -> Navigation.findNavController(this.activity!!, R.id.map).navigate(R.id.action_navigation_Friend_to_navigation_home) }
    }
    private fun setupMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }
    private fun checkGps() {
        val manager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGpsOn = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    private fun requestPermission() {
        locationGrated = if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
            false
        } else {
            true
        }
        toggleMap()
    }
    private fun addGpsListener() {
        mGpsSwitchStateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == "android.location.PROVIDERS_CHANGED") {
                    toggleMap()
                    if(locationGrated&&isGpsOn&&!NEAR_USER_AVAILABLE){
                        getDeviceLocation()
                        getNearUsers()
                        NEAR_USER_AVAILABLE=true
                    }
                    initMapAndGpsViews()
                }
            }
        }
    }
    private fun initMapAndGpsViews() {
        checkGps()
        if (isGpsOn && locationGrated) {
            locationTxtView?.visibility = View.GONE
            currentLocationBtn?.visibility = View.VISIBLE
            go_map?.visibility = View.VISIBLE
            available_num?.visibility = View.VISIBLE
            city_name?.visibility = View.VISIBLE
            onlineView?.visibility = View.VISIBLE
        }
        else {
            warningImg?.toggleVisiblity(false)
            warningTxt?.toggleVisiblity(false)
            onlineView?.toggleVisiblity(false)
            popupRootView?.toggleVisiblity(false)
            currentLocationBtn?.visibility = View.GONE
            locationTxtView?.visibility = View.VISIBLE
            available_num?.visibility = View.GONE
            city_name?.visibility = View.GONE
            go_map?.visibility = View.GONE
        }
    }
    private fun toggleMap() {

        checkGps()
        if (locationGrated && isGpsOn) {
            Log.d(TAG, "toggleMap: TRUE")
            mapFragment?.view?.visibility = View.VISIBLE
        } else {
            mapFragment?.view?.visibility = View.GONE
            Log.d(TAG, "toggleMap: FASLE")
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.interval=2000
        locationRequest?.fastestInterval = 1500
        locationRequest?.smallestDisplacement = 4f
    }

     fun moveCamera(location: LatLng?,bearing:Double=0.0) {
        Log.d(TAG, "moveCamera: --------------------------------------")
        val cameraUpdate: CameraUpdate
        if(location==null&&lastLocation!=null)
        {
            var  lastLocationLatLang=LatLng(lastLocation!!.latitude, lastLocation!!.longitude)
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(lastLocationLatLang, DEFAULT_ZOOM)
            if(currentUserMarker==null)
            {
                if(currentUser!=null){
                    if(currentUser?.image!=defImage&&currentUser?.image!=null){
                        downloadMarkerImage.downloadImage("Me",currentUser?.image!!,-1)
                    }

                    else
                        nearUsersProfiles["Me"]=null
                    currentUserMarker= mGoogleMap?.addMarker(MarkerOptions().icon(customMarker
                            ?.createCustomMarker(null,-1,false))
                            .title("Me")
                            .flat(true)
                            .position(lastLocationLatLang))
                    currentUserMarker?.tag=null
                    markers["Me"]=currentUserMarker

                }
                else{
                    Log.d(TAG, "moveCamera: -----------CURRENT USER IS NULL")

                }

            }

            else{
                Log.d(TAG, "moveCamera:currentUserMarker -------------------not null ")
            }
            if(bundle==null)
                mGoogleMap?.moveCamera(cameraUpdate)
            else if(currentLocationBtnClicked){
                mGoogleMap?.moveCamera(cameraUpdate)
                currentLocationBtnClicked=false
            }
        }
        else{
            Log.d(TAG, "moveCamera: -----------ELSE--------------------")
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM)
            mGoogleMap?.moveCamera(cameraUpdate)
        }
        if(lastLocation!=null)
            initAddressText()
//         if(bearing!=0.0)
//             currentUserMarker?.rotation=bearing.toFloat()-180
    }


    private fun findDistanceBetweenCurrentLocationAndPlace(location: LatLng?): Float {
        val results = FloatArray(1)
        if(lastLocation!=null)
            Location.distanceBetween(lastLocation!!.latitude,lastLocation!!.longitude,location!!.latitude,location.longitude,results)
        Log.d(TAG, "findDistanceBetweenCurrentLocationAndPlace: ---------------------------------------"+results[0])
        if(isGpsOn){
            if(results[0]>40f){
                warningImg?.visibility=View.VISIBLE
                warningTxt?.visibility=View.VISIBLE
                Common.DISTANCE=results[0]
            }
        }
        return results[0]
    }

    private fun initAddressText() {
        val geocoder = Geocoder(activity!!, Locale.ENGLISH)
        if (city_name!=null&&lastLocation!=null)
            city_name?.text= LocationsHelper().getAdrress(lastLocation!!,geocoder)

    }

    private fun getDeviceLocation() {
        fusedLocationProviderClient?.lastLocation?.addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                lastLocation = it.result
                Common.USER_LOCATION=lastLocation

                    if(bundleLocation!=null)
                        findDistanceBetweenCurrentLocationAndPlace(bundleLocation)

                    buildLocationRequest()
                    buildLocationCallback()
                    fusedLocationProviderClient?.requestLocationUpdates(locationRequest,locationCallback,null)
            }

        })
    }


    private fun buildLocationCallback() {

        Log.d(TAG, "buildLocationCallback: --------------------------------------------------------")
        locationCallback= object :LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                if(p0 == null)
                    return
                lastLocation=p0.lastLocation
                Common.USER_LOCATION=lastLocation
                if(firstTimeOpenMap){
                    moveCamera(null)
                    firstTimeOpenMap=false
                }
                else
                markerAnimation.animate(currentUserMarker, LatLng(lastLocation?.latitude!!,lastLocation?.longitude!!))
            }
        }

    }
    private fun getNearUsers() {
        isActive.setActive(true)
        val mtoken = SharedPref(context).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN)
        nearUsersViewModel!!.isActive(mtoken, isActive).observe(this, android.arch.lifecycle.Observer {

            if(it!=null){
                if(it.getMessage()=="done"){
                    if(lastLocation!=null){
                        Log.d(TAG, "getNearUsers: --------------------------------------"+lastLocation!!.longitude)
                        var utilities=Utilities()
                        utilities.setOnLocationSent(this)
                        var location=Location("")
                        //costa
//                        location.latitude= 30.0491303
//                        location.longitude=31.2421211
                        utilities.SendMyLocation(lastLocation,context!!)

                    }

                }

            }})
    }
    private fun checkIfUserCompletedData() {
        Log.d(TAG, "checkIfEmailNotVerified: ********************CHECKING EMAIL ******************")

        if (SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER) != null)
            if (SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER).email == null)
            {
                var  reg=(SharedPref(context).getStrin(getString(R.string.emailRegistered)))
                if(reg==getString(R.string.True)){
                    Log.d(TAG, "checkIfUserCompletedData: --------------EMAIL IS REGISTERED BUT NOT VERIFIED")
                    var user=SharedPref(context).getUser(AppSharedPrefs.TEMP_USER_FROM_COMPLETE_DATA)
                    var bundle=Bundle()
                    bundle!!.putParcelable("USER MODEL", user)
                    val newFragment = ConfimEmailFragment.newInstance("Complete")
                    newFragment.isCancelable = false
                    newFragment.show(fragmentManager, "dialog")
                    newFragment.arguments = bundle
                }
                else{
                    Log.d(TAG, "checkIfUserCompletedData: -----------------EMAIL IS NULL-----------------------")
                    var  newFragment = CompeleteFragment.newInstance("Complete")
                    newFragment.isCancelable = false
                    newFragment.show(fragmentManager, "dialog")
                }



            }
            else
            {
                if (!SharedPref(context).isEmailVerified(AppSharedPrefs.EMAIL_VERIFIED_KEY)) {
                    Log.d(TAG, "checkIfUserCompletedData: ---------------------------------EMAIL NOT VERIFIED----------------------")
                    getCorrectToken()
                } else { //email is verified

                }
            }
        else
        {
            Log.d(TAG, "checkIfUserCompletedData: --------------USER IS NULL---------------")
            //user is null
            var  newFragment = CompeleteFragment.newInstance("Complete")
            newFragment.isCancelable = false
            newFragment.show(fragmentManager, "dialog")
        }

    }

    private fun getCorrectToken() {
        Log.d(TAG, "getCorrectToken: ----------------------------")
        var loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        val email = SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER).email
        val password = SharedPref(context).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER).password
        if (email != null && password != null) {
            Utilities.MsignUpUser = UserModel(email.trim { it <= ' ' }, password.trim { it <= ' ' })
            loginViewModel.login().observe(this, android.arch.lifecycle.Observer<ApiLoginResponse> { apiLoginResponse: ApiLoginResponse? ->
                if (apiLoginResponse != null && apiLoginResponse.status == "200") { //email is verified
                    Log.d(TAG, "getCorrectToken: ---EMAIL IS VERIFIED------")
                    SharedPref(context).saveString(AppSharedPrefs.SHARED_PREF_TOKRN, apiLoginResponse.authToken)
                    SharedPref(context).setEmailVerified(AppSharedPrefs.EMAIL_VERIFIED_KEY, true)
                    SharedPref(context).saveUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER,apiLoginResponse.user)
                    Log.d(TAG, "getCorrectToken: ----------------------------------------"+apiLoginResponse.user.image)
                    Log.d(TAG, "getCorrectToken: ----------------------------------------"+apiLoginResponse.user.email)
                } else {
                    val newFragment = ConfimEmailFragment.newInstance("Complete")
                    newFragment.isCancelable = false
                    newFragment.show(fragmentManager, "dialog")
                }
            })
        }
    }

    override fun onLocationSent(message: String) {
        var location=Location("")

        //costa coordinates
//        location.latitude= 30.049108
//        location.longitude=31.2416948

        //next to costa
//        location.latitude= 30.0497782
//        location.longitude=31.2400733
        if(message=="Location updated successfully"){
            nearUsersViewModel?.nearUsers(mtoken)?.observe(this, android.arch.lifecycle.Observer<ApiNearUsersResponse> {
                apiNearUsersResponse: ApiNearUsersResponse? ->
                if (apiNearUsersResponse != null) {
                    mUsers = apiNearUsersResponse.users
                    addNearUserToMap()
                    available_num.text = mUsers!!.size.toString() + "  available"
                    NEAR_USER_AVAILABLE=true
                }
            })

        }
        else{
            NEAR_USER_AVAILABLE=false
        }
    }

    private fun addPlacesToMap(places: List<NearPlace?>?) {
        Log.d(TAG, "addPlacesToMap: --------------------------------------------"+places?.size)
        for(Place in places!!){

            var latLng=LatLng(Place?.getGeometry()?.getLocation()?.getLat()!!,Place?.getGeometry()?.getLocation()?.getLng()!!)
            var marker= mGoogleMap!!.addMarker(MarkerOptions().position(latLng)
                    .title(Place?.getName())
                    .icon(customMarker?.createPlaceMarker("",Place?.getNumberOfUsers()!!)))
        }
    }

    private fun addNearUserToMap() {
        var latLng:LatLng
        var marker:Marker
//        for(user in mUsers!!){
//            Log.d(TAG, "addNearUserToMap: "+user.image)
//            latLng= LatLng(user.location.coordinates[0],user.location.coordinates[1])
//            if(findDistanceBetweenCurrentLocationAndPlace(latLng)>40){
//                Log.d(TAG, "addNearUserToMap: SOME USER IS AWAY BY MORE THAN 10-------------${user.name}")
//
//                if(Common.USER_LOCATION!=null)
//                    nearUsersViewModel?.nearPlaces(mtoken,lastLocation)?.observe(this, android.arch.lifecycle.Observer {
//                        addPlacesToMap(it?.getPlaces())
//                        if(it?.getPlaces()?.isEmpty()!!){
//                            var marker= mGoogleMap!!.addMarker(MarkerOptions().position(latLng)
//                                    .title(user.name)
//                                    .icon(customMarker?.createPlaceMarker("",1)))
//                        }
//                    })
//            }
//
//            else{
//                var gender=user.sex
//                var resourse=if(gender==0) { R.drawable.ic_bluemarker }
//                else{R.drawable.ic_pinkmarker}
//
//                marker= mGoogleMap!!.addMarker(MarkerOptions().position(latLng)
//                        .title(user.name)
//                        .icon(customMarker?.createCustomMarker(null,resourse,false)))
//                marker.tag=user
//                markers[user.username] = marker
//                if(user?.image!=defImage){
//                    downloadMarkerImage.downloadImage(user?.username,user?.image,resourse)
//                }
//                else nearUsersProfiles[user?.username]=null
//            }
//
//
//        }
    }


    override fun onMarkerClick(marker: Marker?): Boolean {
        if(marker?.tag==null) return false
        var user=(marker?.tag) as FriendModel?
        var projection=mGoogleMap?.projection
        markerPos=marker.position
        var screenPoint=projection?.toScreenLocation(markerPos)
        if(screenPoint!=null)
            screenPoint.y-= popupYOffset/2
        popupRootView.toggleVisiblity(true)
        if(user!=null)
            CustomInfoWindow(this,popupRootView,user)
        var gender=user?.sex
        var resourse=if(gender==0) { R.drawable.ic_bluemarker }
        else{R.drawable.ic_pinkmarker}

        marker?.setIcon(customMarker?.createCustomMarker(nearUsersProfiles[user?.username],resourse,false))
        return true
    }


    override fun onMapClick(p0: LatLng?) {
        popupRootView?.toggleVisiblity(false)
    }


    inner class InfoWindowListener:ViewTreeObserver.OnGlobalLayoutListener{
        override fun onGlobalLayout() {
            popupXOffset=popupRootView.width/2
            popupYOffset=popupRootView.height
        }
    }

    inner class PositionUpdateRunnable :Runnable{
        private var lastXPos=Integer.MIN_VALUE
        private var lastYPos=Integer.MAX_VALUE
        override fun run() {
            handler?.postDelayed(this,POPUP_POSITION_REFRESH_INTERVAL)
            if(popupRootView.visibility==View.VISIBLE&&markerPos!=null){
                var targetPos=mGoogleMap?.projection?.toScreenLocation(markerPos)
                if(lastXPos!=targetPos?.x||lastYPos!=targetPos.y){
                    if(targetPos!=null){
                        popupRootView.x= (targetPos.x - popupXOffset).toFloat()
                        popupRootView.y=(targetPos.y-popupYOffset-markerHeight).toFloat()
                    }
                    lastXPos= targetPos?.x!!
                    lastYPos=targetPos?.y
                }
            }
        }

    }


    override fun onPause() {
        super.onPause()
        visible=false
        Log.d(TAG, "onPause: -----------------------------------VISISBLE ---------------------------$visible")
        popupRootView.viewTreeObserver.removeOnGlobalLayoutListener(infoWindowListener!!)
        handler?.removeCallbacks(positionUpdaterRunnable)
    }


}
