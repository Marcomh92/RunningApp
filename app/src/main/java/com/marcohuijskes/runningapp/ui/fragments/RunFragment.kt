package com.marcohuijskes.runningapp.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fondesa.kpermissions.request.PermissionRequest
import com.marcohuijskes.runningapp.R
import com.marcohuijskes.runningapp.databinding.FragmentRunBinding
import com.marcohuijskes.runningapp.other.showGrantedToast
import com.marcohuijskes.runningapp.other.showPermanentlyDeniedDialog
import com.marcohuijskes.runningapp.other.showRationaleDialog
import com.marcohuijskes.runningapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), PermissionRequest.Listener/*, EasyPermissions.PermissionCallbacks*/ {

    private lateinit var binding: FragmentRunBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRunBinding.bind(view)
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
        requestPermissions.addListener(this)
        requestPermissions.send()
    }

    private val requestPermissions by lazy {
        permissionsBuilder(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).build()
    }

    override fun onPermissionsResult(result: List<PermissionStatus>) {
        when {
            result.anyPermanentlyDenied() -> requireContext().showPermanentlyDeniedDialog(result)
            result.anyShouldShowRationale() -> requireContext().showRationaleDialog(result, requestPermissions)
            result.allGranted() -> requireContext().showGrantedToast(result)
        }
    }

    /*private val request by lazy {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return@lazy permissionsBuilder(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).build()
        } else {
            return@lazy permissionsBuilder(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ).build()
        }
    }*/
    /*private fun requestPermissions() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissionsBuilder(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).build().send() { result ->

            }
        } else {
            permissionsBuilder(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }*/

    /*private fun requestPermissions() {
        if(TrackingUtility.hasLocationPermissions(requireContext())) return
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }


    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }*/
}