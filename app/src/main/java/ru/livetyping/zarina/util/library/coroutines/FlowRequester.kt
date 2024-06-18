package ru.livetyping.zarina.util.library.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach

class FlowRequester<T, R : FlowRequester.Request>(
    initialRequest: R? = null,
    flowBuilder: (R) -> Flow<T>,
) {
    private val requests = MutableSharedFlow<R>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.NotLoading)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val flow: Flow<T> = requests
        .flatMapLatest {
            _loadingState.value = LoadingState.Loading(it)
            flowBuilder(it)
        }
        .onEach {
            _loadingState.value = LoadingState.NotLoading
        }

    init {
        if (initialRequest != null) {
            request(initialRequest)
        }
    }

    fun request(request: R) {
        requests.tryEmit(request)
    }

    sealed class LoadingState {
        abstract val isLoading: Boolean

        data object NotLoading : LoadingState() {
            override val isLoading: Boolean get() = false
        }

        data class Loading<R : Request>(val request: R) : LoadingState() {
            override val isLoading: Boolean get() = true
        }
    }

    interface Request
}
