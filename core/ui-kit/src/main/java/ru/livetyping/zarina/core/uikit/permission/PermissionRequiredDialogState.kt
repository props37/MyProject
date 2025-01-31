package ru.livetyping.zarina.core.uikit.permission

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.text.Text

@Stable
public sealed class PermissionRequiredDialogState {
    @Immutable
    public data class Visible(
        val permission: RequiredPermission,
        val title: Text,
        val body: Text,
    ) : PermissionRequiredDialogState()

    public data object None : PermissionRequiredDialogState()
}
