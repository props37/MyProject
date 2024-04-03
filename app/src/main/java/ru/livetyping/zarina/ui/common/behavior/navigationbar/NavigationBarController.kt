package ru.livetyping.zarina.ui.common.behavior.navigationbar

import androidx.compose.runtime.staticCompositionLocalOf
import ru.livetyping.zarina.base.behavior.BehaviorController
import ru.livetyping.zarina.base.behavior.NoOpBehaviorController

@Deprecated(
    message = "Use BottomNavBarBehaviorController instead.",
    replaceWith = ReplaceWith(
        expression = "BottomNavBarBehaviorController",
        "ru.livetyping.zarina.ui.common.behavior.bottomnavbar",
    ),
)
typealias NavigationBarController = BehaviorController<NavigationBarBehavior>

@Deprecated(
    message = "Use LocalBottomNavBarBehaviorController instead.",
    replaceWith = ReplaceWith(
        expression = "LocalBottomNavBarBehaviorController",
        "ru.livetyping.zarina.ui.common.behavior.bottomnavbar",
    ),
)
val LocalNavigationBarController = staticCompositionLocalOf<NavigationBarController> {
    NoOpBehaviorController(NavigationBarBehavior.DEFAULT)
}
