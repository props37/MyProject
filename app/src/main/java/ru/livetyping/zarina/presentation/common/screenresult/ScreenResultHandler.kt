package ru.livetyping.zarina.presentation.common.screenresult

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.presentation.navigation.base.ScreenResult
import timber.log.Timber

class ScreenResultHandler(val savedStateHandle: SavedStateHandle) {

    suspend inline fun <reified R : ScreenResult> handle(
        resultFlow: StateFlow<R?>,
        key: String,
        crossinline action: suspend (R) -> Unit,
    ) {
        val prevHandledResultIdKey = createPrevHandledResultIdKey(key)
        resultFlow.collect { result ->
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
