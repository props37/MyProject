package ru.livetyping.zarina.core.permission

public sealed interface PermissionState {
    public val isGranted: Boolean
        get() = this == Granted

    public val isDenied: Boolean
        get() = this is Denied

    public data object Granted : PermissionState

    public data class Denied(val shouldShowRequestRationale: Boolean) : PermissionState
}

public val PermissionState.shouldShowRequestRationale: Boolean
    get() = if (this is PermissionState.Denied) shouldShowRequestRationale else false

public inline fun PermissionState.onGranted(action: () -> Unit): PermissionState {
    if (this.isGranted) action()
    return this
}

public inline fun PermissionState.onDenied(
    action: (shouldShowRequestRationale: Boolean) -> Unit,
): PermissionState {
    if (this is PermissionState.Denied) action(shouldShowRequestRationale)
    return this
}

public inline fun PermissionState.handle(
    onGranted: () -> Unit,
    onDenied: (shouldShowRequestRationale: Boolean) -> Unit,
) {
    if (isGranted) onGranted()
    if (this is PermissionState.Denied) onDenied(shouldShowRequestRationale)
}
