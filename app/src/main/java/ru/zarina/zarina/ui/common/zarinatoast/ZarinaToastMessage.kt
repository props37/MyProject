package ru.zarina.zarina.ui.common.zarinatoast

import ru.zarina.zarina.base.messagequeue.MessageQueue
import ru.zarina.zarina.ui.base.text.Text
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class ZarinaToastMessage(
    val text: Text,
    val style: ZarinaToastMessageStyle = ZarinaToastMessageStyle.DEFAULT,
    override val duration: Duration = DURATION_SHORT,
    override val isRemovable: Boolean = true,
) : MessageQueue.Message {
    companion object {
        // According to Android toast lengths
        val DURATION_SHORT: Duration get() = 2.seconds
        val DURATION_LONG: Duration get() = 3.5.seconds
    }
}
