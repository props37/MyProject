package ru.livetyping.zarina.presentation.common.behavior.screenbrightness

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun ForcedScreenBrightnessBehavior(brightness: ScreenBrightness) {
    val controller = LocalScreenBrightnessBehaviorController.current
    DisposableEffect(brightness, controller) {
        val behavior = ScreenBrightnessBehavior(brightness)
        controller.push(behavior)
        onDispose { controller.pop(behavior) }
    }
}
