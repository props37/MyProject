package ru.livetyping.zarina.core.uicomponent.permissionrequired

public sealed interface PermissionRequiredDialogEvent {
    public data class GoToSettingsClicked(
        val permission: RequiredPermission,
    ) : PermissionRequiredDialogEvent

    public data object CloseClicked : PermissionRequiredDialogEvent
}
