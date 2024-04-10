package ru.livetyping.zarina.base.sideeffectsource

import kotlinx.coroutines.flow.Flow

interface SideEffectSource<T : SideEffectSource.SideEffect> {
    val sideEffects: Flow<T>

    fun emitSideEffect(sideEffect: T)

    interface SideEffect
}
