package com.example.a01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions

class MapMarker : AppCompatActivity(), OnMapReadyCallback{

    lateinit var myMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_marker)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(gMap: GoogleMap) {
        myMap = gMap

        val markerOptionsList = listOf(
            MarkerOptions().position(com.google.android.gms.maps.model.LatLng(25.3208949, 82.9087065)).title("Varanasi"),
            MarkerOptions().position(com.google.android.gms.maps.model.LatLng(25.3164, 82.9739)).title("Sarnath"),
            MarkerOptions().position(com.google.android.gms.maps.model.LatLng(25.3356, 83.0127)).title("Assi Ghat"),
        )

        for (markerOptions in markerOptionsList) {
            myMap.addMarker(markerOptions)
        }

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerOptionsList[0].position, 10.0f))

        myMap.setOnMarkerClickListener { marker ->
            marker.showInfoWindow()
            true
        }
    }
}