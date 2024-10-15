package ru.livetyping.zarina.core.ui.common.behavior

import kotlinx.coroutines.flow.StateFlow

public interface BehaviorController<T : Behavior> {
    public val currentBehavior: StateFlow<T>

    public fun setDefaultBehavior(behavior: T)

    public fun push(behavior: T)

    public fun pop(behavior: T)
}
