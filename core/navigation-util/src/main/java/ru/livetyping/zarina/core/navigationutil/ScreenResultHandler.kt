package ru.livetyping.zarina.core.navigationutil

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.navigation.ScreenResult

public class ScreenResultHandler(public val savedStateHandle: SavedStateHandle) {
    public suspend inline fun <reified R : ScreenResult> handle(
        resultFlow: Flow<R?>,
        key: String,
        crossinline action: suspend (R) -> Unit,
    ) {
        val prevHandledResultIdKey = createPrevHandledResultIdKey(key)
        resultFlow.collect { result ->
            val prevHandledResultId: String? = savedStateHandle[prevHandledResultIdKey]
            if (result != null && result.id != prevHandledResultId) {
                action(result)
                savedStateHandle[prevHandledResultIdKey] = result.id
            }
        }
    }

    public fun createPrevHandledResultIdKey(resultKey: String): String {
        return PREV_HANDLED_RESULT_ID_KEY_PREFIX + resultKey
    }

    private companion object {
        private const val PREV_HANDLED_RESULT_ID_KEY_PREFIX = "prev_handled_"
    }
}
