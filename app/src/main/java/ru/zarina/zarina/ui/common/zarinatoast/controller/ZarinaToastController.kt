package ru.zarina.zarina.ui.common.zarinatoast.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.ui.common.ZarinaMessage

interface ZarinaToastController {
    val currentMessage: StateFlow<ZarinaMessage?>

    fun show(message: ZarinaMessage)
    fun hideCurrentToast()
}

val LocalZarinaToastController = staticCompositionLocalOf<ZarinaToastController> {
    NoOpZarinaToastController()
}

@Composable
fun rememberZarinaToastController(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): ZarinaToastController {
    return remember(coroutineScope) { ZarinaToastControllerImpl(coroutineScope) }
}
