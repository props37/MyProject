package ru.livetyping.zarina.core.uicommon.toast

import kotlinx.coroutines.flow.StateFlow

public interface ZarinaToastController {
    public val currentMessage: StateFlow<ZarinaToastMessage?>

    public fun show(message: ZarinaToastMessage, removePreviousMessage: Boolean = true)
    public fun hideCurrentToast()
}
