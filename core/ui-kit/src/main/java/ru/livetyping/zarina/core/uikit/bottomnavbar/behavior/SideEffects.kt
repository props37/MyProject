package ru.livetyping.zarina.core.uikit.bottomnavbar.behavior

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
public fun BottomNavBarBehavior(isVisible: Boolean, isAnimated: Boolean = true) {
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
