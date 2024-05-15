package ru.livetyping.zarina.presentation.common.zarinatoast.controller

import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage

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
