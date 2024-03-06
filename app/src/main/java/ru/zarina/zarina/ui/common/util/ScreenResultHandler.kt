package ru.zarina.zarina.ui.common.util

import androidx.lifecycle.SavedStateHandle
import ru.zarina.zarina.ui.navigation.base.ScreenResult
import timber.log.Timber

class ScreenResultHandler(
    val backStackEntrySavedStateHandle: SavedStateHandle,
    val savedStateHandle: SavedStateHandle,
) {
    suspend inline fun <reified R : ScreenResult> handle(
        key: String,
        crossinline action: suspend (R) -> Unit,
    ) {
        val prevHandledResultIdKey = createPrevHandledResultIdKey(key)
        backStackEntrySavedStateHandle
            .getStateFlow<R?>(
                key = key,
                initialValue = null,
            )
            .collect { result ->
                val prevHandledResultId: String? = savedStateHandle[prevHandledResultIdKey]
                if (result != null && result.id != prevHandledResultId) {
                    Timber.tag(TAG).v("Screen result: $result")
                    action(result)
                    savedStateHandle[prevHandledResultIdKey] = result.id
                }
            }
    }

    fun createPrevHandledResultIdKey(resultKey: String): String {
        return PREV_HANDLED_RESULT_ID_KEY_PREFIX + resultKey
    }

    companion object {
        private const val PREV_HANDLED_RESULT_ID_KEY_PREFIX = "prev_handled_"
        const val TAG = "ScreenResultHandler"
    }
}
