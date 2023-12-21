package ru.zarina.zarina.data.permissionmanager

import androidx.activity.ComponentActivity
import ru.zarina.zarina.data.permissionmanager.PermissionManager.Companion.create

/**
 * A wrapper around Android permissions that allows to work with permissions outside of the
 * UI layer. [PermissionState] is used to describe the state of a permission.
 *
 * Use [create] method to create a new instance.
 */
interface PermissionManager {
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
    fun getPermissionState(permission: String): PermissionState

    /**
     * Get [PermissionState]s of the given permissions.
     */
    fun getMultiplePermissionsState(permissions: List<String>): Map<String, PermissionState>

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

    companion object {
        /**
         * Create a new instance of [PermissionManager].
         */
        fun create(): PermissionManager {
            return PermissionManagerImpl()
        }
    }
}
