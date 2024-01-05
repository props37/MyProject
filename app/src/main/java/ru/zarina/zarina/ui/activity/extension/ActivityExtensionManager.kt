package ru.zarina.zarina.ui.activity.extension

import javax.inject.Inject

class ActivityExtensionManager @Inject constructor(
    permissionManager: PermissionManagerActivityExtension,
) {
    val extensions = listOf(permissionManager)
}
