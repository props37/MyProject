package ru.livetyping.zarina.core.ui.compose.systembars

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
public fun ForcedSystemBarsBehavior(
    isStatusBarContentLight: Boolean = false,
    isNavigationBarContentLight: Boolean = false,
) {
    val controller = LocalSystemBarsBehaviorController.current
    DisposableEffect(isStatusBarContentLight, isNavigationBarContentLight, controller) {
        val behavior = SystemBarsBehavior(isStatusBarContentLight, isNavigationBarContentLight)
        controller.push(behavior)
        onDispose { controller.pop(behavior) }
    }
}
