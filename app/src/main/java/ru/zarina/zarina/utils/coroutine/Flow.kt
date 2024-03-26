package ru.zarina.zarina.utils.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

inline fun <F, reified T : Throwable> Flow<F>.catch(
    crossinline action: suspend FlowCollector<F>.(T) -> Unit,
): Flow<F> {
    return this.catch { if (it is T) this.action(it) else throw it }
}

@Deprecated("Use mapState instead.")
fun <T, R> StateFlow<T>.mapState(
    coroutineScope: CoroutineScope,
    started: SharingStarted = SharingStarted.WhileSubscribed(),
    transform: (T) -> R,
): StateFlow<R> {
    return this
        .map(transform)
        .stateIn(coroutineScope, started, transform(this.value))
}
