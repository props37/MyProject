package ru.livetyping.zarina.presentation.common.zarinatoast.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.messagequeue.MessageQueueImpl
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage

class ZarinaToastControllerImpl(
    coroutineScope: CoroutineScope,
) : ZarinaToastController {
    private val messageQueue = MessageQueueImpl<ZarinaToastMessage>(coroutineScope)

    override val currentMessage: StateFlow<ZarinaToastMessage?> = messageQueue.currentMessage

    override fun show(message: ZarinaToastMessage, removePreviousMessage: Boolean) {
        if (removePreviousMessage) {
            messageQueue.removeCurrentMessage()
        }
        messageQueue.addMessage(message)
    }

    override fun hideCurrentToast() {
        messageQueue.removeCurrentMessage()
    }
}
