package com.example.ve441_lifesaver_draft

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ve441_lifesaver_draft.BuildConfig.MAPS_API_KEY

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ve441_lifesaver_draft.databinding.ActivityMapsBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.PolylineOptions
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    // route parameters
    private var start = LatLng(42.293, -83.716) // BBB
    private var end = LatLng(42.281, -83.738) // Rackham

    private var route = ArrayList<LatLng>()

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // set to Hybrid
        GoogleMapOptions().mapType(GoogleMap.MAP_TYPE_HYBRID)

        // enable location layer
        mMap.isMyLocationEnabled = true

//        mMap.setOnMyLocationButtonClickListener(this)
//        mMap.setOnMyLocationClickListener(this)


        with(mMap.uiSettings){
            isCompassEnabled = true
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
        }

        // Add a marker and move the camera
//        val annArbor = LatLng(42.28, -83.74)
//
//        mMap.addMarker(MarkerOptions().position(annArbor).title("Marker in Ann Arbor"))
//        // zoom in closer and move camera
//        mMap.moveCamera(CameraUpdateFactory.zoomTo(15F))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(annArbor))


        mMap.addMarker(MarkerOptions().position(start).title("Marker in BBB"))
        // zoom in closer and move camera
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15F))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start))

        getRoute()
        getPoly()
    }

//    override fun onMyLocationClick(location: Location) {
//        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG)
//            .show()
//    }
//
//    override fun onMyLocationButtonClick(): Boolean {
//        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
//            .show()
//        // Return false so that we don't consume the event and the default behavior still occurs
//        // (the camera animates to the user's current position).
//        return false
//    }

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
        val request = Request.Builder()
            .url(getDirectionUrl())
            .build()

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
            }
        })
    }


    private fun getPoly(){
        var lineOptions = PolylineOptions()
        lineOptions.addAll(route)
        lineOptions
            .width(3F)
            .color(Color.BLUE)
        mMap.addPolyline(lineOptions)
    }

}
