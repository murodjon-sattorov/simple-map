package uz.murodjon_sattorov.yotoqxona_task

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import uz.murodjon_sattorov.yotoqxona_task.databinding.ActivityMainBinding
import uz.murodjon_sattorov.yotoqxona_task.databinding.BottomSheetDialogBinding

class MainActivity : AppCompatActivity(), PermissionsListener {

    private lateinit var mainBinding: ActivityMainBinding

    private var mapboxMap: MapboxMap? = null

    private var googleApiClient: GoogleApiClient? = null
    private val REQUESTLOCATION = 199

    private var permissionsManager: PermissionsManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        enableLoc()
        checkPermission()

        mainBinding.mapboxView.onCreate(savedInstanceState)

        Log.d("TBT", "onCreate: 1")
        mainBinding.mapboxView.getMapAsync { mapboxMap ->
            Log.d("TBT", "onCreate: 2")
            this.mapboxMap = mapboxMap
            getCameraPosition()
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {

                enableLocationComponent(it)

                for (i in MapList.allMark.indices) {
                    mapboxMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                MapList.allMark[i][0],
                                MapList.allMark[i][1]
                            )
                        )
                            .title(MapList.getTitle[i])
                    )
                    mapboxMap.setOnMarkerClickListener(object : MapboxMap.OnMarkerClickListener {
                        override fun onMarkerClick(marker: Marker): Boolean {

                            val bottomSheetDialog = BottomSheetDialog(this@MainActivity)

                            val bottomSheetDialogBinding = BottomSheetDialogBinding.bind(
                                LayoutInflater.from(this@MainActivity)
                                    .inflate(R.layout.bottom_sheet_dialog, findViewById(R.id.bottom_sheet_container))
                            )

                            bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)
                            bottomSheetDialog.show()

                            return true
                        }

                    })
                }

            }

        }


    }

    private fun checkPermission() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("TBT", "checkPermission: 1")
            getCameraPosition()

        } else {
            Log.d("TBT", "checkPermission: 2")
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {

                Log.d("TBT", "isGranted: $isGranted")
                getCameraPosition()

            } else {
                Log.d("TBT", "isGranted: $isGranted")
            }
        }

    private fun getCameraPosition() {
        val position = CameraPosition.Builder()
            .target(LatLng(41.299496, 69.240074))
            .zoom(10.0)
            .tilt(20.0)
            .build()

        Log.d("TBT", "getCameraPosition: ")
        mapboxMap?.animateCamera(CameraUpdateFactory.newCameraPosition(position), 4000)
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            val locationComponent = mapboxMap?.locationComponent!!

            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(this, loadedMapStyle).build()
            )

            locationComponent.isLocationComponentEnabled = true

            locationComponent.cameraMode = CameraMode.TRACKING

            locationComponent.renderMode = RenderMode.COMPASS

            Log.d(
                "TAT",
                "enableLocationComponent: ${locationComponent.lastKnownLocation?.latitude} and ${locationComponent.lastKnownLocation?.longitude}"
            )

            getCameraPosition()

        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager?.requestLocationPermissions(this)
        }
    }

    private fun enableLoc() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(bundle: Bundle?) {}
                override fun onConnectionSuspended(i: Int) {
                    googleApiClient?.connect()
                }
            })
            .addOnConnectionFailedListener {
            }.build()
        googleApiClient?.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000.toLong()
        locationRequest.fastestInterval = 5 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result: PendingResult<LocationSettingsResult> =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(
                        this,
                        REQUESTLOCATION
                    )
                } catch (e: IntentSender.SendIntentException) {
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUESTLOCATION -> when (resultCode) {
                Activity.RESULT_OK -> {
//                    getCameraPosition()
                    mainBinding.mapboxView.getMapAsync { mapboxMap ->
                        Log.d("TBT", "onCreate: 2")
                        this.mapboxMap = mapboxMap
                        getCameraPosition()
                        mapboxMap.setStyle(Style.MAPBOX_STREETS) {

                            enableLocationComponent(it)

                        }

                    }
                }
                Activity.RESULT_CANCELED -> enableLoc()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("TBT", "onStart: ")
        mainBinding.mapboxView.onStart()
    }

    override fun onResume() {
        super.onResume()
        Log.d("TBT", "onResume: ")
        mainBinding.mapboxView.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("TBT", "onPause: ")
        mainBinding.mapboxView.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.d("TBT", "onStop: ")
        mainBinding.mapboxView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("TBT", "onSaveInstanceState: ")
        mainBinding.mapboxView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d("TBT", "onLowMemory: ")
        mainBinding.mapboxView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TBT", "onDestroy: ")
        mainBinding.mapboxView.onDestroy()
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {

    }

    override fun onPermissionResult(granted: Boolean) {

    }

}