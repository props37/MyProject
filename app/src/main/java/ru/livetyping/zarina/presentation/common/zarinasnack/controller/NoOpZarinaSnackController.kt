package ru.livetyping.zarina.presentation.common.zarinasnack.controller

import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.presentation.common.zarinasnack.ZarinaSnackMessage

class NoOpZarinaSnackController : ZarinaSnackController {
    override val currentMessage: StateFlow<ZarinaSnackMessage?>
        get() = throw NotImplementedError()

    override fun show(message: ZarinaSnackMessage) {
        throw NotImplementedError()
    }

    override fun hideCurrentSnack() {
        throw NotImplementedError()
    }
}
