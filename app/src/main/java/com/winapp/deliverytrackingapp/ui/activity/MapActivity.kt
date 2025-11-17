package com.winapp.deliverytrackingapp.ui.activity

import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.winapp.deliverytrackingapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Example: two postal codes (replace with your own)
        val postalCodes = listOf("621112", "621211") // e.g. NYC 10001, SF 94103

        showPostalCodesOnMap(postalCodes)
    }

    private fun showPostalCodesOnMap(postalCodes: List<String>) {
        val geocoder = Geocoder(this, Locale.getDefault())

        // Run geocoding off the main thread
        lifecycleScope.launch {
            val latLngs = mutableListOf<Pair<String, LatLng>>() // pair postalCode -> LatLng

            withContext(Dispatchers.IO) {
                for (code in postalCodes) {
                    try {
                        val results = geocoder.getFromLocationName(code, 1)
                        if (!results.isNullOrEmpty()) {
                            val r = results[0]
                            latLngs.add(code to LatLng(r.latitude, r.longitude))

                            Log.w("codess",""+LatLng(r.latitude, r.longitude))
                        } else {
                            // Keep empty result and report later on UI thread
                            latLngs.add(code to LatLng(Double.NaN, Double.NaN))
                        }
                    } catch (e: Exception) {
                        // exception while geocoding
                        latLngs.add(code to LatLng(Double.NaN, Double.NaN))
                    }
                }
            }

            // Back on main thread: add markers and adjust camera
            val boundsBuilder = LatLngBounds.Builder()
            var validCount = 0
            for ((code, pos) in latLngs) {
                if (pos.latitude.isNaN() || pos.longitude.isNaN()) {
                    Toast.makeText(this@MapActivity, "Could not find: $code", Toast.LENGTH_SHORT).show()
                    continue
                }
                validCount++
                map.addMarker(MarkerOptions().position(pos).title("Postal: $code"))
                val polylineOptions = PolylineOptions()
                    .add(pos)
                    .add(pos)
                    .width(8f)
                    .color(Color.BLUE)

                map.addPolyline(polylineOptions)

                boundsBuilder.include(pos)
                Log.w("locapost",""+pos+"...."+latLngs)

            }

            if (validCount == 0) {
                // nothing valid to show
                Toast.makeText(this@MapActivity, "No postal codes resolved to coordinates.", Toast.LENGTH_LONG).show()
                return@launch
            }
            val origin = LatLng(12.9716, 77.5946)   // Example: Bangalore
            val destination = LatLng(13.0827, 80.2707) // Example: Chennai

            val polylineOptions = PolylineOptions()
                .add(origin)
                .add(destination)
                .width(8f)
                .color(Color.BLUE)

            map.addPolyline(polylineOptions)

            // If only one valid location, zoom to it; otherwise fit bounds
            if (validCount == 1) {
                val single = latLngs.first { !it.second.latitude.isNaN() }
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(single.second, 12f))
            } else {
                val bounds = boundsBuilder.build()
                val padding = (resources.displayMetrics.widthPixels * 0.12).toInt() // 12% padding
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
            }
        }
    }
}