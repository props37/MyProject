package ru.zarina.zarina.base.messagequeue

import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.base.messagequeue.MessageQueue.Message
import kotlin.time.Duration

interface MessageQueue<T : Message> {
    val message: StateFlow<T?>

    fun addMessage(message: T)
    fun removeCurrentMessage()

    interface Message {
        val duration: Duration
    }
}
