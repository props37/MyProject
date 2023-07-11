package ru.zarina.zarina.ui.common.base.behavior

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BehaviorController<T : Behavior>(default: T) :
    IBehaviorController<T> {

    private var _default = default

    private val stack = ArrayDeque<T>()
    private val _current = MutableStateFlow(default)
    override val current = _current.asStateFlow()

    @Synchronized
    override fun setDefault(behavior: T) {
        _default = behavior
        updateCurrent()
    }

    @Synchronized
    override fun push(behavior: T) {
        stack.addFirst(behavior)
        updateCurrent()
    }

    @Synchronized
    override fun pop(behavior: T) {
        val lastIndex = stack.indexOfLast { it == behavior }
        stack.removeAt(lastIndex)
        updateCurrent()
    }

    private fun updateCurrent() {
        _current.value = stack.firstOrNull() ?: _default
    }

}
