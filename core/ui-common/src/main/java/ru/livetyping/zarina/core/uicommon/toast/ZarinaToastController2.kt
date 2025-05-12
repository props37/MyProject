package ru.livetyping.zarina.core.uicommon.toast

import kotlinx.coroutines.flow.StateFlow

// TODO: [Top] Rename after full migration
public interface ZarinaToastController2 {
    public val currentMessage: StateFlow<ZarinaToastMessage2?>

    public fun show(message: ZarinaToastMessage2, removePreviousMessage: Boolean = true)
    public fun cancelCurrentToast()
}
