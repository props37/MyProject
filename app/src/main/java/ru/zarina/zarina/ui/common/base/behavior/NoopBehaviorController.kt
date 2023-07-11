package ru.zarina.zarina.ui.common.base.behavior

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NoopBehaviorController<T : Behavior>(
    default: T,
) : IBehaviorController<T> {
    override val current: StateFlow<T> = MutableStateFlow(default)
    override fun setDefault(behavior: T) = Unit
    override fun pop(behavior: T) = Unit
    override fun push(behavior: T) = Unit
}
