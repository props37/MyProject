package ru.livetyping.zarina.core.uikit.toast

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.uicommon.message.MessageQueueImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastController2
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage2

// TODO: [Top] Rename after full migration
public class ZarinaToastController2Impl(
    coroutineScope: CoroutineScope,
) : ZarinaToastController2 {
    private val messageQueue = MessageQueueImpl<ZarinaToastMessage2>(coroutineScope)

    override val currentMessage: StateFlow<ZarinaToastMessage2?> = messageQueue.currentMessage

    override fun show(message: ZarinaToastMessage2, removePreviousMessage: Boolean) {
        if (removePreviousMessage) {
            messageQueue.removeCurrentMessage()
        }
        messageQueue.addMessage(message)
    }

    override fun cancelCurrentToast() {
        messageQueue.removeCurrentMessage()
    }
}
