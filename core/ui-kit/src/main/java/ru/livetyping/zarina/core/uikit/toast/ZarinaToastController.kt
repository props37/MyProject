package ru.livetyping.zarina.core.uikit.toast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

public interface ZarinaToastController {
    public val currentMessage: StateFlow<ZarinaToastMessage?>

    public fun show(message: ZarinaToastMessage, removePreviousMessage: Boolean = true)
    public fun hideCurrentToast()
}

public val LocalZarinaToastController: ProvidableCompositionLocal<ZarinaToastController> =
    staticCompositionLocalOf { NoOpZarinaToastController() }

@Composable
public fun rememberZarinaToastController(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): ZarinaToastController {
    return remember(coroutineScope) { ZarinaToastControllerImpl(coroutineScope) }
}
