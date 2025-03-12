package ru.livetyping.zarina.core.coroutinesutil

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn

public class FlowRequester<T, R : FlowRequest>(
    initialRequest: R? = null,
    coroutineScope: CoroutineScope,
    flowBuilder: suspend FlowBuilderScope<R>.(R) -> Flow<T>,
) {
    private val requests = Channel<R>(Channel.CONFLATED)

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.NotLoading)
    public val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val flowBuilderScopeImpl = object : FlowBuilderScope<R> {
        override fun markAsLoading(request: R) {
            _loadingState.value = LoadingState.Loading(request)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    public val flow: SharedFlow<T> = requests.receiveAsFlow()
        .flatMapLatest { request ->
            _loadingState.value = LoadingState.Loading(request)
            flowBuilder(flowBuilderScopeImpl, request)
        }
        .onEach {
            _loadingState.value = LoadingState.NotLoading
        }
        .shareIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    init {
        if (initialRequest != null) {
            request(initialRequest)
        }
    }

    public fun request(request: R) {
        requests.trySend(request)
    }

    public sealed class LoadingState {
        public open val loadingRequest: FlowRequest?
            get() = if (this is Loading) this.request else null

        @Suppress("NOTHING_TO_INLINE")
        public inline fun isLoading(): Boolean = this is Loading

        public data object NotLoading : LoadingState()

        public data class Loading(val request: FlowRequest) : LoadingState() {
            @Deprecated(
                message = "Use request directly instead",
                level = DeprecationLevel.HIDDEN,
            )
            override val loadingRequest: FlowRequest
                get() = request
        }
    }

    public interface FlowBuilderScope<R : FlowRequest> {
        public fun markAsLoading(request: R)
    }
}

public interface FlowRequest
