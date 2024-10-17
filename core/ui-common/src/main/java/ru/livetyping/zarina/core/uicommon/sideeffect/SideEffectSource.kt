package ru.livetyping.zarina.core.uicommon.sideeffect

import kotlinx.coroutines.flow.Flow

public interface SideEffectSource<T : SideEffect> {
    public val sideEffects: Flow<T>

    public fun emitSideEffect(sideEffect: T)
}
