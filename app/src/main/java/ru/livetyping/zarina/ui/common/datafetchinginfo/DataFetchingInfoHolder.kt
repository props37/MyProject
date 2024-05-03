package ru.livetyping.zarina.ui.common.datafetchinginfo

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class DataFetchingInfoHolder<F> {
    private val _fetchingRequests = Channel<Unit>(Channel.CONFLATED)
    val fetchingRequests: Flow<Unit> = _fetchingRequests.receiveAsFlow()

    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()

    private val _fetchingType = MutableStateFlow<F?>(null)
    val fetchingType: StateFlow<F?> = _fetchingType.asStateFlow()

    fun requestFetching(fetchingType: F) {
        _fetchingRequests.trySend(Unit)
        _isFetching.value = true
        _fetchingType.value = fetchingType
    }

    fun completeFetching() {
        _isFetching.value = false
        _fetchingType.value = null
    }
}
