package ru.zarina.zarina.ui.common.behavior.navigationbar

import androidx.compose.runtime.staticCompositionLocalOf
import ru.zarina.zarina.base.behavior.BehaviorController
import ru.zarina.zarina.base.behavior.NoOpBehaviorController

@Deprecated(
    message = "Use BottomNavBarBehaviorController instead.",
    replaceWith = ReplaceWith(
        expression = "BottomNavBarBehaviorController",
        "ru.zarina.zarina.ui.common.behavior.bottomnavbar",
    ),
)
typealias NavigationBarController = BehaviorController<NavigationBarBehavior>

@Deprecated(
    message = "Use LocalBottomNavBarBehaviorController instead.",
    replaceWith = ReplaceWith(
        expression = "LocalBottomNavBarBehaviorController",
        "ru.zarina.zarina.ui.common.behavior.bottomnavbar",
    ),
)
val LocalNavigationBarController = staticCompositionLocalOf<NavigationBarController> {
    NoOpBehaviorController(NavigationBarBehavior.DEFAULT)
}
