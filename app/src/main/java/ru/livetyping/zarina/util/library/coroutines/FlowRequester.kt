package ru.livetyping.zarina.util.library.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class FlowRequester<T, R : FlowRequester.Request>(
    initialRequest: R? = null,
    flowBuilder: (R) -> Flow<T>,
) {
    private val requests = Channel<R>(Channel.CONFLATED)

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.NotLoading)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val flow: Flow<T> = requests.receiveAsFlow()
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
        requests.trySend(request)
    }

    sealed class LoadingState {
        val isLoading: Boolean get() = this is Loading

        data object NotLoading : LoadingState()

        data class Loading(val request: Request) : LoadingState()
    }

    interface Request
}
