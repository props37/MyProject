package ru.zarina.zarina.ui.common.base.behavior

import kotlinx.coroutines.flow.StateFlow

interface IBehaviorController<T : Behavior> {
    val current: StateFlow<T>
    fun setDefault(behavior: T)
    fun push(behavior: T)
    fun pop(behavior: T)
}
