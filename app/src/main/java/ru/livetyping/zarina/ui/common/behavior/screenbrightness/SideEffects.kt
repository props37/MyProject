package ru.livetyping.zarina.ui.common.behavior.screenbrightness

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun ForcedScreenBrightnessBehavior(brightness: Float?) {
    val controller = LocalScreenBrightnessBehaviorController.current
    DisposableEffect(brightness, controller) {
        val behavior = ScreenBrightnessBehavior(brightness)
        controller.push(behavior)
        onDispose { controller.pop(behavior) }
    }
}
