package ru.livetyping.zarina.core.uikit.toast

import kotlinx.coroutines.flow.StateFlow

internal class NoOpZarinaToastController : ZarinaToastController {
    override val currentMessage: StateFlow<ZarinaToastMessage?>
        get() = throw NotImplementedError()

    override fun show(message: ZarinaToastMessage, removePreviousMessage: Boolean) {
        throw NotImplementedError()
    }

    override fun hideCurrentToast() {
        throw NotImplementedError()
    }
}
