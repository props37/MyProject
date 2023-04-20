package ru.zarina.zarina.utils.coroutine

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch

inline fun <F, reified T : Throwable> Flow<F>.catch(
    crossinline action: suspend FlowCollector<F>.(T) -> Unit,
): Flow<F> {
    return this.catch { if (it is T) this.action(it) else throw it }
}
