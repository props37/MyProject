package ru.zarina.zarina.ui.common.base.behavior

import kotlinx.coroutines.flow.StateFlow

interface BehaviorController<T : Behavior> {
    val currentBehavior: StateFlow<T>

    fun setDefaultBehavior(behavior: T)
    fun pushBehavior(behavior: T)
    fun popBehavior(behavior: T)
}
