package ru.zarina.zarina.ui.common.zarinatoast.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.base.messagequeue.MessageQueueImpl
import ru.zarina.zarina.ui.common.ZarinaMessage

class ZarinaToastControllerImpl(
    coroutineScope: CoroutineScope,
) : ZarinaToastController {
    private val messageQueue = MessageQueueImpl<ZarinaMessage>(coroutineScope)

    override val currentMessage: StateFlow<ZarinaMessage?> = messageQueue.currentMessage

    override fun show(message: ZarinaMessage) {
        messageQueue.addMessage(message)
    }

    override fun hideCurrentToast() {
        messageQueue.removeCurrentMessage()
    }
}
