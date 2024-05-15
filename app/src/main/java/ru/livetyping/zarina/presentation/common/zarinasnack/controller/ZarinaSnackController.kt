package ru.livetyping.zarina.presentation.common.zarinasnack.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.presentation.common.zarinasnack.ZarinaSnackMessage

interface ZarinaSnackController {
    val currentMessage: StateFlow<ZarinaSnackMessage?>

    fun show(message: ZarinaSnackMessage)
    fun hideCurrentSnack()
}

val LocalZarinaSnackController = staticCompositionLocalOf<ZarinaSnackController> {
    NoOpZarinaSnackController()
}

@Composable
fun rememberZarinaSnackController(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) : ZarinaSnackController {
    return remember(coroutineScope) { ZarinaSnackControllerImpl(coroutineScope) }
}
