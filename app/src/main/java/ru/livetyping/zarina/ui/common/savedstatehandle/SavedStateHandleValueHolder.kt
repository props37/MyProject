package ru.livetyping.zarina.ui.common.savedstatehandle

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow

fun <T> SavedStateHandle.createValueHolder(
    key: String,
    initialValue: T,
): SavedStateHandleValueHolder<T> = SavedStateHandleValueHolder(
    key = key,
    initialValue = initialValue,
    savedStateHandle = this,
)

class SavedStateHandleValueHolder<T>(
    private val key: String,
    initialValue: T,
    private val savedStateHandle: SavedStateHandle,
) {
    val stateFlow: StateFlow<T> = savedStateHandle.getStateFlow(key, initialValue)

    fun getValue(): T? = savedStateHandle[key]

    fun setValue(value: T) {
        savedStateHandle[key] = value
    }
}
