package ru.zarina.zarina.ui.common.behavior.navigationbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Deprecated(
    message = "Use ForcedBottomNavBarBehavior instead.",
    replaceWith = ReplaceWith(
        expression = "ForcedBottomNavBarBehavior",
        "ru.zarina.zarina.ui.common.behavior.bottomnavbar",
    )
)
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
        bottomBarController.push(behavior)
        this.onDispose {
            bottomBarController.pop(behavior)
        }
    }
}
