package uz.murodjon_sattorov.yotoqxona_task.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
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
import uz.murodjon_sattorov.yotoqxona_task.MapList
import uz.murodjon_sattorov.yotoqxona_task.Model
import uz.murodjon_sattorov.yotoqxona_task.R
import uz.murodjon_sattorov.yotoqxona_task.adapter.ImagesAdapter
import uz.murodjon_sattorov.yotoqxona_task.databinding.ActivityMainBinding
import uz.murodjon_sattorov.yotoqxona_task.databinding.BottomSheetDialogBinding
import uz.murodjon_sattorov.yotoqxona_task.fragment.SignUpFragment
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), PermissionsListener {

    private lateinit var mainBinding: ActivityMainBinding

    private var mapboxMap: MapboxMap? = null

    private var googleApiClient: GoogleApiClient? = null
    private val REQUESTLOCATION = 199

    private var permissionsManager: PermissionsManager? = null

    private var adapter = ImagesAdapter()

    private var data0 = ArrayList<Model>()
    private var data1 = ArrayList<Model>()
    private var data2 = ArrayList<Model>()
    private var data3 = ArrayList<Model>()
    private var data4 = ArrayList<Model>()
    private var data5 = ArrayList<Model>()
    private var data6 = ArrayList<Model>()
    private var data7 = ArrayList<Model>()
    private var data8 = ArrayList<Model>()
    private var data9 = ArrayList<Model>()
    private var data10 = ArrayList<Model>()
    private var data11 = ArrayList<Model>()
    private var data12 = ArrayList<Model>()
    private var data13 = ArrayList<Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        enableLoc()
        checkPermission()

        mainBinding.mapboxView.onCreate(savedInstanceState)

        loadImageData()

        getNightOrDay()


    }


    private fun getNightOrDay() {
        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                mainBinding.mapboxView.getMapAsync { mapboxMap ->
                    Log.d("TBT", "onCreate: 2")
                    this.mapboxMap = mapboxMap
                    getCameraPosition()
                    mapboxMap.setStyle(Style.DARK) {

                        enableLocationComponent(it)
                        setMarkAndBottomSheetNight(mapboxMap)

                    }

                }
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                mainBinding.mapboxView.getMapAsync { mapboxMap ->
                    Log.d("TBT", "onCreate: 2")
                    this.mapboxMap = mapboxMap
                    getCameraPosition()
                    mapboxMap.setStyle(Style.MAPBOX_STREETS) {

                        enableLocationComponent(it)
                        setMarkAndBottomSheetDay(mapboxMap)

                    }

                }
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                mainBinding.mapboxView.getMapAsync { mapboxMap ->
                    Log.d("TBT", "onCreate: 2")
                    this.mapboxMap = mapboxMap
                    getCameraPosition()
                    mapboxMap.setStyle(Style.MAPBOX_STREETS) {

                        enableLocationComponent(it)
                        setMarkAndBottomSheetDay(mapboxMap)

                    }

                }
            }
        }
    }

    private fun setMarkAndBottomSheetNight(mapboxMap: MapboxMap) {
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
                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onMarkerClick(marker: Marker): Boolean {

                    val bottomSheetDialog = BottomSheetDialog(
                        this@MainActivity,
                        R.style.BottomBgNight
                    )

                    val bottomSheetDialogBinding = BottomSheetDialogBinding.bind(
                        LayoutInflater.from(this@MainActivity)
                            .inflate(
                                R.layout.bottom_sheet_dialog,
                                findViewById(R.id.bottom_sheet_container)
                            )
                    )

                    bottomSheetDialogBinding.recyclerView.layoutManager = LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )

                    when (marker.id.toInt()) {
                        0 -> adapter.addData(data0)
                        1 -> adapter.addData(data1)
                        2 -> adapter.addData(data2)
                        3 -> adapter.addData(data3)
                        4 -> adapter.addData(data4)
                        5 -> adapter.addData(data5)
                        6 -> adapter.addData(data6)
                        7 -> adapter.addData(data7)
                        8 -> adapter.addData(data8)
                        9 -> adapter.addData(data9)
                        10 -> adapter.addData(data10)
                        11 -> adapter.addData(data11)
                        12 -> adapter.addData(data12)
                        13 -> adapter.addData(data13)
                    }

                    bottomSheetDialogBinding.recyclerView.adapter = adapter

                    bottomSheetDialogBinding.root.maxHeight = bottomSheetDialogDefaultHeight()

                    bottomSheetDialogBinding.bottomSheetContainer.setBackgroundColor(
                        resources.getColor(
                            android.R.color.transparent
                        )
                    )
                    bottomSheetDialogBinding.bottomSheetBg.background =
                        getDrawable(R.drawable.dialog_bg_night)
                    bottomSheetDialogBinding.texts.text = marker.title

                    bottomSheetDialogBinding.loginBtn.setOnClickListener {
                        val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
                        startActivity(intent)
                    }

                    bottomSheetDialogBinding.drawNavigate.setOnClickListener {
                        Toast.makeText(
                            this@MainActivity,
                            "Bosilishlar keyingi versiyalarda ishga tushuriladi))",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    bottomSheetDialogBinding.shareMap.setOnClickListener {
                        Toast.makeText(
                            this@MainActivity,
                            "Bosilishlar keyingi versiyalarda ishga tushuriladi))",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)
                    bottomSheetDialog.show()

                    return true
                }

            })
        }
    }

    private fun setMarkAndBottomSheetDay(mapboxMap: MapboxMap) {
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
                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onMarkerClick(marker: Marker): Boolean {

                    val bottomSheetDialog = BottomSheetDialog(
                        this@MainActivity,
                        R.style.BottomBgNight
                    )

                    val bottomSheetDialogBinding = BottomSheetDialogBinding.bind(
                        LayoutInflater.from(this@MainActivity)
                            .inflate(
                                R.layout.bottom_sheet_dialog,
                                findViewById(R.id.bottom_sheet_container)
                            )
                    )

                    bottomSheetDialogBinding.recyclerView.layoutManager = LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )

                    when (marker.id.toInt()) {
                        0 -> adapter.addData(data0)
                        1 -> adapter.addData(data1)
                        2 -> adapter.addData(data2)
                        3 -> adapter.addData(data3)
                        4 -> adapter.addData(data4)
                        5 -> adapter.addData(data5)
                        6 -> adapter.addData(data6)
                        7 -> adapter.addData(data7)
                        8 -> adapter.addData(data8)
                        9 -> adapter.addData(data9)
                        10 -> adapter.addData(data10)
                        11 -> adapter.addData(data11)
                        12 -> adapter.addData(data12)
                        13 -> adapter.addData(data13)
                    }



                    bottomSheetDialogBinding.recyclerView.adapter = adapter

                    bottomSheetDialogBinding.root.maxHeight = bottomSheetDialogDefaultHeight()

                    bottomSheetDialogBinding.bottomSheetContainer.setBackgroundColor(
                        resources.getColor(
                            android.R.color.transparent
                        )
                    )
                    bottomSheetDialogBinding.bottomSheetBg.background =
                        getDrawable(R.drawable.dialog_bg_day)

                    bottomSheetDialogBinding.texts.text = marker.title

                    bottomSheetDialogBinding.loginBtn.setOnClickListener {
                        val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
                        startActivity(intent)
                    }

                    bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)
                    bottomSheetDialog.show()

                    return true
                }

            })
        }
    }

    var s = ""
    fun sendData(s: String) {
        this.s = s
    }

    fun getSendData(): String {
        return s
    }

    private fun bottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight()
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (this as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
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
                    getNightOrDay()
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

    private fun loadImageData() {
        data0.add(
            Model(
                0,
                "https://avatars.mds.yandex.net/get-altay/1992662/2a0000016fd51c483a39828731cf324b9475/XXXL"
            )
        )
        data0.add(
            Model(
                0,
                "https://avatars.mds.yandex.net/get-altay/4581272/2a0000017932a852ac426721947b413af6d4/XXXL"
            )
        )
        data0.add(
            Model(
                0,
                "https://avatars.mds.yandex.net/get-altay/4080739/2a0000017925f8f2050109925f4e32bc3bfd/XXXL"
            )
        )
        data0.add(
            Model(
                0,
                "https://mybooking.uz/uploads/hotel/images/755_1555052549q41E_1024x768.jpg"
            )
        )
        data0.add(
            Model(
                0,
                "https://avatars.mds.yandex.net/get-altay/4304228/2a000001792591f33321ee8a2a7ddc574584/XXXL"
            )
        )
        data0.add(
            Model(
                0,
                "https://avatars.mds.yandex.net/get-altay/4079915/2a00000179430baf810fcf4262fb8dd3c8ea/XXXL"
            )
        )
        data0.add(
            Model(
                0,
                "https://avatars.mds.yandex.net/get-altay/2397657/2a00000174e2b3fbb06c21300d25f2d5bdf5/XXXL"
            )
        )
        data1.add(
            Model(
                1,
                "https://avatars.mds.yandex.net/get-altay/1622057/2a00000169326f15f062abde8dc088c8bd35/XXXL"
            )
        )
        data1.add(
            Model(
                1,
                "https://avatars.mds.yandex.net/get-altay/2799320/2a00000174df189c3ad0b2cac29c040fb836/XXXL"
            )
        )
        data1.add(Model(1, "https://i.postimg.cc/8cWTByrh/image-2021-10-27-00-44-41.png"))
        data1.add(Model(1, "http://tf.tdpu.uz/upload/5987_18265.jpg"))
        data2.add(
            Model(
                2,
                "https://avatars.mds.yandex.net/get-altay/1886165/2a0000016db4d47deee8c8da3e9c72ec546b/XXXL"
            )
        )
        data2.add(
            Model(
                2,
                "https://avatars.mds.yandex.net/get-altay/4633583/2a00000179323104fdfc2f01fc07b033bb56/XXXL"
            )
        )
        data2.add(
            Model(
                2,
                "https://avatars.mds.yandex.net/get-altay/4581272/2a0000017925c1781e64028806d1703648d1/XXXL"
            )
        )
        data2.add(
            Model(
                2,
                "http://tfi.uz/photos/1/photos/IMG_0925.JPG"
            )
        )
        data2.add(
            Model(
                2,
                "https://avatars.mds.yandex.net/get-altay/4804646/2a000001794963cd0c96456bac96250e5b6a/XXXL"
            )
        )
        data2.add(
            Model(
                2,
                "https://avatars.mds.yandex.net/get-altay/4336337/2a00000179432ca9bcfd27ca0dac7d621e65/XXXL"
            )
        )
        data2.add(
            Model(
                2,
                "https://avatars.mds.yandex.net/get-altay/2383444/2a00000174e1a3fe2a438f3f31ba10dce2c5/XXXL"
            )
        )
        data3.add(
            Model(
                3,
                "https://avatars.mds.yandex.net/get-altay/2094876/2a0000016d8211097deee5f3bbe12eab5f60/L"
            )
        )
        data3.add(Model(3, "https://www.sbtsue.uz/frontend/web/news/613b54d011d42.jpg"))
        data3.add(Model(3, "http://tdpu.uz/upload/6079_18204.jpg"))
        data3.add(Model(3, "http://storage.kun.uz/source/uploads/2016iyulavgust/rj8.jpg"))
        data3.add(Model(3, "http://tiiame.uz//storage/web/source/1/Erkin/TTJ/14.jpg"))
        data3.add(
            Model(
                3,
                "https://i2.wp.com/kakiedoki.ru/wp-content/uploads/2018/05/11409100866_3570eaaf30_o.jpg"
            )
        )
        data3.add(Model(3, "http://tdpu.uz/upload/6079_18197.jpg"))

        data4.add(
            Model(
                4,
                "https://storage.kun.uz/source/thumbnails/_medium/7/hhK6cr3i4r58dGlWfV3deqtR0zLq6YOC_medium.jpg"
            )
        )
        data4.add(Model(4, "https://storage.kun.uz/source/4/CWVPtmIIrUpc7vBpjBfdsg9tunZCCRJl.jpg"))
        data4.add(Model(4, "https://buxdu.uz/images/ttj/5.jpg"))
        data4.add(Model(4, "http://tfi.uz/photos/1/photos/DSC02278.JPG"))
        data4.add(Model(4, "https://buxdu.uz/images/ttj/1.jpg"))
        data4.add(Model(4, "http://www.samdu.uz/public/images/image012%281%29.jpg"))
        data4.add(Model(4, "https://buxdu.uz/images/ttj/7.jpg"))

        data5.add(
            Model(
                5,
                "https://avatars.mds.yandex.net/get-altay/1546239/2a0000016e97dd231547fc03b9ea8c03ffec/XXXL"
            )
        )
        data5.add(Model(5, "http://tfi.uz/photos/1/photos/ttj/ttj1/IMG_3164.JPG"))
        data5.add(
            Model(
                5,
                "https://tfi.uz/photos/14/news-2021/00mahalla/photo_2021-04-05_17-58-26.jpg"
            )
        )
        data5.add(Model(5, "http://tfi.uz/photos/1/photos/IMG_0926.JPG"))
        data5.add(Model(5, "https://samarkand-vtc.uz/wp-content/uploads/2018/11/55.jpg"))
        data5.add(Model(5, "https://buxdu.uz/images/ttj/5.jpg"))
        data5.add(Model(5, "https://telegra.ph/file/7efbb26eb2f4d0e7d8819.png"))

        data6.add(Model(6, "http://tfi.uz/photos/1/photos/ttj/ttj3/IMG_0552.JPG"))
        data6.add(Model(6, "http://tfi.uz/photos/1/photos/ttj/ttj3/IMG_0558.JPG"))
        data6.add(Model(6, "https://samdchti.uz/images/source/news/2019/5/10/5.jpg"))
        data6.add(Model(6, "http://www.samdu.uz/public/images/x2%281%29.jpg"))
        data6.add(Model(6, "https://samdchti.uz/images/source/news/2019/5/10/8.jpg"))
        data6.add(Model(6, "http://tfi.uz/photos/1/photos/ttj/ttj3/IMG_3213.JPG"))
        data6.add(Model(6, "http://tfi.uz/photos/1/photos/ttj/ttj2/IMG_3158.JPG"))

        data7.add(
            Model(
                7,
                "https://avatars.mds.yandex.net/get-altay/1595534/2a0000016bff8e350c814f2b794b006c8251/XXXL"
            )
        )
        data7.add(
            Model(
                7,
                "https://lh3.googleusercontent.com/proxy/iWqJSamInxoQWQvpCu-U4Rb4ZUaWd85qAvXlOZ8efAXsDaIclEJuIlVecswDNEU-gnbiwH6piqHI9oqd9g8oycC20jXFAXsCqnrZ0iKsSZc"
            )
        )
        data7.add(Model(7, "http://tdpu.uz/upload/7144_20332.jpg"))
        data7.add(
            Model(
                7,
                "https://i0.wp.com/mr-build.ru/wp-content/uploads/5/5/6/55692c6744999b141bf27013d01078e4.jpg"
            )
        )
        data7.add(
            Model(
                7,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQszOOSPv7gOXCXYa1wiV8qCc08NYTNIlf2Pzy0LJZKrRsYSIJa0qywKcmaXc8JAZ7XSJA&usqp=CAU"
            )
        )
        data7.add(Model(7, "http://tdpu.uz/upload/6079_18197.jpg"))
        data7.add(Model(7, "https://buxdu.uz/images/ttj/1.jpg"))
        data7.add(Model(7, "https://buxdu.uz/images/ttj/7.jpg"))

        data8.add(
            Model(
                8,
                "https://static-pano.maps.yandex.ru/v1/?panoid=1487094095_804076290_23_1570359403&size=500%2C240&azimuth=-153.1&tilt=10&api_key=maps&signature=xUtYcfdwtjhzCy8T1RkLcHnlO68w21S5kHqO63pyhWQ="
            )
        )
        data8.add(Model(8, "http://tiiame.uz//storage/web/source/1/Erkin/TTJ/14.jpg"))
        data8.add(Model(8, "http://tiiame.uz//storage/web/source/1/Erkin/TTJ/13.jpg"))
        data8.add(Model(8, "https://i.ytimg.com/vi/8-tKhSxFDO0/maxresdefault.jpg"))
        data8.add(Model(8, "http://tf.tdpu.uz/upload/5987_18265.jpg"))
        data8.add(Model(8, "http://tiiame.uz//storage/web/source/1/Erkin/TTJ/12.jpg"))
        data8.add(
            Model(
                8,
                "https://xabar.uz/static/uploads/35/720__vrtQE9mNnG8LoXcRJSzsaSCfZCfdmNkK.jpg"
            )
        )
        data8.add(
            Model(
                8,
                "https://lh3.googleusercontent.com/proxy/C1SG8WjPs2Sk_HP2OPHqWxsRz-hxxAWDBbKWq8GZ9T0eag-e0d8EuPNQxvpKJkWa4MG72bMXE0Ka_cxCuRg3aE1WhMW2Yn_ivwtb6UYuTGIjzgK5"
            )
        )

        data9.add(
            Model(
                9,
                "https://avatars.mds.yandex.net/get-altay/2714499/2a0000017051b170d4c1a4c39d1b8ca60133/XXXL"
            )
        )
        data9.add(Model(9, "https://buxdu.uz/images/ttj/4.jpg"))
        data9.add(
            Model(
                9,
                "https://mybooking.uz/uploads/hotel/images/755_1555052549q41E_1024x768.jpg"
            )
        )
        data9.add(Model(9, "https://www.sbtsue.uz/frontend/web/news/5fae73456bac0.jpg"))
        data9.add(Model(9, "http://www.samdu.uz/public/images/image012%281%29.jpg"))
        data9.add(Model(9, "https://samarkand-vtc.uz/wp-content/uploads/2018/11/55.jpg"))
        data9.add(Model(9, "http://www.samdu.uz/public/images/7%286%29.jpg"))

        data10.add(
            Model(
                10,
                "http://konservatoriya.uz/wp-content/uploads/2019/07/1555557602_photo_2019-04-13_11-45-16.jpg"
            )
        )
        data10.add(Model(10, "http://www.samdu.uz/public/images/7%286%29.jpg"))
        data10.add(Model(10, "http://www.samdu.uz/public/images/5%2833%29.jpg"))
        data10.add(Model(10, "http://www.samdu.uz/public/images/x1%281%29.jpg"))
        data10.add(
            Model(
                10,
                "https://mybooking.uz/uploads/hotel/images/592_1574416002AZ0X_1024x768.jpg"
            )
        )
        data10.add(
            Model(
                10,
                "https://i1.wp.com/hudud24.uz/wp-content/uploads/2020/07/obshejitie.jpg?fit=665%2C442"
            )
        )

        data11.add(
            Model(
                11,
                "https://avatars.mds.yandex.net/get-altay/2701879/2a00000170482b4873ad576063b63c643913/L"
            )
        )
        data11.add(
            Model(
                11,
                "https://xabar.uz/static/uploads/35/720__fwi9Gbz42QFnaT707zRAIrHAZ6CftuHe.jpg"
            )
        )
        data11.add(
            Model(
                11,
                "https://lh3.googleusercontent.com/proxy/UzaUaf466FafV_2Ncj0qhBjshrqqyJBUeVV0cwlRsiPsW-dVXnWQssrjbURL-m2dsIi6C9N_ccGSXwBgF7M81IIKpCsFHN3hYfLLAYp2KdcuFnEQ2eg3YDSXwtXx"
            )
        )
        data11.add(Model(11, "https://adti.uz/wp-content/uploads/2018/11/44-1.jpg"))
        data11.add(Model(11, "https://adti.uz/wp-content/uploads/2018/11/105.jpg"))
        data11.add(Model(11, "https://adti.uz/wp-content/uploads/2018/11/102.jpg"))
        data11.add(
            Model(
                11,
                "https://lh3.googleusercontent.com/proxy/ECReCzaXpGV5kMndh9766d8MhHfvAlSFWCxCKPGUgW4C-yUqeyiL5OyXwdt34Q-8HmhBoXQhZfWCUDNIMLbI2-qVB43KxTAwqDesLQ4aVaJbJ8xb"
            )
        )
        data11.add(Model(11, "https://buxdu.uz/images/ttj/5.jpg"))

        data12.add(Model(12, "http://www.samdu.uz/public/images/image001%2819%29.jpg"))
        data12.add(
            Model(
                12,
                "http://mrdi.uz/wp-content/uploads/2021/01/talabalar-yotoqxonasi-3-768x1024.jpg"
            )
        )
        data12.add(
            Model(
                12,
                "https://adti.uz/wp-content/uploads/2021/02/photo_2021-02-05_17-09-07.jpg"
            )
        )
        data12.add(
            Model(
                12,
                "https://www.norma.uz/img/mw500/90/73/fae3cddc35905542116b3d698e7f.jpg"
            )
        )
        data12.add(
            Model(
                12,
                "https://lh3.googleusercontent.com/proxy/cqkAurvqYVBkizK3PfiXGXQ0UEboxfThTL0zChYTaHMthz_OIlBM8oJECp6wxu2ZVpEe5TUYCukJ3VdaoqTST92g5RvYgsm_lrSrjzEwaPckzGpN"
            )
        )
        data12.add(Model(12, "http://samvmi.uz/frontend/web/news/5dfc643095132.png"))
        data12.add(Model(12, "http://tf.tdpu.uz/upload/5987_18265.jpg"))

        data13.add(Model(13, "https://adu.uz/rasmlar/news/2020/photo_2021-05-10_17-00-571.jpg"))
        data13.add(
            Model(
                13,
                "https://xs.uz/upload/post/2021/01/13/33a82d86bce8003b0f24d1a14eb2c3370113.jpg"
            )
        )
        data13.add(Model(13, "https://samgasi.uz/files/images/001/002/2.JPG"))
        data13.add(
            Model(
                13,
                "https://mybooking.uz/uploads/hotel/images/755_1555052549q41E_1024x768.jpg"
            )
        )
        data13.add(
            Model(
                13,
                "https://storage.kun.uz/source/4/Ay60n63VdQ5QneoZa-9oCUF3WDouXbvw.jpg"
            )
        )
    }

}