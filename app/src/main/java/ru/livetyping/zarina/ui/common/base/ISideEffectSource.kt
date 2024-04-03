package ru.livetyping.zarina.ui.common.base

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface ISideEffectSource<T : ISideEffectSource.ISideEffect> {

    val sideEffects: Flow<T>

    fun sideEffect(sideEffect: T)

    interface ISideEffect
}

class SideEffectQueue<T : ISideEffectSource.ISideEffect> : ISideEffectSource<T> {

    private val _sideEffects = Channel<T>(Channel.UNLIMITED)
    override val sideEffects = _sideEffects.receiveAsFlow()

    override fun sideEffect(sideEffect: T) {
        _sideEffects.trySend(sideEffect)
    }

}
