package ru.livetyping.zarina.core.uicommon.message

import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.uicommon.message.MessageQueue.Message
import kotlin.time.Duration

public interface MessageQueue<T : Message> {
    public val currentMessage: StateFlow<T?>

    public fun addMessage(message: T)
    public fun removeCurrentMessage()

    public interface Message {
        public val duration: Duration
        public val isRemovable: Boolean
    }
}
