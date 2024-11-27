package ru.livetyping.zarina.core.uicommon.toast

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.uicommon.message.MessageQueueImpl

public class ZarinaToastControllerImpl(
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
