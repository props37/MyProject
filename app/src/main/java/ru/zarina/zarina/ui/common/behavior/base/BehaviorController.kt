package ru.zarina.zarina.ui.common.behavior.base

import kotlinx.coroutines.flow.StateFlow

interface BehaviorController<T : Behavior> {
    val currentBehavior: StateFlow<T>

    fun setDefaultBehavior(behavior: T)
    fun push(behavior: T)
    fun pop(behavior: T)
}
