package ru.livetyping.zarina.core.uicommon.behavior

import kotlinx.coroutines.flow.StateFlow

public class NoOpBehaviorController<T : Behavior> : BehaviorController<T> {
    override val currentBehavior: StateFlow<T>
        get() = throw NotImplementedError()

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
