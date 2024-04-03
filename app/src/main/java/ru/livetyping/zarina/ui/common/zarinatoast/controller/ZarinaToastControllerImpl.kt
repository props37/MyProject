package ru.livetyping.zarina.ui.common.zarinatoast.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.messagequeue.MessageQueueImpl
import ru.livetyping.zarina.ui.common.zarinatoast.ZarinaToastMessage

class ZarinaToastControllerImpl(
    coroutineScope: CoroutineScope,
) : ZarinaToastController {
    private val messageQueue = MessageQueueImpl<ZarinaToastMessage>(coroutineScope)

    override val currentMessage: StateFlow<ZarinaToastMessage?> = messageQueue.currentMessage

    override fun show(message: ZarinaToastMessage) {
        messageQueue.addMessage(message)
    }

    override fun hideCurrentToast() {
        messageQueue.removeCurrentMessage()
    }
}
