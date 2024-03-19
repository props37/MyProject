package ru.zarina.zarina.ui.common.toast.controller

import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.ui.common.toast.ZarinaToastMessage

class NoOpZarinaToastController : ZarinaToastController {
    override val currentMessage: StateFlow<ZarinaToastMessage?>
        get() = throw NotImplementedError()

    override fun show(message: ZarinaToastMessage) {
        throw NotImplementedError()
    }

    override fun hideCurrentToast() {
        throw NotImplementedError()
    }
}
