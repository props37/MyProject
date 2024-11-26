package ru.livetyping.zarina.core.permission.impl

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.livetyping.zarina.core.permission.PermissionManager
import ru.livetyping.zarina.core.permission.PermissionState
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import kotlin.coroutines.resume

internal class PermissionManagerImpl @Inject constructor(
    private val storage: PermissionManagerStorage,
) : PermissionManager {
    private var activityRef = AtomicReference<WeakReference<ComponentActivity>?>(null)

    override fun isPermissionGranted(permission: String): Boolean {
        val activity = requireActivity()
        return ContextCompat.checkSelfPermission(
            activity,
            permission,
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun areMultiplePermissionsGranted(permissions: List<String>): Map<String, Boolean> {
        return buildMap {
            for (permission in permissions) {
                put(permission, isPermissionGranted(permission))
            }
        }
    }

    override suspend fun requestPermission(permission: String): PermissionState {
        val activity = requireActivity()
        var launcher: ActivityResultLauncher<String>? = null
        suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                launcher?.unregister()
            }

            launcher = activity.activityResultRegistry.register(
                key = UUID.randomUUID().toString(),
                contract = ActivityResultContracts.RequestPermission(),
            ) {
                continuation.resume(Unit)
            }
            launcher?.launch(permission)
        }
        launcher?.unregister()

        if (shouldShowRequestPermissionRationale(permission)) {
            storage.savePermissionRequiredRequestRationale(permission)
        }

        return getPermissionState(permission)
    }

    override suspend fun requestMultiplePermissions(
        permissions: List<String>,
    ): Map<String, PermissionState> {
        val activity = requireActivity()
        var launcher: ActivityResultLauncher<Array<String>>? = null
        suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                launcher?.unregister()
            }

            launcher = activity.activityResultRegistry.register(
                key = UUID.randomUUID().toString(),
                contract = ActivityResultContracts.RequestMultiplePermissions(),
            ) {
                continuation.resume(Unit)
            }
            launcher?.launch(permissions.toTypedArray())
        }
        launcher?.unregister()

        for (permission in permissions) {
            if (shouldShowRequestPermissionRationale(permission)) {
                storage.savePermissionRequiredRequestRationale(permission)
            }
        }

        return getMultiplePermissionsState(permissions)
    }

    override suspend fun getPermissionState(permission: String): PermissionState {
        return if (isPermissionGranted(permission)) {
            PermissionState.Granted
        } else {
            val shouldShowRequestRationale = shouldShowRequestPermissionRationale(permission)
            if (shouldShowRequestRationale) {
                storage.savePermissionRequiredRequestRationale(permission)
            }
            PermissionState.Denied(shouldShowRequestRationale)
        }
    }

    override suspend fun getMultiplePermissionsState(
        permissions: List<String>,
    ): Map<String, PermissionState> {
        return buildMap {
            for (permission in permissions) {
                val permissionState = getPermissionState(permission)
                put(permission, permissionState)
            }
        }
    }

    override fun hasPermissionRequiredRequestRationale(permission: String): Flow<Boolean?> {
        return storage.hasPermissionRequiredRequestRationale(permission)
    }

    override fun haveMultiplePermissionsRequiredRequestRationale(
        permissions: List<String>,
    ): Flow<Map<String, Boolean?>> {
        return storage.haveMultiplePermissionsRequiredRequestRationale(permissions)
    }

    override fun setActivity(activity: ComponentActivity) {
        activityRef.set(WeakReference(activity))
        Timber.tag(TAG).v("Activity set")
    }

    override fun unsetActivity(activity: ComponentActivity) {
        val currentActivityRef = activityRef.get()
        val unset = activityRef.compareAndSet(
            /* expectedValue = */ currentActivityRef,
            /* newValue = */ null,
        )
        Timber.tag(TAG).v("Activity unset: $unset")
    }

    override fun release() {
        activityRef.set(null)
        Timber.tag(TAG).v("Released")
    }

    private fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        val activity = requireActivity()
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    private fun requireActivity(): ComponentActivity {
        val activity = activityRef.get()?.get()
        checkNotNull(activity) { "Activity can not be null. Did you forget to call setActivity?" }
        return activity
    }

    private companion object {
        private const val TAG = "PermissionManagerImpl"
    }
}
