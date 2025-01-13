package ru.livetyping.zarina.core.uicomponent.permissionrequired

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.text.Text

@Stable
public sealed class PermissionRequiredDialogState {

    @Immutable
    public data class PermissionRequired(
        val permission: RequiredPermission,
        val title: Text,
        val body: Text,
    ) : PermissionRequiredDialogState()

    public data object None : PermissionRequiredDialogState()
}
