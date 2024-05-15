package ru.livetyping.zarina.presentation.common.behavior.bottomnavbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun ForcedBottomNavBarBehavior(isVisible: Boolean, isAnimated: Boolean = true) {
    val controller = LocalBottomNavBarBehaviorController.current
    DisposableEffect(isVisible, isAnimated, controller) {
        val behavior = if (isVisible) {
            BottomNavBarBehavior.Visible(isAnimated)
        } else {
            BottomNavBarBehavior.Hidden(isAnimated)
        }
        controller.push(behavior)
        onDispose { controller.pop(behavior) }
    }
}
