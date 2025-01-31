package ru.livetyping.zarina.core.uikit.permission

public sealed interface PermissionRequiredDialogEvent {
    public data class GoToSettingsClicked(
        val permission: RequiredPermission,
    ) : PermissionRequiredDialogEvent

    public data object CloseClicked : PermissionRequiredDialogEvent
}
