package com.gts.flickrflow.presentation

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.app.ActivityCompat

import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.gts.flickrflow.BuildConfig

import org.koin.androidx.viewmodel.ext.android.viewModel

import com.gts.flickrflow.R
import com.gts.flickrflow.domain.model.Photo
import kotlinx.android.synthetic.main.fragment_photo_stream.*
import timber.log.Timber

class PhotoStreamFragment : Fragment() {

    private val viewModel: PhotoStreamViewModel by viewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter

    // Used in checking for runtime permissions.
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    // The BroadcastReceiver used to listen from broadcasts from the service.
    private lateinit var locationReceiver: LocationReceiver
    // A reference to the service used to get location updates.
    private var locationService: LocationService? = null
    // Tracks the bound state of the service.
    private var serviceBound = false
    // used to store button state
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        locationReceiver = LocationReceiver()
//        recyclerView = photo_recycler_view.apply {
//            adapter = photoAdapter
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_stream, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.retrievePhotosFromDb()
        viewModel.photos.observe(viewLifecycleOwner, Observer { photos ->
            //            my_image_view.setImageURI("https://farm${it.farm}.staticflickr.com/${it.server}/${it.id}_${it.secret}.jpg")
            Toast.makeText(context, "photos size: " + photos.size, Toast.LENGTH_SHORT).show()
        })
    }

    // Monitors the state of the connection to the service.
    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            locationService = binder.service
            serviceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            locationService = null
            serviceBound = false
        }
    }

    override fun onStart() {
        super.onStart()

        buttonStart.text = PreferenceManager.getDefaultSharedPreferences(context).getString(getString(R.string.service_state), "Start")
        buttonStart.setOnClickListener {
            if (buttonStart.text == getString(R.string.button_text_stop)) {
                locationService?.removeLocationUpdates()
                buttonStart.text = getString(R.string.button_text_start)
            } else {
                if (!checkPermissions()) {
                    requestPermissions()
                } else {
                    locationService?.requestLocationUpdates()
                }
                buttonStart.text = getString(R.string.button_text_stop)
            }
        }

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        requireActivity().bindService(
            Intent(context, LocationService::class.java), serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(context!!).registerReceiver(
            locationReceiver,
            IntentFilter(LocationService.ACTION_BROADCAST)
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(locationReceiver)
        super.onPause()
    }

    override fun onStop() {
        if (serviceBound) {
            // Unbind from the service. This signals to the service that this activity is no longer in the foreground,
            // and the service can respond by promoting itself to a foreground service.
            requireActivity().unbindService(serviceConnection)
            serviceBound = false
        }
        super.onStop()
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Timber.i("Displaying permission rationale to provide additional context.")
            Snackbar.make(
                fragment_main,
                getString(R.string.location_permission_text),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(getString(R.string.location_permission_action_ok_text)) {
                // Request permission
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_REQUEST_CODE
                )
            }.show()
        } else {
            Timber.i("Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy sets the permission
            // in a given state or the user denied the permission previously and checked "Never ask again".
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Timber.i("onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request is cancelled and you receive empty arrays.
                    Timber.i("=======> User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    Timber.i("=======> Permission was granted.")
                    // Permission was granted.
                    locationService?.requestLocationUpdates()
                }
                else -> // Permission denied.
                    Snackbar.make(fragment_main, getString(R.string.location_permission_denied_text), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.location_permission_action_settings_text)) {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }.show()
            }
        }
    }

    companion object {
        fun newInstance() = PhotoStreamFragment()
    }

    /**
     * Receiver for broadcasts sent by [LocationService].
     */
    private inner class LocationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val photo = intent.getParcelableExtra<Photo>(LocationService.EXTRA_PHOTO)
            if (photo != null) {
                Timber.i(" Photo broadcast received!")
                //viewModel.getPhotoBasedOnLocation(location.latitude.toString(), location.longitude.toString())
                val loctext = "(" + photo.id + ", " + photo.farm + ")"
                Toast.makeText(context, loctext, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
