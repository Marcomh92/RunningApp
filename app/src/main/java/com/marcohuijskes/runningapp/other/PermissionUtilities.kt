package com.marcohuijskes.runningapp.other

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fondesa.kpermissions.request.PermissionRequest
import com.fondesa.kpermissions.PermissionStatus
import com.marcohuijskes.runningapp.R

/*object PermissionUtilities {

    fun requestPermissions(context: Context, vararg permissions: String) {
        val permissionsToRequest = permissions.toMutableList()
        if(permissionsToRequest.isNotEmpty()) {
            permissionsBuilder(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).build().send()





            *//*requestPermissions(context, permissionsToRequest.toTypedArray(), 0)
            ActivityCompat.requestPermissions(context, permissionsToRequest.toTypedArray(), 0)*//*
        }
    }
}*/

internal fun Context.showGrantedToast(permissions: List<PermissionStatus>) {
    val msg = getString(R.string.granted_permissions, permissions.toMessage<PermissionStatus.Granted>())
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

internal fun Context.showRationaleDialog(permissions: List<PermissionStatus>, request: PermissionRequest) {
    val msg = getString(R.string.rationale_permissions, permissions.toMessage<PermissionStatus.Denied.ShouldShowRationale>())

    AlertDialog.Builder(this)
        .setTitle(R.string.permissions_required)
        .setMessage(msg)
        .setPositiveButton(R.string.request_again) { _, _ ->
            // Send the request again.
            request.send()
        }
        .setNegativeButton(android.R.string.cancel, null)
        .show()
}

internal fun Context.showPermanentlyDeniedDialog(permissions: List<PermissionStatus>) {
    val msg = getString(R.string.permanently_denied_permissions, permissions.toMessage<PermissionStatus.Denied.Permanently>())

    AlertDialog.Builder(this)
        .setTitle(R.string.permissions_required)
        .setMessage(msg)
        .setPositiveButton(R.string.action_settings) { _, _ ->
            // Open the app's settings.
            val intent = createAppSettingsIntent()
            startActivity(intent)
        }
        .setNegativeButton(android.R.string.cancel, null)
        .show()
}

private fun Context.createAppSettingsIntent() = Intent().apply {
    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    data = Uri.fromParts("package", packageName, null)
}

private inline fun <reified T : PermissionStatus> List<PermissionStatus>.toMessage(): String = filterIsInstance<T>()
    .joinToString { it.permission }