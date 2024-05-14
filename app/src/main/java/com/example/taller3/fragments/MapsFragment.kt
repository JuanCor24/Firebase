package com.example.taller3.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.taller3.R
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment:  Fragment(), SensorEventListener {

    private lateinit var gMap: GoogleMap
    val sydney = LatLng(-34.0, 151.0)
    private lateinit var dogMarker: Marker
    var zoomLevel = 15.0f
    private val rutaCoordinates = mutableListOf<LatLng>()
    var moveCamera = true
    private lateinit var sensorManager: SensorManager
    private lateinit var lightSensor: Sensor
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = false
        gMap.uiSettings.isCompassEnabled = true



        gMap.setMapStyle(
            context?.let { MapStyleOptions.loadRawResourceStyle(it, R.raw.map_day) })

        dogMarker = gMap.addMarker(
            MarkerOptions().position(sydney).title("Hey Dog!")
                .icon(context?.let { bitmapDescriptorFromVector(it, R.drawable.baseline_location_pin_24) })
        )!!
        gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)) }


    fun updateCamera(cameraUpdate: CameraUpdate) {
        gMap.animateCamera(cameraUpdate)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (this::gMap.isInitialized) {
            if (event!!.values[0] > 100) {
                gMap.setMapStyle(
                    context?.let { MapStyleOptions.loadRawResourceStyle(it, R.raw.map_day) })
            } else {
                gMap.setMapStyle(
                    context?.let { MapStyleOptions.loadRawResourceStyle(it, R.raw.map_night) })
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //Do Nothing
    }

    //From https://stackoverflow.com/questions/42365658/custom-marker-in-google-maps-in-android-with-vector-asset-icon
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }


}