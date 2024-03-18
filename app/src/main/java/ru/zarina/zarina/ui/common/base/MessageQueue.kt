package ru.zarina.zarina.ui.common.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.zarina.zarina.ui.common.text.Text
import timber.log.Timber
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class MessageQueue(
    private val coroutineScope: CoroutineScope,
    private val delayBetweenMessages: Duration = DEFAULT_DELAY_BETWEEN_MESSAGES,
) {

    private val _isMessageVisible = MutableStateFlow(false)
    val isMessageVisible = _isMessageVisible.asStateFlow()

    private val _message = MutableStateFlow<Text>(Text.Empty)
    val message = _message.asStateFlow()

    private var job: Job? = null

    private val queue = Channel<Message>(Channel.UNLIMITED)

    init {
        queue
            .receiveAsFlow()
            .onEach {
                job?.cancelAndJoin()
                hideMessage()
                coroutineScope.launch { showMessage(it) }
            }
            .launchIn(coroutineScope)
    }

    fun showMessage(
        text: Text,
        duration: Duration = DEFAULT_MESSAGE_DURATION,
        minDuration: Duration = DEFAULT_MESSAGE_MIN_DURATION,
    ) {
        check(duration >= minDuration)
        queue.trySend(Message(text, duration, minDuration))
    }

    private suspend fun showMessage(
        message: Message,
    ) {
        job = coroutineScope.launch {
            withContext(NonCancellable) {
                _message.value = message.text
                _isMessageVisible.value = true
                Timber.v("Shown message: $message")
                delay(message.minDuration)
            }
            delay(message.duration - message.minDuration)
            withContext(NonCancellable) {
                hideMessage()
            }
        }
    }

    private suspend fun hideMessage() {
        if (_isMessageVisible.value) {
            _isMessageVisible.value = false
            delay(delayBetweenMessages)
        }
    }

    data class Message(val text: Text, val duration: Duration, val minDuration: Duration)

    companion object {
        private val DEFAULT_DELAY_BETWEEN_MESSAGES = 0.5.seconds
        private val DEFAULT_MESSAGE_DURATION = 3.seconds
        private val DEFAULT_MESSAGE_MIN_DURATION = 1.seconds
    }
}
