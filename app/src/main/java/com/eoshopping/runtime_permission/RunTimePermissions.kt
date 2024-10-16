package com.eoshopping.runtime_permission
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class RunTimePermissions {


    companion object {
        const val PERMISSION_REQUEST_CODE = 200
        const val CAMERAREQUEST_CODE = 200
        const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 111
        const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE2 = 222
        const val MY_PERMISSIONS_REQUEST_FINE_LOCATION = 333
        const val READ_PHONE_STATE1 = 444

        // Utility method for checking and requesting permissions
        private fun requestPermission(context: Context, permission: String, requestCode: Int): Boolean {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                    AlertDialog.Builder(context)
                        .setCancelable(true)
                        .setTitle("Permission necessary")
                        .setMessage("This permission is necessary for the app to function properly.")
                        .setPositiveButton(android.R.string.yes) { _, _ ->
                            ActivityCompat.requestPermissions(context, arrayOf(permission), requestCode)
                        }
                        .create()
                        .show()
                } else {
                    ActivityCompat.requestPermissions(context, arrayOf(permission), requestCode)
                }
                return false // Permission not granted
            }
            return true // Permission already granted
        }

        fun checkPermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return requestPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) &&
                        requestPermission(context, Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
            }
            return true // Permissions are granted on lower APIs
        }

        fun checkPermissionFiles(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return requestPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE2) &&
                        requestPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE2) &&
                        requestPermission(context, Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE2)
            }
            return true // Permissions are granted on lower APIs
        }

        fun checkLocationPermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return requestPermission(context, Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_FINE_LOCATION)
            }
            return true // Permissions are granted on lower APIs
        }

        fun checkReadPhoneState(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return requestPermission(context, Manifest.permission.READ_PHONE_STATE, READ_PHONE_STATE1)
            }
            return true // Permissions are granted on lower APIs
        }

        fun checkAllPermissions(context: Context): Boolean {
            val permissionsNeeded = mutableListOf<String>()
            val allPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.RECORD_AUDIO
            )

            for (perm in allPermissions) {
                if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNeeded.add(perm)
                }
            }

            if (permissionsNeeded.isNotEmpty()) {
                ActivityCompat.requestPermissions(context as Activity, permissionsNeeded.toTypedArray(), PERMISSION_REQUEST_CODE)
                return false // Permissions not granted
            }
            return true // All permissions granted
        }
    }
}