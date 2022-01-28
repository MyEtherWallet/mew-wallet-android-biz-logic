package com.myetherwallet.mewwalletbl.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Created by BArtWell on 23.07.2019.
 */

class PermissionsHelper {

    private var activity: AppCompatActivity? = null
    private var fragment: Fragment? = null
    private var permissions: Array<String>
    private var callback: ((Boolean) -> Unit)? = null

    private var launcher: ActivityResultLauncher<Array<String>>? = null
    private val activityResultCallback = ActivityResultCallback<MutableMap<String, Boolean>> { permissions ->
        callback?.invoke(!permissions.containsValue(false))
    }

    constructor(activity: AppCompatActivity, permissions: Array<String>, callback: (isGranted: Boolean) -> Unit) {
        this.activity = activity
        this.permissions = permissions
        this.callback = callback
        init()
    }

    constructor(fragment: Fragment, permissions: Array<String>, callback: (isGranted: Boolean) -> Unit) {
        this.fragment = fragment
        this.permissions = permissions
        this.callback = callback
        init()
    }

    constructor(activity: AppCompatActivity, permission: String, callback: (isGranted: Boolean) -> Unit) {
        this.activity = activity
        permissions = arrayOf(permission)
        this.callback = callback
        init()
    }

    constructor(fragment: Fragment, permission: String, callback: (isGranted: Boolean) -> Unit) {
        this.fragment = fragment
        permissions = arrayOf(permission)
        this.callback = callback
        init()
    }

    private fun init() {
        fragment?.let {
            launcher = it.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), activityResultCallback)
        } ?: activity?.let {
            launcher = it.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), activityResultCallback)
        }
    }

    fun requestPermissions() {
        if (fragment != null) {
            if (checkPermission(fragment!!.requireContext())) {
                callback?.invoke(true)
            } else {
                launcher?.launch(permissions)
            }
        } else {
            if (checkPermission(activity!!)) {
                callback?.invoke(true)
            } else {
                launcher?.launch(permissions)
            }
        }
    }

    fun checkPermission(context: Context): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun shouldShowRequestPermissionsRationale(fragment: Fragment): Boolean {
        for (permission in permissions) {
            if (shouldShowRequestPermissionRationale(fragment, permission)) {
                return true
            }
        }
        return false
    }

    private fun shouldShowRequestPermissionRationale(fragment: Fragment, permission: String): Boolean {
        return fragment.shouldShowRequestPermissionRationale(permission)
    }

    fun shouldShowRequestPermissionsRationale(activity: AppCompatActivity): Boolean {
        for (permission in permissions) {
            if (shouldShowRequestPermissionRationale(activity, permission)) {
                return true
            }
        }
        return false
    }

    private fun shouldShowRequestPermissionRationale(activity: AppCompatActivity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }
}
