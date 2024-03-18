package ru.zarina.zarina.base.messagequeue

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class MessageQueueImpl<T : MessageQueue.Message>(
    private val coroutineScope: CoroutineScope,
    private val delayBetweenMessages: Duration = DELAY_BETWEEN_MESSAGES_DEFAULT_VALUE,
) : MessageQueue<T> {
    private val queue = Channel<T>(Channel.UNLIMITED)

    private val _message = MutableStateFlow<T?>(null)
    override val message: StateFlow<T?> = _message.asStateFlow()

    private var currentMessageJob: Job? = null
    private var delayBetweenMessagesJob: Job? = null

    init {
        processMessages()
    }

    override fun addMessage(message: T) {
        check(message.duration > Duration.ZERO) {
            "Message duration ${message.duration} should be positive"
        }
        queue.trySend(message)
    }

    override fun removeCurrentMessage() {
        currentMessageJob?.cancel()
    }

    private fun processMessages() {
        queue
            .receiveAsFlow()
            .onEach { message ->
                currentMessageJob?.join()
                delayBetweenMessagesJob?.join()

                currentMessageJob = coroutineScope.launch {
                    Timber.tag(TAG).v("Process message: $message")
                    _message.value = message
                    delay(message.duration)
                    _message.value = null
                }

                delayBetweenMessagesJob = coroutineScope.launch(NonCancellable) {
                    delay(delayBetweenMessages)
                }
            }
            .launchIn(coroutineScope)
    }

    companion object {
        private val DELAY_BETWEEN_MESSAGES_DEFAULT_VALUE: Duration get() = 0.5.seconds

        private const val TAG = "MessageQueueImpl"
    }
}
