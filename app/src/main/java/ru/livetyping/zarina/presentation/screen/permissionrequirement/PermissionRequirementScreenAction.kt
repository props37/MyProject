package ru.livetyping.zarina.presentation.screen.permissionrequirement

sealed class PermissionRequirementScreenAction {
    data object ScreenClosed : PermissionRequirementScreenAction()
}
