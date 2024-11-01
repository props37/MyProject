package ru.livetyping.zarina.core.uikit.toast

import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.message.MessageQueue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public data class ZarinaToastMessage(
    val text: Text,
    val style: ZarinaToastMessageStyle = ZarinaToastMessageStyle.DEFAULT,
    override val duration: Duration = DURATION_SHORT,
    override val isRemovable: Boolean = true,
) : MessageQueue.Message {
    public companion object {
        // According to Android toast lengths
        public val DURATION_SHORT: Duration get() = 2.seconds
        public val DURATION_LONG: Duration get() = 3.5.seconds

        public fun error(
            text: Text,
            duration: Duration = DURATION_SHORT,
            isRemovable: Boolean = true,
        ): ZarinaToastMessage = ZarinaToastMessage(
            text = text,
            style = ZarinaToastMessageStyle.ERROR,
            duration = duration,
            isRemovable = isRemovable,
        )
    }
}
