package ru.livetyping.zarina.core.permission

import androidx.activity.ComponentActivity
import kotlinx.coroutines.flow.Flow

public interface PermissionManager {
    public fun isPermissionGranted(permission: String): Boolean

    public fun areMultiplePermissionsGranted(permissions: List<String>): Map<String, Boolean>

    public suspend fun requestPermission(permission: String): PermissionState

    public suspend fun requestMultiplePermissions(permissions: List<String>): Map<String, PermissionState>

    public suspend fun getPermissionState(permission: String): PermissionState

    public suspend fun getMultiplePermissionsState(permissions: List<String>): Map<String, PermissionState>

    /**
     * Check if given permission has required request rationale in past.
     */
    public fun hasPermissionRequiredRequestRationale(permission: String): Flow<Boolean?>

    /**
     * Check if given permissions have required request rationale in past.
     */
    public fun haveMultiplePermissionsRequiredRequestRationale(
        permissions: List<String>,
    ): Flow<Map<String, Boolean?>>

    /**
     * Set Activity that will be used to request permissions under the hood.
     *
     * @see [unsetActivity]
     */
    public fun setActivity(activity: ComponentActivity)

    /**
     * Unset Activity. This method does nothing if the [activity] is not set at the moment.
     *
     * @see [setActivity]
     */
    public fun unsetActivity(activity: ComponentActivity)

    /**
     * Release resources.
     *
     * Consider using [unsetActivity] if it is needed to unset an Activity before setting a new one.
     *
     * @see [unsetActivity]
     */
    public fun release()
}
