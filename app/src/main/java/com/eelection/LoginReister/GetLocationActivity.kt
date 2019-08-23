package com.eelection.LoginReister

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Pair
import android.view.View
import android.widget.*
import com.eelection.Home.BottomNaviagtionDrawer
import com.eelection.LoginReister.Adapters.LocationAdapter
import com.eelection.LoginReister.PrefManger.PrefManager
import com.eelection.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.firebase.database.*
import java.io.IOException
import java.util.*

class GetLocationActivity : AppCompatActivity() , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    internal var adds: MutableList<Pair<String, String>> = ArrayList()
    lateinit var search: EditText
    lateinit var suggestionrecycle: RecyclerView
    internal var REQUEST_CHECK_SETTINGS = 1
    private var mGoogleApiClient: GoogleApiClient? = null
    lateinit var locationManager: LocationManager
    private var mLocation: Location? = null
    lateinit var searchprog: ProgressBar
    lateinit var recview: View
    lateinit var back: LinearLayout
    lateinit var rest: LinearLayout
    lateinit var curloc: LinearLayout
    lateinit var proglayout: ConstraintLayout
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    internal lateinit var suggestionAdapter: LocationAdapter
    internal var sugglist: MutableList<Pair<String, String>> = ArrayList()
    lateinit var cancel: ImageView
    lateinit var noresult: TextView
    private var prefManager: PrefManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_location)

        prefManager = PrefManager(this)
        if (!prefManager!!.isFirstTimeLaunch()) {
            launchHomeScreen()
            finish()
        }

        addSet()

        recview = findViewById(R.id.recview)
        search = findViewById(R.id.search)
        suggestionrecycle = this.findViewById(R.id.suggestion)
        searchprog = findViewById(R.id.searchprog)
        back = findViewById(R.id.backbt)
        curloc = findViewById(R.id.curloc)
        cancel=findViewById(R.id.cancel)
        noresult=findViewById(R.id.noresult)
        proglayout=findViewById(R.id.proglayout)
        rest=findViewById(R.id.rest)

        back.setOnClickListener { onBackPressed() }

        recyclerSet()

        suggestionrecycle.isNestedScrollingEnabled = false

        cancel.setOnClickListener(View.OnClickListener { search.setText("") })

        search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(st: CharSequence, start: Int, before: Int, count: Int) {

                var newText = st.toString()

                if (newText.length == 0) {
                    suggestionrecycle.visibility = View.GONE
                    recview.visibility = View.GONE

                    sugglist.clear()
                    suggestionrecycle.removeAllViewsInLayout()
                    suggestionAdapter.notifyDataSetChanged()
                    searchprog.setVisibility(View.GONE)
                    cancel.setVisibility(View.GONE)
                    noresult.setVisibility(View.GONE)

                    rest.setVisibility(View.VISIBLE)
                }

                newText = newText.trim { it <= ' ' }
                newText = newText.toLowerCase()

                if (newText.length == 1) {
                    searchprog.setVisibility(View.GONE)
                    cancel.setVisibility(View.VISIBLE)
                    noresult.setVisibility(View.GONE)

                    if (sugglist.size == 0)
                        recview.visibility = View.GONE

                }

                if (newText.length > 1) {

                    searchprog.setVisibility(View.VISIBLE)

                    val ref: DatabaseReference
                    ref = FirebaseDatabase.getInstance().reference.child("Addresses")

                    val finalNewText = newText
                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            var ct = 0

                            for (dataSnapshot1 in dataSnapshot.children) {

                                val actualinst = dataSnapshot1.child("name").getValue().toString()
                                val actualinstid = dataSnapshot1.child("id").getValue().toString()

                                if (actualinst == null)
                                    continue

                                val inst = actualinst.toLowerCase()

                                if (inst.length > finalNewText.length && inst.startsWith(finalNewText)) {

                                    if (ct == 0) {
                                        ct = 1

                                        sugglist.clear()
                                        suggestionrecycle.removeAllViewsInLayout()
                                        suggestionAdapter.notifyDataSetChanged()
                                    }

                                    if (sugglist.size == 0)
                                        sugglist.add(Pair(actualinst, actualinstid))
                                    else
                                        sugglist.add(Pair(actualinst, actualinstid))
                                } else if (inst.length > finalNewText.length && inst.contains(finalNewText)) {

                                    if (ct == 0) {
                                        ct = 1

                                        sugglist.clear()
                                        suggestionrecycle.removeAllViewsInLayout()
                                        suggestionAdapter.notifyDataSetChanged()
                                    }

                                    sugglist.add(Pair(actualinst, actualinstid))
                                } else {
                                    val splited =
                                        finalNewText.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                                    for (aSplited in splited) {

                                        if (inst.length > aSplited.length && inst.contains(aSplited)) {

                                            if (ct == 0) {
                                                ct = 1

                                                sugglist.clear()
                                                suggestionrecycle.removeAllViewsInLayout()
                                                suggestionAdapter.notifyDataSetChanged()
                                            }

                                            sugglist.add(Pair(actualinst, actualinstid))
                                        }
                                    }
                                }
                            }

                            searchprog.setVisibility(View.GONE)
                            cancel.setVisibility(View.VISIBLE)
                            suggestionrecycle.setVisibility(View.VISIBLE);
                            rest.setVisibility(View.GONE)
                            recview.visibility = View.VISIBLE

                            if (sugglist.size == 0) {
                                noresult.setVisibility(View.VISIBLE)
                                recview.visibility = View.VISIBLE
                                noresult.setText("Sorry, we couldn't find result matching " + "\"" + search.text + "\"")
                            } else {
                                recview.visibility = View.VISIBLE
                                noresult.setVisibility(View.GONE)
                            }

                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })

                    suggestionAdapter.notifyDataSetChanged()

                }

            }


        })


        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        curloc = findViewById(R.id.curloc)

        curloc.setOnClickListener {
            enableMyLocationIfPermitted()

            checkLocation()

            startLocationUpdates()
        }

    }

    protected fun startLocationUpdates() {

        proglayout.visibility = View.VISIBLE
        // Create the location request
        val UPDATE_INTERVAL = (2 * 1000).toLong()
        val FASTEST_INTERVAL: Long = 2000
        val mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest, this
        )

    }

    override fun onLocationChanged(location: Location) {

        try {

            val geo = Geocoder(this.applicationContext, Locale.getDefault())
            val addresses = geo.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses.isEmpty()) {
            } else {
                val returnedAddress = addresses[0]
                val add = returnedAddress.postalCode
                var found = "0"

                for (i in adds.indices) {
                    if (adds[i].second.contains(add)) {

                        val pref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                        @SuppressLint("CommitPrefEdits") val searchhint = pref.edit()

                        searchhint.putString("useraddress", adds[i].first)
                        searchhint.putString("useraddressid", adds[i].second)

                        found = "1"

                        searchhint.apply()

                        launchHomeScreen()
                    }
                }

                if (found == "0") {
                    onBackPressed()
                    Toast.makeText(
                        applicationContext,
                        "Sorry, we couldn't find result matching to your current location",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        } catch (e: IOException) {

        }

    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun showAlert() {

        val locationRequest = LocationRequest.create()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build())
        result.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> startLocationUpdates()
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        proglayout.visibility = View.GONE
                        status.startResolutionForResult(this@GetLocationActivity, REQUEST_CHECK_SETTINGS)
                    } catch (e: IntentSender.SendIntentException) {
                    }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> proglayout.visibility = View.GONE
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onConnected(bundle: Bundle?) {

    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    fun recyclerSet(){
        suggestionAdapter = LocationAdapter(sugglist, this)
        val recyceSugg = GridLayoutManager(applicationContext, 1)
        suggestionrecycle.setLayoutManager(recyceSugg)
        recyceSugg.isAutoMeasureEnabled = false
        suggestionrecycle.setItemAnimator(DefaultItemAnimator())
        suggestionrecycle.setAdapter(suggestionAdapter)

    }


    private fun enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates()

                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

                    if (mLocation == null) {
                        startLocationUpdates()
                    }
                    if (mLocation != null) {
                        startLocationUpdates()
                    } else {
                        startLocationUpdates()

                    }

                } else {
                    proglayout.setVisibility(View.GONE)

                }
                return
            }
        }
    }


    fun addSet() {

        val ref: DatabaseReference
        ref = FirebaseDatabase.getInstance().reference.child("Addresses")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataSnapshot1 in dataSnapshot.children) {
                    adds.add(
                        Pair(dataSnapshot1.child("name").getValue().toString(),
                            dataSnapshot1.child("id").getValue().toString()
                        )
                    )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient?.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected()) {
            mGoogleApiClient?.disconnect()
        }
    }

    private fun launchHomeScreen() {
        prefManager!!.setFirstTimeLaunch(false)
        startActivity(Intent(applicationContext, BottomNaviagtionDrawer::class.java))
        finishAffinity()
    }


}
