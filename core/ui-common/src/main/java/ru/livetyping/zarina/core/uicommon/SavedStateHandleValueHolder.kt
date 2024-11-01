package ru.livetyping.zarina.core.uicommon

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow

public fun <T> SavedStateHandle.createValueHolder(
    key: String,
    initialValue: T,
): SavedStateHandleValueHolder<T> = SavedStateHandleValueHolder(
    key = key,
    initialValue = initialValue,
    savedStateHandle = this,
)

public class SavedStateHandleValueHolder<T>(
    private val key: String,
    initialValue: T,
    private val savedStateHandle: SavedStateHandle,
) {
    public val stateFlow: StateFlow<T> = savedStateHandle.getStateFlow(key, initialValue)

    public fun get(): T = stateFlow.value

    public fun set(value: T) {
        savedStateHandle[key] = value
    }
}
