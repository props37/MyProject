package ru.livetyping.zarina.core.uicommon.behavior

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

public class DefaultBehaviorController<T : Behavior>(
    private var defaultBehavior: T,
) : BehaviorController<T> {
    private val behaviorStack = ArrayDeque<T>()

    private val _currentBehavior = MutableStateFlow(defaultBehavior)
    override val currentBehavior: StateFlow<T> = _currentBehavior.asStateFlow()

    override fun setDefaultBehavior(behavior: T) {
        defaultBehavior = behavior
        updateCurrentBehavior()
    }

    override fun push(behavior: T) {
        behaviorStack.addFirst(behavior)
        updateCurrentBehavior()
    }

    override fun pop(behavior: T) {
        behaviorStack.remove(behavior)
        updateCurrentBehavior()
    }

    @Synchronized
    private fun updateCurrentBehavior() {
        _currentBehavior.value = behaviorStack.firstOrNull() ?: defaultBehavior
    }
}
