package ru.zarina.zarina.ui.common.base.sideeffectsource

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SideEffectSourceImpl<T : SideEffectSource.SideEffect> : SideEffectSource<T> {
    private val _sideEffects = Channel<T>(Channel.UNLIMITED)
    override val sideEffects = _sideEffects.receiveAsFlow()

    override fun emitSideEffect(sideEffect: T) {
        _sideEffects.trySend(sideEffect)
    }
}
