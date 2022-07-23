package com.example.ve441_lifesaver_draft

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ve441_lifesaver_draft.BuildConfig.MAPS_API_KEY

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ve441_lifesaver_draft.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.PolylineOptions
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    // route parameters, start also use to store current position
    private var start = LatLng(42.293, -83.716) // BBB
    private var end = LatLng(42.281, -83.738) // Rackham

    // useful for get location info
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var route = ArrayList<LatLng>()

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    // do not give warning when no permission guaranteed
    // triggered when map is ready
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // set to Hybrid
        GoogleMapOptions().mapType(GoogleMap.MAP_TYPE_HYBRID)

        // enable location layer
        mMap.isMyLocationEnabled = true
        mMap.setOnMyLocationButtonClickListener(this)


//        mMap.setOnMyLocationClickListener(this)


        with(mMap.uiSettings){
            isCompassEnabled = true
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
        }

        // zoom in closer and move camera to default location
        updateMapLocation(start)

        val getRouteButton = binding.buttonMapAction
        getRouteButton.setOnClickListener{
            println("Debug---------> Click get route")
            getRoute()
        }
    }


    // https://developers.google.com/maps/documentation/directions/get-directions
    private fun getDirectionUrl(): String{
        // origin and destination
        val origin = "origin=" + start.latitude + "," + start.longitude
        val dest = "destination=" + end.latitude + "," + end.longitude
        val sensor = "sensor=false"

        // driving mode or walking mode
        val mode = "mode=driving"
        val key = "key=$MAPS_API_KEY"
        val para = "$origin&$dest&$sensor&$mode&$key"

        // output format, xml or json
        val output = "json"
        val url: String = "https://maps.googleapis.com/maps/api/directions/$output?$para"

        println("getDirectionUrl---> $url")
        return url
    }


    private fun getRoute(){
        println("Debug------> getRoute start")
        val request = Request.Builder()
            .url(getDirectionUrl())
            .build()

        // execute for synchronous
        // enqueue for synchronous request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("getRoute", "Failed api request")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var jRoutes : JSONArray? = null
                    var jLegs : JSONArray? = null
                    var jSteps : JSONArray? = null

                    // get routes
                    jRoutes = try { JSONObject(response.body?.string() ?: "")
                        .getJSONArray("routes") } catch (e: JSONException) { JSONArray() }

                    // prepare for add route
                    route.clear()

                    jLegs = JSONObject(jRoutes!![0].toString()?:"").getJSONArray("legs")
                    jSteps = JSONObject(jLegs!![0].toString()?:"").getJSONArray("steps")

                    // start location
                    var stepEntry = jSteps[0] as JSONObject
                    var point = stepEntry.getJSONObject("start_location")
                    route.add(LatLng(point.getDouble("lat"), point.getDouble("lng")))

                    for (i in 0 until  jSteps!!.length()){
                        stepEntry = jSteps[i] as JSONObject
                        point = stepEntry.getJSONObject("end_location")
                        route.add(LatLng(point.getDouble("lat"), point.getDouble("lng")))
                    }
                }
                runOnUiThread{
                    getPoly()
                }
                println("Debug ------> response end")
            }
        })

        println("Debug----> getRoute ends")
    }


    private fun getPoly(){
        println("Debug---> getPoly")
        var lineOptions = PolylineOptions()
        lineOptions.addAll(route)
        lineOptions
            .width(3F)
            .color(Color.BLUE)
        mMap.addPolyline(lineOptions)

        mMap.addMarker(MarkerOptions().position(start).title("Origin"))
        mMap.addMarker(MarkerOptions().position(end).title("Destination"))
    }


    // triggered when my location button is clicked
    // update $start to current location
    @SuppressLint("MissingPermission")
    override fun onMyLocationButtonClick(): Boolean {
        // get current location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                updateMapLocation(location)
                start = LatLng(location!!.latitude, location!!.longitude)
            }
        println("Debug ------> My location button clicked")
        println("Debug ------> set start location1 as $start")
        return false
    }


    // https://medium.com/@paultr/google-maps-for-android-pt-2-user-location-f7416966aa67
    // TODO: Try to continuously update location


    // helper functions
    // move camera to specific location (latLng)
    private fun updateMapLocation(location: Location?) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(
            location?.latitude ?: 0.0,
            location?.longitude ?: 0.0)))

        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f))
    }
    private fun updateMapLocation(coordinate: LatLng?) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate))

        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f))
    }

}
