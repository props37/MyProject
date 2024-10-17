package ru.livetyping.zarina.core.uicommon.sideeffect

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

public class SideEffectSourceImpl<T : SideEffect> : SideEffectSource<T> {
    private val _sideEffects = Channel<T>(Channel.UNLIMITED)
    override val sideEffects: Flow<T> = _sideEffects.receiveAsFlow()

    override fun emitSideEffect(sideEffect: T) {
        _sideEffects.trySend(sideEffect)
    }
}
