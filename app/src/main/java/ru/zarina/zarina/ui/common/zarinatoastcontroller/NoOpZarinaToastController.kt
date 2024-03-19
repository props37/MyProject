package ru.zarina.zarina.ui.common.zarinatoastcontroller

import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.ui.base.message.ZarinaMessage

class NoOpZarinaToastController : ZarinaToastController {
    override val currentMessage: StateFlow<ZarinaMessage?>
        get() = throw NotImplementedError()

    override fun show(message: ZarinaMessage) {
        throw NotImplementedError()
    }

    override fun hideCurrentToast() {
        throw NotImplementedError()
    }
}
