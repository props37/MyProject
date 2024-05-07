package ru.livetyping.zarina.presentation.common.zarinasnack.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.messagequeue.MessageQueueImpl
import ru.livetyping.zarina.presentation.common.zarinasnack.ZarinaSnackMessage

class ZarinaSnackControllerImpl(
    coroutineScope: CoroutineScope,
) : ZarinaSnackController {
    private val messageQueue = MessageQueueImpl<ZarinaSnackMessage>(coroutineScope)

    override val currentMessage: StateFlow<ZarinaSnackMessage?> = messageQueue.currentMessage

    override fun show(message: ZarinaSnackMessage) {
        messageQueue.addMessage(message)
    }

    override fun hideCurrentSnack() {
        messageQueue.removeCurrentMessage()
    }
}
