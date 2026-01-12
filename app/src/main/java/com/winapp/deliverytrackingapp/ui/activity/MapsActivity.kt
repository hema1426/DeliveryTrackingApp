package com.winapp.deliverytrackingapp.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.winapp.deliverytrackingapp.R
import kotlinx.coroutines.*
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvDistance: TextView
    private lateinit var btnOpenGoogleMaps: Button

    private var fromLatLng: LatLng? = null
    private var toLatLng: LatLng? = null
    private var distanceKm: Double = 0.0

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) fetchCurrentLocation()
            else Toast.makeText(this, "Location permission required", Toast.LENGTH_LONG).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        tvDistance = findViewById(R.id.tvDistance)
        btnOpenGoogleMaps = findViewById(R.id.btnOpenGoogleMaps)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btnOpenGoogleMaps.setOnClickListener {
            openGoogleMapsApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        checkPermission()
    }

    /* ---------- PERMISSION ---------- */

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchCurrentLocation()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun isGpsEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /* ---------- LOCATION ---------- */

    private fun fetchCurrentLocation() {

        if (!isGpsEnabled()) {
            Toast.makeText(this, "Please turn ON location", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            return
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        googleMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                fromLatLng = LatLng(location.latitude, location.longitude)
                resolveZipAndDrawRoute()
            } else {
                Toast.makeText(this, "Unable to fetch current location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /* ---------- ZIP + MAP ---------- */

    private fun resolveZipAndDrawRoute() {

        val toZipcode = intent.getStringExtra("to_zipcode")
        val from = fromLatLng ?: return
        Log.w("tozipcode","$toZipcode")

        if (toZipcode.isNullOrEmpty()) {
            Toast.makeText(this, "Destination zipcode missing", Toast.LENGTH_SHORT).show()
            return
        }

        scope.launch {
            val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())

            toLatLng = withContext(Dispatchers.IO) {
                getLatLngFromZip(geocoder, toZipcode)
            }

            if (toLatLng == null) {
                Toast.makeText(this@MapsActivity, "Unable to locate destination", Toast.LENGTH_LONG).show()
                return@launch
            }

            drawRoute(from, toLatLng!!)
        }
    }

    private fun drawRoute(start: LatLng, end: LatLng) {

        googleMap.clear()

        googleMap.addMarker(MarkerOptions().position(start).title("Current Location"))
        googleMap.addMarker(MarkerOptions().position(end).title("Destination"))

        googleMap.addPolyline(
            PolylineOptions()
                .add(start, end)
                .width(8f)
                .color(0xFF1976D2.toInt())
                .geodesic(true)
        )

        val results = FloatArray(1)
        Location.distanceBetween(
            start.latitude, start.longitude,
            end.latitude, end.longitude,
            results
        )

        distanceKm = results[0] / 1000.0
        tvDistance.text = String.format(Locale.getDefault(), "Distance: %.2f km", distanceKm)

        val bounds = LatLngBounds.Builder()
            .include(start)
            .include(end)
            .build()

        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120))
    }

    /* ---------- GOOGLE MAPS ---------- */

    private fun openGoogleMapsApp() {

        val from = fromLatLng ?: return
        val to = toLatLng ?: return

        val uri = Uri.parse(
            "https://www.google.com/maps/dir/?api=1" +
                    "&origin=${from.latitude},${from.longitude}" +
                    "&destination=${to.latitude},${to.longitude}" +
                    "&travelmode=driving"
        )

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    /* ---------- GEO ---------- */

    private fun getLatLngFromZip(geocoder: Geocoder, zipcode: String): LatLng? {
        return try {
            val addresses = geocoder.getFromLocationName(zipcode, 1)
            if (!addresses.isNullOrEmpty()) {
                LatLng(addresses[0].latitude, addresses[0].longitude)
            } else null
        } catch (e: Exception) {
            null
        }
    }
}