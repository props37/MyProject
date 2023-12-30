package ru.zarina.zarina.ui.common.base.behavior

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NoopBehaviorController<T : Behavior>(defaultBehavior: T) : BehaviorController<T> {
    override val currentBehavior: StateFlow<T> = MutableStateFlow(defaultBehavior)

    override fun setDefaultBehavior(behavior: T) {
        throw NotImplementedError()
    }

    override fun push(behavior: T) {
        throw NotImplementedError()
    }

    override fun pop(behavior: T) {
        throw NotImplementedError()
    }
}
