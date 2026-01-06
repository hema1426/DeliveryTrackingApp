package com.winapp.deliverytrackingapp.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.winapp.deliverytrackingapp.R
import kotlinx.coroutines.*
import java.util.Locale

class MapsActivityCopy : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var tvDistance: TextView
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(this, "Location permission denied (not required to geocode)", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        tvDistance = findViewById(R.id.tvDistance)

        // Ask for fine location if you want 'my location' enabled (optional)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Optional: enable my-location if permission granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        }
        // Example postal codes — change to your inputs (or pass via UI/intent)
//        private val postal1 = "621112"    // example: New York ZIP
//        private val postal2 = "621211"    // example: San Francisco ZIP

        val fromZipcode = intent.getStringExtra("from_zipcode")
        val toZipcode = intent.getStringExtra("to_zipcode")

        if (fromZipcode != null && toZipcode != null) {
            drawPolylineBetweenPostcodes(fromZipcode, toZipcode)
        }
    }

    private fun drawPolylineBetweenPostcodes(codeA: String, codeB: String) {
        scope.launch {
            val coords = withContext(Dispatchers.IO) {
                val geocoder = Geocoder(this@MapsActivityCopy, Locale.getDefault())
                val a = geocodePostcode(geocoder, codeA)
                val b = geocodePostcode(geocoder, codeB)
                Pair(a, b)
            }

            val locA = coords.first
            val locB = coords.second

            if (locA == null || locB == null) {
                Toast.makeText(this@MapsActivityCopy, "Could not resolve one or both postal codes", Toast.LENGTH_LONG).show()
                return@launch
            }

            // Clear previous
            map.clear()

            // Add markers
            val markerA = map.addMarker(MarkerOptions().position(locA).title("Postal: $codeA"))
            val markerB = map.addMarker(MarkerOptions().position(locB).title("Postal: $codeB"))

            // Draw polyline
            val polylineOptions = PolylineOptions()
                .add(locA, locB)
                .width(8f)
                .geodesic(true)
            map.addPolyline(polylineOptions)

            // Compute distance (meters)
            val results = FloatArray(1)
            Location.distanceBetween(
                locA.latitude, locA.longitude,
                locB.latitude, locB.longitude,
                results
            )
            val meters = results[0]
            val km = meters / 1000.0

            // Show distance
            tvDistance.text = String.format(Locale.getDefault(), "Distance: %.0f m (%.2f km)", meters, km)

            // Zoom so both markers fit
            val boundsBuilder = LatLngBounds.Builder()
            boundsBuilder.include(locA)
            boundsBuilder.include(locB)
            val padding = 120 // px padding
            val bounds = boundsBuilder.build()
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))

            // Add info window on polyline midpoint
            val midLat = (locA.latitude + locB.latitude) / 2.0
            val midLng = (locA.longitude + locB.longitude) / 2.0
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(midLat, midLng))
                    .title(String.format(Locale.getDefault(), "%.2f km", km))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )?.showInfoWindow()
        }
    }

    private fun geocodePostcode(geocoder: Geocoder, postal: String): LatLng? {
        try {
            // getFromLocationName may return multiple addresses; take first
            val addresses = geocoder.getFromLocationName(postal, 5)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                return LatLng(address.latitude, address.longitude)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
