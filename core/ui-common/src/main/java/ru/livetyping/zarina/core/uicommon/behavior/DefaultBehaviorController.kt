package ru.livetyping.zarina.core.uicommon.behavior

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

public class DefaultBehaviorController<T : Behavior>(
    private var defaultBehavior: T,
) : BehaviorController<T> {
    // TODO: [High] Migrate to concurrent collection
    private val behaviorStack = ArrayDeque<T>()
    private val behaviorStackLock = Any()

    private val _currentBehavior = MutableStateFlow(defaultBehavior)
    override val currentBehavior: StateFlow<T> = _currentBehavior.asStateFlow()

    override fun setDefaultBehavior(behavior: T) {
        defaultBehavior = behavior
        updateCurrentBehavior()
    }

    override fun push(behavior: T) {
        synchronized(behaviorStackLock) {
            behaviorStack.addFirst(behavior)
        }
        updateCurrentBehavior()
    }

    override fun pop(behavior: T) {
        synchronized(behaviorStackLock) {
            behaviorStack.remove(behavior)
        }
        updateCurrentBehavior()
    }

    private fun updateCurrentBehavior() {
        _currentBehavior.value = behaviorStack.firstOrNull() ?: defaultBehavior
    }
}
