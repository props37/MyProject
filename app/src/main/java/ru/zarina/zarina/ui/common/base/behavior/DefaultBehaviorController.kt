package ru.zarina.zarina.ui.common.base.behavior

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultBehaviorController<T : Behavior>(
    private var defaultBehavior: T,
) : BehaviorController<T> {
    private val behaviorStack = ArrayDeque<T>()

    private val _currentBehavior = MutableStateFlow(defaultBehavior)
    override val currentBehavior: StateFlow<T> = _currentBehavior.asStateFlow()

    @Synchronized
    override fun setDefaultBehavior(behavior: T) {
        defaultBehavior = behavior
        updateCurrentBehavior()
    }

    @Synchronized
    override fun pushBehavior(behavior: T) {
        behaviorStack.addFirst(behavior)
        updateCurrentBehavior()
    }

    @Synchronized
    override fun popBehavior(behavior: T) {
        behaviorStack.remove(behavior)
        updateCurrentBehavior()
    }

    private fun updateCurrentBehavior() {
        _currentBehavior.value = behaviorStack.firstOrNull() ?: defaultBehavior
    }
}
