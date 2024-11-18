package com.demo.recipelist.ui.screen

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel

data class CameraState(
    val hasCameraDevices: Boolean,
    val hasCameraAccess: Boolean,
)
class PermissionViewModel(application: Application): AndroidViewModel(application) {
    private val context: Context
        get() = getApplication()

    var cameraState by mutableStateOf(
        CameraState(
            hasCameraDevices = hasCameraCheck(PackageManager.FEATURE_CAMERA_ANY),
            hasCameraAccess = hasPermission(Manifest.permission.CAMERA),
        )
    )
        private set

    // 確認 user 是否已授予應用程式相機的權限
    private fun hasPermission(permission: String): Boolean {
        // ContextCompat.checkSelfPermission 會傳回 PERMISSION_GRANTED 或 PERMISSION_DENIED
        // PERMISSION_GRANTED = 0, PERMISSION_DENIED = -1
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 確認裝置有沒有相機
    private fun hasCameraCheck(feature: String): Boolean {
        // Check whether your app is running on a device that has a front-facing camera.
        // If Yes: Continue with the part of your app's workflow that requires a front-facing camera.
        // If No: Gracefully degrade your app experience.
        return context.packageManager.hasSystemFeature(feature)
    }

    fun onPermissionChange(permission: String, isGranted: Boolean) {
        if (permission == Manifest.permission.CAMERA) {
            cameraState = cameraState.copy(hasCameraAccess = isGranted)
        }
        else {
            Log.e("Permission change", "Unexpected permission: $permission")
        }
    }
}