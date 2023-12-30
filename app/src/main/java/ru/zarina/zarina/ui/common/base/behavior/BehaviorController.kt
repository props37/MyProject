package ru.zarina.zarina.ui.common.base.behavior

import kotlinx.coroutines.flow.StateFlow

interface BehaviorController<T : Behavior> {
    val currentBehavior: StateFlow<T>

    fun setDefaultBehavior(behavior: T)
    fun push(behavior: T)
    fun pop(behavior: T)
}
