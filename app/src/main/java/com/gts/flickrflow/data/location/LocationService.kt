package com.gts.flickrflow.data.location

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.gts.flickrflow.R
import com.gts.flickrflow.presentation.MainActivity
import timber.log.Timber

class LocationService : Service() {

    private val TAG = LocationService::class.java.simpleName
    // The name of the channel for notifications.
    private val CHANNEL_ID = "channel_01"
    // The identifier for the notification displayed for the foreground service.
    private val NOTIFICATION_ID = 1101
    private val EXTRA_LOCATION = "location"
    private val EXTRA_STARTED_FROM_NOTIFICATION = "started_from_notification"
    private val ACTION_BROADCAST = "broadcast"


    private val mBinder = LocalBinder()
    // Contains parameters used by [com.google.android.gms.location.FusedLocationProviderApi].
    private lateinit var mLocationRequest: LocationRequest
    // Provides access to the Fused Location Provider API.
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    // Callback for changes in location.
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mServiceHandler: Handler
    // The current location.
    private lateinit var mLocation: Location
    private lateinit var mNotificationManager: NotificationManager

    fun LocationService() {}

    override fun onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult!!.lastLocation)
            }
        }

        createLocationRequest()
        getLastLocation()

        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            // Create the channel for the notification
            val mChannel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Timber.tag(TAG).i("Service started")
        val startedFromNotification = intent.getBooleanExtra(
            EXTRA_STARTED_FROM_NOTIFICATION,
            false
        )

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates()
            stopSelf()
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return Service.START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
//        mChangingConfiguration = true
    }

    override fun onBind(intent: Intent): IBinder? {
        // Called when a client (MainActivity in case of this sample) comes to the foreground and binds with this service.
        // The service should cease to be a foreground service when that happens.
        Timber.tag(TAG).i("in onBind()")
        stopForeground(true)
//        mChangingConfiguration = false
        return mBinder
    }

    override fun onRebind(intent: Intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground and
        // binds once again with this service. The service should cease to be a foreground service when that happens.
        Timber.tag(TAG).i("in onRebind()")
        stopForeground(true)
//        mChangingConfiguration = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Timber.tag(TAG).i("Last client unbound from service")

        // Called when the last client (MainActivity in case of this sample) unbinds from this service.
        // If this method is called due to a configuration change in MainActivity, we do nothing.
        // Otherwise, we make this service a foreground service.
//        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
        Timber.tag(TAG).i("Starting foreground service")

        startForeground(NOTIFICATION_ID, getNotification())
//        }
        return true // Ensures onRebind() is called when a client re-binds.
    }

    override fun onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null)
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the [SecurityException].
     */
    fun requestLocationUpdates() {
        Timber.tag(TAG).i("Requesting location updates")
//        Utils.setRequestingLocationUpdates(this, true)
        startService(Intent(applicationContext, LocationService::class.java))
        try {
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
//            Utils.setRequestingLocationUpdates(this, false)
            Timber.tag(TAG).e("Lost location permission. Could not request updates. $unlikely")
        }

    }

    /**
     * Removes location updates. Note that in this sample we merely log the [SecurityException].
     */
    fun removeLocationUpdates() {
        Timber.tag(TAG).i("Removing location updates")
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback)
//            Utils.setRequestingLocationUpdates(this, false)
            stopSelf()
        } catch (unlikely: SecurityException) {
//            Utils.setRequestingLocationUpdates(this, true)
            Timber.tag(TAG).e("Lost location permission. Could not remove updates. $unlikely")
        }
    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    mLocation = task.result!!
                } else {
                    Timber.tag(TAG).w("Failed to get location.")
                }
            }
        } catch (unlikely: SecurityException) {
            Timber.tag(TAG).e("Lost location permission.$unlikely")
        }
    }

    private fun onNewLocation(location: Location) {
        Timber.tag(TAG).i("New location: $location")
        mLocation = location

        // Notify anyone listening for broadcasts about the new location.
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification())
        }
    }

    /**
     * Sets the location request parameters.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.smallestDisplacement = 100F
//        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
//        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its clients,
     * we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: LocationService
            get() = this@LocationService
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The [Context].
     */
    private fun serviceIsRunningInForeground(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Returns the [NotificationCompat] used as part of the foreground service.
     */
    private fun getNotification(): Notification {
        val intent = Intent(this, LocationService::class.java)

//        val text = Utils.getLocationText(mLocation)

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        val servicePendingIntent = PendingIntent.getService(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // The PendingIntent to launch activity.
        val activityPendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java), 0
        )

        val builder = NotificationCompat.Builder(this)
            .addAction(0, "launch", activityPendingIntent)
//            .addAction(
//                R.drawable.ic_launch, getString(R.string.launch_activity),
//                activityPendingIntent
//            )
            .addAction(0, "stop", servicePendingIntent)
//            .addAction(
//                R.drawable.ic_cancel, getString(R.string.remove_location_updates),
//                servicePendingIntent
//            )
//            .setContentText(text)
//            .setContentTitle(Utils.getLocationTitle(this))
            .setOngoing(true)
            .setPriority(1) // Notification.PRIORITY_HIGH
            .setSmallIcon(R.mipmap.ic_launcher)
//            .setTicker(text)
            .setWhen(System.currentTimeMillis())

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID) // Channel ID
        }

        return builder.build()
    }
}
