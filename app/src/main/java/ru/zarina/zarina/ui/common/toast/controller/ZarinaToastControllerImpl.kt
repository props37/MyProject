package ru.zarina.zarina.ui.common.toast.controller

import kotlinx.coroutines.CoroutineScope
import ru.zarina.zarina.base.messagequeue.MessageQueueImpl
import ru.zarina.zarina.ui.common.toast.ZarinaToastMessage

class ZarinaToastControllerImpl(
    coroutineScope: CoroutineScope,
) : ZarinaToastController {
    private val messageQueue = MessageQueueImpl<ZarinaToastMessage>(coroutineScope)

    override val currentMessage = messageQueue.currentMessage

    override fun show(message: ZarinaToastMessage) {
        messageQueue.addMessage(message)
    }

    override fun hideCurrentToast() {
        messageQueue.removeCurrentMessage()
    }
}
