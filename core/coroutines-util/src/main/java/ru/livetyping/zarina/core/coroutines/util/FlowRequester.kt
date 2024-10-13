package ru.livetyping.zarina.core.coroutines.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

public class FlowRequester<T, R : FlowRequest>(
    initialRequest: R? = null,
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
    public val flow: Flow<T> = requests.receiveAsFlow()
        .flatMapLatest { request ->
            _loadingState.value = LoadingState.Loading(request)
            flowBuilder(flowBuilderScopeImpl, request)
        }
        .onEach {
            _loadingState.value = LoadingState.NotLoading
        }

    init {
        if (initialRequest != null) {
            request(initialRequest)
        }
    }

    public fun request(request: R) {
        requests.trySend(request)
    }

    public sealed class LoadingState {
        @OptIn(ExperimentalContracts::class)
        public fun isLoading(): Boolean {
            contract {
                returns(true) implies (this@LoadingState is Loading)
            }

            return this is Loading
        }

        public val loadingRequest: FlowRequest?
            get() = if (this is Loading) this.request else null

        public data object NotLoading : LoadingState()

        public data class Loading(val request: FlowRequest) : LoadingState()
    }

    public interface FlowBuilderScope<R : FlowRequest> {
        public fun markAsLoading(request: R)
    }
}

public interface FlowRequest
