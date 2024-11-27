package ru.livetyping.zarina.core.uikit.toast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastController
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastControllerImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

public val LocalZarinaToastController: ProvidableCompositionLocal<ZarinaToastController> =
    staticCompositionLocalOf { NoOpZarinaToastController() }

@Composable
public fun rememberZarinaToastController(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): ZarinaToastController {
    return remember(coroutineScope) { ZarinaToastControllerImpl(coroutineScope) }
}

internal class NoOpZarinaToastController : ZarinaToastController {
    override val currentMessage: StateFlow<ZarinaToastMessage?>
        get() = throw NotImplementedError()

    override fun show(message: ZarinaToastMessage, removePreviousMessage: Boolean) {
        throw NotImplementedError()
    }

    override fun hideCurrentToast() {
        throw NotImplementedError()
    }
}
