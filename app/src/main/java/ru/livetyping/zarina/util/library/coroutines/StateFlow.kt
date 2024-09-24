package ru.livetyping.zarina.util.library.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Suppress("NOTHING_TO_INLINE", "FunctionName")
inline fun <T> ImmutableStateFlow(value: T): StateFlow<T> = MutableStateFlow(value).asStateFlow()

fun <T, R> StateFlow<T>.mapState(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.WhileSubscribed(),
    transform: (T) -> R,
): StateFlow<R> {
    return this
        .map(transform)
        .stateIn(scope, started, transform(this.value))
}
