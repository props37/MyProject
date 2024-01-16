package ru.zarina.zarina.data.rework.permissionmanager

/**
 * Represents the state of a permission.
 */
sealed interface PermissionState {
    /**
     * Represents the state of the granted permission.
     */
    data object Granted : PermissionState

    /**
     * Represents the state of the denied permission. Use [shouldShowRequestRationale] to check
     * it is needed to show permission request rationale to the user.
     */
    data class Denied(val shouldShowRequestRationale: Boolean) : PermissionState
}

/**
 * Returns `true` if this instance represents the [PermissionState.Granted] state.
 */
val PermissionState.isGranted: Boolean
    get() = this == PermissionState.Granted

/**
 * Returns `true` if this instance represents the [PermissionState.Denied] state.
 */
val PermissionState.isDenied: Boolean
    get() = this is PermissionState.Denied

/**
 * Returns `true` if this instance represents the [PermissionState.Denied] state
 * and the app has to show permission request rationale or `false` otherwise.
 */
val PermissionState.shouldShowRequestRationale: Boolean
    get() = if (this is PermissionState.Denied) shouldShowRequestRationale else false

/**
 * Performs the given [action] if this instance represents the [PermissionState.Granted] state.
 * Returns the original [PermissionState] unchanged.
 */
inline fun PermissionState.onGranted(action: () -> Unit): PermissionState {
    if (isGranted) action()
    return this
}

/**
 * Performs the given [action] if this instance represents the [PermissionState.Denied] state.
 * Returns the original [PermissionState] unchanged.
 */
inline fun PermissionState.onDenied(
    action: (shouldShowRequestRationale: Boolean) -> Unit,
): PermissionState {
    if (this is PermissionState.Denied) action(shouldShowRequestRationale)
    return this
}

/**
 * Performs the given [onGranted] block if this instance represents the [PermissionState.Granted]
 * state or [onDenied] block if the instance represents [PermissionState.Denied] state.
 */
inline fun PermissionState.handle(
    onGranted: () -> Unit,
    onDenied: (shouldShowRequestRationale: Boolean) -> Unit,
) {
    if (isGranted) onGranted()
    if (this is PermissionState.Denied) onDenied(shouldShowRequestRationale)
}
