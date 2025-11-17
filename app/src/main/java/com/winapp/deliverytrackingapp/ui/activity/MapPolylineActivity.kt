//package com.winapp.deliverytrackingapp.ui.activity
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.location.Geocoder
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.lifecycleScope
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.gms.maps.model.PolylineOptions
//import com.winapp.deliverytrackingapp.R
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import java.util.Locale
//
//class MapPolylineActivity : AppCompatActivity(), OnMapReadyCallback {
//
//    private var googleMapRef: GoogleMap? = null
//
//    // Example postal codes (replace with dynamic values as needed)
//    private val postal1 = "110001"   // e.g., Delhi GPO pin code
//    private val postal2 = "560001"   // e.g., Bangalore GPO pin code
//
//    private val requestPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
//            if (granted) {
//                if (ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return
//                }
//                googleMapRef?.isMyLocationEnabled = true
//            }
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_map)
//
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.mapFragment) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//    }
//
//    override fun onMapReady(map: GoogleMap) {
//        googleMapRef = map
//        checkLocationPermission()
//
//        // do geocoding and drawing
//        drawPolylineBetweenPostcodes(postal1, postal2)
//    }
//
//    private fun checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//            == PackageManager.PERMISSION_GRANTED) {
//            googleMapRef?.isMyLocationEnabled = true
//        } else {
//            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }
//
//    private fun drawPolylineBetweenPostcodes(pc1: String, pc2: String) {
//        lifecycleScope.launchWhenStarted {
//            // Geocode off main thread
//            val coords = withContext(Dispatchers.IO) {
//                try {
//                    val geocoder = Geocoder(this@MapPolylineActivity, Locale.getDefault())
//                    val list1 = geocoder.getFromLocationName(pc1, 1)
//                    val list2 = geocoder.getFromLocationName(pc2, 1)
//
//                    val latLng1 = if (list1 != null && list1.isNotEmpty()) {
//                        LatLng(list1[0].latitude, list1[0].longitude)
//                    } else null
//
//                    val latLng2 = if (list2 != null && list2.isNotEmpty()) {
//                        LatLng(list2[0].latitude, list2[0].longitude)
//                    } else null
//
//                    Pair(latLng1, latLng2)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    Pair<LatLng?, LatLng?>(null, null)
//                }
//            }
//
//            val loc1 = coords.first
//            val loc2 = coords.second
//
//            if (loc1 != null && loc2 != null) {
//                addMarkersAndPolyline(loc1, loc2)
//            } else {
//                // Geocoder failed — you can fall back to Google Geocoding API here
//                Toast.makeText(
//                    this@MapPolylineActivity,
//                    "Could not geocode one or both postcodes. Try network fallback.",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//    }
//
//    private fun addMarkersAndPolyline(a: LatLng, b: LatLng) {
//        val map = googleMapRef ?: return
//
//        map.clear()
//        map.addMarker(MarkerOptions().position(a).title("Postal 1"))
//        map.addMarker(MarkerOptions().position(b).title("Postal 2"))
//
//        val boundsBuilder = com.google.android.gms.maps.model.LatLngBounds.builder()
//        boundsBuilder.include(a).include(b)
//
//        // draw polyline
//        val polylineOptions = PolylineOptions()
//            .add(a)
//            .add(b)
//            .width(8f)          // thickness in pixels
//            // .color(...)       // do not hardcode colors unless you want; uncomment if desired
//            .geodesic(true)
//
//        map.addPolyline(polylineOptions)
//
//        // move camera to show both points
//        val bounds = boundsBuilder.build()
//        val padding = 150 // px
//        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
//    }
//}
