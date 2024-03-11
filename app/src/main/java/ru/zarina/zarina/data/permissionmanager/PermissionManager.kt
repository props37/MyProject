package ru.zarina.zarina.data.permissionmanager

import androidx.activity.ComponentActivity
import kotlinx.coroutines.flow.Flow

/**
 * A wrapper around Android permissions that allows to work with permissions outside of the
 * UI layer. [PermissionState] is used to describe the state of a permission.
 *
 * PermissionManager also tracks and stores the `shouldShowRequestPermissionRationale` state
 * of each permission that was requested or checked using PermissionManager.
 * This allows to make *an assumption* whether the permission was permanently denied by the user.
 */
interface PermissionManager {
    /**
     * Check if the permission is granted.
     *
     * Consider using [getPermissionState] that also tracks the
     * `shouldShowRequestPermissionRationale` state of the permission.
     */
    fun isPermissionGranted(permission: String): Boolean

    /**
     * Check if the permissions are granted.
     *
     * Consider using [getMultiplePermissionsState] that also tracks the
     * `shouldShowRequestPermissionRationale` state of each permission.
     */
    fun areMultiplePermissionsGranted(permissions: List<String>): Map<String, Boolean>

    /**
     * Request permission.
     */
    suspend fun requestPermission(permission: String): PermissionState

    /**
     * Request multiple permissions.
     */
    suspend fun requestMultiplePermissions(permissions: List<String>): Map<String, PermissionState>

    /**
     * Get [PermissionState] of the given permission.
     */
    suspend fun getPermissionState(permission: String): PermissionState

    /**
     * Get [PermissionState]s of the given permissions.
     */
    suspend fun getMultiplePermissionsState(permissions: List<String>): Map<String, PermissionState>

    /**
     * Check if the permission has required request rationale in past.
     */
    fun hasPermissionRequiredRequestRationale(permission: String): Flow<Boolean?>

    /**
     * Check if the permissions have required request rationale in past.
     */
    fun haveMultiplePermissionsRequiredRequestRationale(
        permissions: List<String>,
    ): Flow<Map<String, Boolean?>>

    /**
     * Set Activity that will be used to request permissions under the hood.
     *
     * @see [unsetActivity]
     */
    fun setActivity(activity: ComponentActivity)

    /**
     * Unset Activity. This method does nothing if the [activity] is not set at the moment.
     *
     * @see [setActivity]
     */
    fun unsetActivity(activity: ComponentActivity)

    /**
     * Release resources.
     *
     * Consider using [unsetActivity] if it is needed to unset an Activity before setting a new one.
     *
     * @see [unsetActivity]
     */
    fun release()
}
