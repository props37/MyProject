package ru.zarina.zarina.ui.common.behavior.base

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NoOpBehaviorController<T : Behavior>(
    defaultBehavior: T,
    private val throwExceptions: Boolean = true,
) : BehaviorController<T> {
    override val currentBehavior: StateFlow<T> = MutableStateFlow(defaultBehavior)

    override fun setDefaultBehavior(behavior: T) {
        if (throwExceptions) throw NotImplementedError()
    }

    override fun push(behavior: T) {
        if (throwExceptions) throw NotImplementedError()
    }

    override fun pop(behavior: T) {
        if (throwExceptions) throw NotImplementedError()
    }
}
