package ru.livetyping.zarina.base.messagequeue

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
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class MessageQueueImpl<T : MessageQueue.Message>(
    private val coroutineScope: CoroutineScope,
    private val delayBetweenMessages: Duration = DELAY_BETWEEN_MESSAGES_DEFAULT_VALUE,
) : MessageQueue<T> {
    private val queue = Channel<T>(Channel.UNLIMITED)

    private val _currentMessage = MutableStateFlow<T?>(null)
    override val currentMessage: StateFlow<T?> = _currentMessage.asStateFlow()

    private var currentMessageJob: Job? = null
    private var delayBetweenMessagesJob: Job? = null

    init {
        check(delayBetweenMessages > Duration.ZERO) {
            "Delay between messages $delayBetweenMessages should be positive"
        }

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
                    val context =
                        if (message.isRemovable) EmptyCoroutineContext else NonCancellable
                    withContext(context) {
                        try {
                            Timber.tag(TAG).v("Process message: $message")
                            _currentMessage.value = message
                            delay(message.duration)
                        } finally {
                            _currentMessage.value = null
                        }
                    }
                }
                currentMessageJob?.join()

                delayBetweenMessagesJob = coroutineScope.launch {
                    withContext(NonCancellable) {
                        delay(delayBetweenMessages)
                    }
                }
                delayBetweenMessagesJob?.join()
            }
            .launchIn(coroutineScope)
    }

    companion object {
        private val DELAY_BETWEEN_MESSAGES_DEFAULT_VALUE: Duration get() = 0.5.seconds

        private const val TAG = "MessageQueueImpl"
    }
}
