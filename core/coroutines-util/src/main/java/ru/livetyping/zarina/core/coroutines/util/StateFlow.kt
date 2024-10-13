package ru.livetyping.zarina.core.coroutines.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Suppress("NOTHING_TO_INLINE", "FunctionName")
public inline fun <T> ReadOnlyStateFlow(value: T): StateFlow<T> =
    MutableStateFlow(value).asStateFlow()

public fun <T, R> StateFlow<T>.mapState(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.WhileSubscribed(),
    transform: (T) -> R,
): StateFlow<R> {
    return this
        .map(transform)
        .stateIn(scope, started, transform(this.value))
}
