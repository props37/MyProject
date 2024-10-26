package ru.livetyping.zarina.core.uicommon.behavior

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ConcurrentLinkedDeque

public class DefaultBehaviorController<T : Behavior>(
    private var defaultBehavior: T,
) : BehaviorController<T> {
    private val behaviorDeque = ConcurrentLinkedDeque<T>()

    private val _currentBehavior = MutableStateFlow(defaultBehavior)
    override val currentBehavior: StateFlow<T> = _currentBehavior.asStateFlow()

    override fun setDefaultBehavior(behavior: T) {
        defaultBehavior = behavior
        updateCurrentBehavior()
    }

    override fun push(behavior: T) {
        behaviorDeque.addFirst(behavior)
        updateCurrentBehavior()
    }

    override fun pop(behavior: T) {
        behaviorDeque.remove(behavior)
        updateCurrentBehavior()
    }

    private fun updateCurrentBehavior() {
        _currentBehavior.value = behaviorDeque.peekFirst() ?: defaultBehavior
    }
}
