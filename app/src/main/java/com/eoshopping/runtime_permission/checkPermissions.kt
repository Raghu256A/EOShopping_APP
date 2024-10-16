package com.eoshopping.runtime_permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class checkPermissions {
    private  val REQUEST_CODE_PERMISSIONS = 1001

    private fun checkCameraPermissions(context: Context) {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (!permissions.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(
                context as Activity,
                permissions,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }


}