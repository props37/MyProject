package ru.zarina.zarina.ui.common.behavior.navigationbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun NavigationBarState(
    isVisible: Boolean,
    isAnimated: Boolean,
) {
    val bottomBarController = LocalNavigationBarController.current
    DisposableEffect(bottomBarController, isVisible, isAnimated) {
        val behavior = if (isVisible)
            NavigationBarBehavior.Visible(isAnimated)
        else
            NavigationBarBehavior.Hidden(isAnimated)
        bottomBarController.pushBehavior(behavior)
        this.onDispose {
            bottomBarController.popBehavior(behavior)
        }
    }
}
