package com.marcohuijskes.runningapp.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import com.marcohuijskes.runningapp.R
import com.marcohuijskes.runningapp.other.Constants.ACTION_PAUSE_SERVICE
import com.marcohuijskes.runningapp.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.marcohuijskes.runningapp.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.marcohuijskes.runningapp.other.Constants.ACTION_STOP_SERVICE
import com.marcohuijskes.runningapp.other.Constants.FASTEST_LOCATION_INTERVAL
import com.marcohuijskes.runningapp.other.Constants.LOCATION_UPDATE_INTERVAL
import com.marcohuijskes.runningapp.other.Constants.NOTIFICATION_CHANNEL_ID
import com.marcohuijskes.runningapp.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.marcohuijskes.runningapp.other.Constants.NOTIFICATION_ID
import com.marcohuijskes.runningapp.other.TrackingUtility
import com.marcohuijskes.runningapp.ui.MainActivity
import timber.log.Timber

typealias PolyLine = MutableList<LatLng> // MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine> // MutableList<MutableList<LatLng>>

class TrackingService : LifecycleService() {

    var isFirstRun = true

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<PolyLines>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
        // Passing "this" as observe owner is only possible because the service inherits from LifecycleService.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                        Timber.d("Starting service")
                    } else {
                        Timber.d("Resuming service")
                        startForegroundService()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun pauseService() {
        isTracking.postValue(false)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if(isTracking) {
            if(/*TrackingUtility.hasLocationPermissions(this)*/true) {
                val request = LocationRequest.Builder(
                    PRIORITY_HIGH_ACCURACY,
                    LOCATION_UPDATE_INTERVAL
                ).setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(FASTEST_LOCATION_INTERVAL).build()

                fusedLocationProviderClient.requestLocationUpdates(
                    request, locationCallback, Looper.getMainLooper()
                    )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }
    
    val locationCallback = object : LocationCallback() {
        // Used to actually get the location updates. Using a "fused location provider client". That is something we can use to request location updates. Will basically deliver us on consistent basis with new location updates whenever the location changes or we can set an interval for that.
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isTracking.value!!) {
                result?.locations?.let { locations ->
                    for(location in locations) {
                        addPathPoint(location)
                        Timber.d("NEW LOCATION: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private fun addEmptyPolyLine() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this) // Because we added a new PolyLine, there is a change, and we want to post the new value so the Fragment will be notified about the change later on.
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
    )

    private fun startForegroundService() {
        addEmptyPolyLine()
        isTracking.postValue(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false) // Prevents disappearing when clicked.
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Running App")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}