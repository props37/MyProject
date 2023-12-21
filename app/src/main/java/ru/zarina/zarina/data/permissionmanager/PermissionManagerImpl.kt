package ru.zarina.zarina.data.permissionmanager

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume

class PermissionManagerImpl : PermissionManager {
    private var activity: ComponentActivity? = null

    override suspend fun requestPermission(permission: String): PermissionState {
        val activity = checkNotNull(activity) { ACTIVITY_NULL_ERROR_MESSAGE }
        return suspendCancellableCoroutine { continuation ->
            var launcher: ActivityResultLauncher<String>? = null

            continuation.invokeOnCancellation {
                launcher?.unregister()
            }

            launcher = activity.activityResultRegistry.register(
                UUID.randomUUID().toString(),
                ActivityResultContracts.RequestPermission(),
            ) {
                launcher?.unregister()

                val permissionState = getPermissionState(permission)
                continuation.resume(permissionState)
            }
            launcher.launch(permission)
        }
    }

    override suspend fun requestMultiplePermissions(
        permissions: List<String>,
    ): Map<String, PermissionState> {
        val activity = checkNotNull(activity) { ACTIVITY_NULL_ERROR_MESSAGE }
        return suspendCancellableCoroutine { continuation ->
            var launcher: ActivityResultLauncher<Array<String>>? = null

            continuation.invokeOnCancellation {
                launcher?.unregister()
            }

            launcher = activity.activityResultRegistry.register(
                UUID.randomUUID().toString(),
                ActivityResultContracts.RequestMultiplePermissions(),
            ) {
                launcher?.unregister()

                val permissionsState = getMultiplePermissionsState(permissions)
                continuation.resume(permissionsState)
            }
            launcher.launch(permissions.toTypedArray())
        }
    }

    override fun getPermissionState(permission: String): PermissionState {
        return if (isPermissionGranted(permission)) {
            PermissionState.Granted
        } else {
            val shouldShowRequestRationale = shouldShowRequestPermissionRationale(permission)
            PermissionState.Denied(shouldShowRequestRationale)
        }
    }

    override fun getMultiplePermissionsState(permissions: List<String>): Map<String, PermissionState> {
        val map = mutableMapOf<String, PermissionState>()
        for (permission in permissions) {
            map[permission] = getPermissionState(permission)
        }
        return map
    }

    @Synchronized
    override fun setActivity(activity: ComponentActivity) {
        this.activity = activity
    }

    @Synchronized
    override fun unsetActivity(activity: ComponentActivity) {
        if (this.activity == activity) {
            this.activity = null
        }
    }

    @Synchronized
    override fun release() {
        activity = null
    }

    private fun isPermissionGranted(permission: String): Boolean {
        val activity = checkNotNull(activity) { ACTIVITY_NULL_ERROR_MESSAGE }
        return ContextCompat.checkSelfPermission(
            activity,
            permission,
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        val activity = checkNotNull(activity) { ACTIVITY_NULL_ERROR_MESSAGE }
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    companion object {
        private const val ACTIVITY_NULL_ERROR_MESSAGE =
            "Activity can not be null. Did you forget to call setActivity?"
    }
}
