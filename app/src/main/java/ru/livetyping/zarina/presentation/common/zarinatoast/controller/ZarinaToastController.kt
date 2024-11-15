package ru.livetyping.zarina.presentation.common.zarinatoast.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage

interface ZarinaToastController {
    val currentMessage: StateFlow<ZarinaToastMessage?>

    fun show(message: ZarinaToastMessage, removePreviousMessage: Boolean = true)
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
